package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithm;
import com.ds4h.model.alignment.alignmentAlgorithm.PointOverloadEnum;
import com.ds4h.model.alignment.automatic.pointDetector.MatCache;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.alignment.preprocessImage.TargetImagePreprocessing;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.IJ;

import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class is used for the alignment algorithms. Inside this class we can found all the methods to perform the alignment.
 * Every child class must implement the 'align' method.
 */
public class Alignment implements Runnable{
    private ImagePoints targetImage;
    private final List<ImagePoints> imagesToAlign;
    private final List<AlignedImage> alignedImages;
    private Thread thread;
    private AlignmentEnum type;
    private PointDetector pointDetector;
    private AlignmentAlgorithm algorithm;
    private int status = 0;
    private int total = 2;

    public Alignment(){
        this.thread = new Thread(this);
        this.imagesToAlign = new LinkedList<>();
        this.alignedImages = new CopyOnWriteArrayList<>();
    }

    /**
     * When this method is called we start the real process of image alignment. If the thread is not alive and the
     * target image is present we can start the operation. Take in mind that the alignment is done inside a separate Thread,
     * so if you need the images you have to wait until the alignment is not done.
     * @param pointManager  container where all the images are stored with their points
     * @throws IllegalArgumentException If the targetImage is not present or if the cornerManager is null, an exception
     * is thrown.
     */
    public void alignImages(final PointManager pointManager, final AlignmentAlgorithm algorithm, final AlignmentEnum type) throws IllegalArgumentException {
        if (Objects.nonNull(pointManager) && pointManager.getSourceImage().isPresent()) {
            if (!this.isAlive()) {
                this.targetImage = pointManager.getSourceImage().get();
                this.alignedImages.clear();
                this.imagesToAlign.clear();
                this.type = type;
                this.algorithm = algorithm;
                try {
                    this.imagesToAlign.addAll(pointManager.getImagesToAlign());
                    this.thread.start();
                } catch (final Exception ex) {
                    throw new IllegalArgumentException("Error: " + ex.getMessage());
                }
            }
        } else {
            throw new IllegalArgumentException("In order to do the alignment It is necessary to have a target," +
                    " please pick a target image.");
        }
    }

    /**
     * Align images with the automatic algorithm
     * @param pointManager
     * @param algorithm
     * @param type
     * @param pointDetector
     * @param factor
     * @param scalingFactor
     * @throws IllegalArgumentException
     */
    public void alignImages(final PointManager pointManager, final AlignmentAlgorithm algorithm,
                            final AlignmentEnum type,
                            final PointDetector pointDetector,
                            final double factor,
                            final int scalingFactor) throws IllegalArgumentException{
        if(Objects.nonNull(pointManager) &&
                Objects.nonNull(algorithm) &&
                Objects.nonNull(type) &&
                Objects.nonNull(pointDetector) &&
                factor >= 0 &&
                scalingFactor >= 1) {

            this.pointDetector = (pointDetector);
            this.pointDetector.setFactor(factor);
            this.pointDetector.setScalingFactor(scalingFactor);

            this.alignImages(pointManager, algorithm, type);
        }else{
            throw new IllegalArgumentException("One of the argument for the alignment are not correct.\n" +
                    " Please, take a look to: Alignment Algorithm, the factor must be greater than 0 and the scaling factor\n" +
                    "must be at least 1.");
        }
    }


    private void manual(){
        assert this.targetImage.getProcessor() != null;

        final ImagePoints target =  TargetImagePreprocessing.manualProcess(this.targetImage, this.imagesToAlign, this.algorithm, this.targetImage.getProcessor());
        this.alignedImages.add(new AlignedImage(target.getImagePlus().getProcessor(), target.getName()));
        IJ.log("[MANUAL] Start alignment.");
        this.imagesToAlign.forEach(img ->
                this.alignedImages.add(this.algorithm.align(target, img, img.getProcessor()))
        );
        IJ.log("[MANUAL] End alignment.");
        this.imagesToAlign.clear();
        this.targetImage = null;
    }

    private void auto() {
        final Map<ImagePoints, ImagePoints> images = new HashMap<>();
        this.total += this.imagesToAlign.size();
        IJ.log("[AUTOMATIC] Scaling Factor: " + this.pointDetector.getScalingFactor());
        this.targetImage.clearPoints();
        boolean ransac = true;
        for(final ImagePoints image : this.imagesToAlign){
            final ImagePoints target = new ImagePoints(this.targetImage.getPath(),
                    this.targetImage.toImprove(),
                    this.targetImage.getProcessor());
            image.clearPoints();
            IJ.log("[AUTOMATIC] Start Detection");
            this.pointDetector.detectPoint(target, image);
            this.status+=1;
            IJ.log("[AUTOMATIC] End Detection");
            IJ.log("[AUTOMATIC] Number of points T: " + target.numberOfPoints());
            IJ.log("[AUTOMATIC] Number of points I: " + image.numberOfPoints());
            if(target.numberOfPoints() >= this.algorithm.getLowerBound()){
                IJ.log("[AUTOMATIC] Inside ");
                images.put(image, target);
                this.total+=1;//this is for the warp;
                if(target.numberOfPoints() < 3){
                    IJ.log("[AUTOMATIC] No RANSAC");
                    ransac = false;
                }
            }
        }
        this.pointDetector.clearCache();
        this.imagesToAlign.clear();
        System.gc();
        IJ.log("[AUTOMATIC] Starting preprocess");
        if(images.size() == 0){
            throw new IllegalArgumentException("The detection has failed,\n" +
                    " please consider to expand the memory (by going to Edit > Options > Memory & Threads) and also increase the Threshold Factor.");
        }else {
            //if we find at least 3 points in every image we can use ransac otherwise first available
            if(ransac){
                this.algorithm.setPointOverload(PointOverloadEnum.RANSAC);
            }else{
                this.algorithm.setPointOverload(PointOverloadEnum.FIRST_AVAILABLE);
            }
            this.alignedImages.add(new AlignedImage(TargetImagePreprocessing.automaticProcess(this.targetImage.getProcessor(),
                    images,
                    this.algorithm), this.targetImage.getName()));
            IJ.log("[AUTOMATIC] End preprocess");

            IJ.log("[AUTOMATIC] Start aligning the images.");
            this.targetImage = null;
            images.forEach((key, value) -> {
                value.getMatImage().release();
                IJ.log("[AUTOMATIC] Target Size: " + value.getMatSize());
                this.alignedImages.add(this.algorithm.align(value, key, key.getProcessor()));
                key.getMatImage().release();
                value.clearPoints();
                key.clearPoints();
                key = null;
                value = null;
                System.gc();
            });
            images.clear();
            //this.alignedImages.forEach(i -> i.getAlignedImage().show());
            IJ.log("[AUTOMATIC] The alignment is done.");
            //this.alignedImages.clear();
        }
    }

    public synchronized int getStatus(){
        return (this.status/this.total) * 100;
    }


    /**
     * Inside this method we call the 'align' operation, It is strictly necessary that the 'align' method is present,
     * otherwise It will throw a 'NoSuchMethodException'. Each image is stored inside the 'alignedImages' collection.
     * In order to perform the alignment It is necessary that the targetImage is present.
     */
    @Override
    public void run(){
        try {
            if(Objects.nonNull(this.targetImage)) {
                this.status = 0;
                if(type == AlignmentEnum.MANUAL){
                    manual();
                }else if(type == AlignmentEnum.AUTOMATIC){
                    auto();
                }
            }
            this.thread = new Thread(this);
        } catch (final Exception ex) {
            this.thread = new Thread(this);
            throw ex;
        }
    }

    public void clearList(){
        this.alignedImages.clear();
    }


    /**
     * With this method we return all the images aligned. If the thread is running an empty list is returned otherwise
     * this method returns all the images.
     * @return all the images aligned.
     */
    public List<AlignedImage> alignedImages(){
        return this.isAlive() ? Collections.emptyList() : new LinkedList<>(this.alignedImages);
    }

    /**
     *
     * @return
     */
    public boolean isAlive(){
        return this.thread.isAlive();
    }

}
