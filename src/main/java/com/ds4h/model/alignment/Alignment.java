package com.ds4h.model.alignment;

import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.alignment.alignmentAlgorithm.*;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.alignment.preprocessImage.TargetImagePreprocessing;
import com.ds4h.model.pointManager.ImageManager;
import com.ds4h.model.image.imagePoints.ImagePoints;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import org.opencv.core.Core;
import org.opencv.core.Mat;

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
    private double status = 0;
    private double total = 2; //Preprocess and Target image

    /**
     * Constructor for the Alignment object.
     */
    public Alignment(){
        this.thread = new Thread(this);
        this.imagesToAlign = new LinkedList<>();
        this.alignedImages = new CopyOnWriteArrayList<>();
    }

    /**
     * When this method is called we start the real process of image alignment. If the thread is not alive and the
     * target image is present we can start the operation. Take in mind that the alignment is done inside a separate Thread,
     * so if you need the images you have to wait until the alignment is not done.
     * @param imageManager  container where all the images are stored with their points
     * @throws IllegalArgumentException If the targetImage is not present or if the cornerManager is null, an exception
     * is thrown.
     */
    public void alignImages(final ImageManager imageManager, final AlignmentAlgorithm algorithm, final AlignmentEnum type) throws RuntimeException {
        if (Objects.nonNull(imageManager) && imageManager.getTargetImage().isPresent()) {
            if (!this.isAlive()) {
                this.targetImage = imageManager.getTargetImage().get();
                this.alignedImages.clear();
                this.imagesToAlign.clear();
                this.type = type;
                this.algorithm = algorithm;
                try {
                    this.imagesToAlign.addAll(imageManager.getImagesToAlign());
                    this.thread.start();
                } catch (Exception ex) {
                    throw new RuntimeException("Error: The alignment requires more memory than is available.");
                }
            }
        } else {
            throw new IllegalArgumentException("In order to do the alignment It is necessary to have a target," +
                    " please pick a target image.");
        }
    }

    /**
     * Align the input images with the chosen PointDetector and Alignment algorithm.
     * @param imageManager where all the images to align are stored.
     * @param algorithm the algorithm to use for the alignment.
     * @param type the "AlignmentType". It can be "Automatic" or "Manual".
     * @param pointDetector which point detector should be use for the detection.
     * @param factor threshold factor for the point detector.
     * @param scalingFactor scaling factor for the point detector.
     * @throws IllegalArgumentException if one of the input parameters is not correct.
     * @throws RuntimeException if during the alignment an error occur.
     */
    public void alignImages(final ImageManager imageManager, final AlignmentAlgorithm algorithm,
                            final AlignmentEnum type,
                            final PointDetector pointDetector,
                            final double factor,
                            final int scalingFactor) throws IllegalArgumentException, RuntimeException{
        if(Objects.nonNull(imageManager) &&
                Objects.nonNull(algorithm) &&
                Objects.nonNull(type) &&
                Objects.nonNull(pointDetector) &&
                factor >= 0 &&
                scalingFactor >= 1) {
            this.pointDetector = (pointDetector);
            this.pointDetector.setFactor(factor);
            this.pointDetector.setScalingFactor(scalingFactor);
            this.alignImages(imageManager, algorithm, type);
        }else{
            throw new IllegalArgumentException("One of the argument for the alignment are not correct.\n" +
                    " Please, take a look to: Alignment Algorithm, the factor must be greater than 0 and the scaling factor\n" +
                    "must be at least 1.");
        }
    }

    /**
     * Affine and Rigid accept more points than their limits
     * Perspective accept the exact number of points.
     * @throws RuntimeException a
     */
    private void manual() throws RuntimeException{

        assert this.targetImage.getProcessor() != null;
        if(this.imagesToAlign.stream()
                .map(ImagePoints::numberOfPoints)
                .anyMatch(n -> (n >= this.algorithm.getLowerBound()))) {

            if (!this.isOnly8Bit())
                new ImageConverter(this.targetImage).convertToRGB();
            else
                new ImageConverter(this.targetImage).convertToGray8();

            final ImagePoints target = TargetImagePreprocessing.manualProcess(this.targetImage,
                    this.imagesToAlign,
                    this.algorithm,
                    this.targetImage.getProcessor());
            this.alignedImages.add(new AlignedImage(target.getImagePlus().getProcessor(), target.getName()));
            IJ.log("[MANUAL] Start alignment.");
            this.imagesToAlign.forEach(img ->
                    this.alignedImages.add(this.algorithm.align(target, img, img.getProcessor()))
            );
            IJ.log("[MANUAL] End alignment.");
            this.imagesToAlign.clear();
            this.targetImage = null;
            System.gc();
        }
    }

    /**
     * Affine and Rigid accept more points than their limits
     * Perspective accept the exact number of points.
     * @throws RuntimeException if something went wrong inside the Preprocess or during the Point detection.
     */
    private void auto() throws RuntimeException{
        int sameImages = 0;
        final Map<ImagePoints, ImagePoints> images = new HashMap<>();
        this.total += this.imagesToAlign.size();
        this.targetImage.clearPoints();
        boolean ransac = true;
        for(final ImagePoints image : this.imagesToAlign){
            if (!this.isOnly8Bit())
                new ImageConverter(this.targetImage).convertToRGB();
            else
                new ImageConverter(this.targetImage).convertToGray8();

            final ImagePoints target = new ImagePoints(this.targetImage.getPath(),
                    this.targetImage.toImprove(),
                    this.targetImage.getProcessor());
            image.clearPoints();
            IJ.log("[AUTOMATIC] Start Detection");
            this.pointDetector.detectPoint(target, image);
            this.status+=1; //detected points
            IJ.log("[AUTOMATIC] End Detection");
            IJ.log("[AUTOMATIC] Number of points T: " + target.numberOfPoints());
            IJ.log("[AUTOMATIC] Number of points I: " + image.numberOfPoints());

            if(target.numberOfPoints() >= this.algorithm.getLowerBound()){
                IJ.log("[AUTOMATIC] Inside ");
                images.put(image, target);
                this.total += 1;//this is for the warp;
                if (target.numberOfPoints() < 3) {
                    IJ.log("[AUTOMATIC] No RANSAC");
                    ransac = false;
                }
            }else if(target.numberOfPoints() == this.algorithm.getLowerBound()){
                IJ.log("[AUTOMATIC] Inside ");
                images.put(image, target);
                this.total += 1;//this is for the warp;

            }else{
                if(target.getMatSize().equals(image.getMatSize())) {
                    final Mat diff = new Mat(image.getMatSize(),image.getType());
                    Core.bitwise_xor(target.getGrayScaleMat(), image.getGrayScaleMat(), diff);
                    // if all pixels are 0, it means the two images are the same.
                    if (Core.countNonZero(diff) == 0) {
                        sameImages++;
                    }
                }
            }
        }
        this.pointDetector.clearCache();
        this.imagesToAlign.clear();
        if(images.isEmpty() && sameImages == 0){
            this.status = total;
            throw new RuntimeException("No points detected, please Increase the \"Threshold Factor\", " +
                    "decrease the \"Scaling Factor\".");
        }else {

            IJ.log("[AUTOMATIC] Starting preprocess");
            if(sameImages == 0 && !images.isEmpty()){
                //if we find at least 3 points in every image we can use ransac otherwise first available
                if(ransac){
                    this.algorithm.setPointOverload(PointOverloadEnum.RANSAC);
                }else{
                    this.algorithm.setPointOverload(PointOverloadEnum.FIRST_AVAILABLE);
                }
                final AlignedImage target = new AlignedImage(TargetImagePreprocessing.automaticProcess(this.targetImage.getProcessor(),
                        images,
                        this.algorithm), this.targetImage.getName());
                this.alignedImages.addAll(Collections.nCopies(sameImages, target));
                this.alignedImages.add(target);
            }else {
                final AlignedImage target = new AlignedImage(this.targetImage.getProcessor(), this.targetImage.getName());
                this.alignedImages.add(target);
                this.alignedImages.addAll(Collections.nCopies(sameImages, target));
            }
            this.status+=1; //pre process
            IJ.log("[AUTOMATIC] End preprocess");
            IJ.log("[AUTOMATIC] Start aligning the images.");
            this.targetImage = null;
            images.forEach((key, value) -> {
                value.getMatImage().release();
                IJ.log("[AUTOMATIC] Target Size: " + value.getMatSize());
                this.alignedImages.add(this.algorithm.align(value, key, key.getProcessor()));
                this.status+=1; //warp image
                IJ.log("[AUTOMATIC] Status: " + this.getStatus());
                key.getMatImage().release();
                value.clearPoints();
                key.clearPoints();
            });
            this.status+=1;
            images.clear();
            System.gc();
            IJ.log("[AUTOMATIC] The alignment is done.");
        }
    }

    /**
     * Returns the alignment status.
     * @return the alignment status.
     */
    public synchronized int getStatus(){
        return (int) (this.status*100/this.total);
    }


    /**
     * Inside this method we call the 'align' operation, It is strictly necessary that the 'align' method is present,
     * otherwise It will throw a 'NoSuchMethodException'. Each image is stored inside the 'alignedImages' collection.
     * In order to perform the alignment It is necessary that the targetImage is present.
     *
     * @throws RuntimeException if there is a OutOfMemory exception or an error during the alignment.
     */
    @Override
    public void run() throws RuntimeException{
        try{
            try {
                if(Objects.nonNull(this.targetImage)) {
                    this.status = 0;
                    this.total = 2;
                    if(type == AlignmentEnum.MANUAL){
                        manual();
                    }else if(type == AlignmentEnum.AUTOMATIC){
                        auto();
                    }
                }
                this.thread = new Thread(this);
            } catch (final RuntimeException ex) {
                this.thread = new Thread(this);
                this.clearList();
                IJ.log(ex.getMessage());
                throw ex;
            }
        }catch (OutOfMemoryError e){
            this.thread = new Thread(this);
            this.clearList();
            throw new RuntimeException("Error: The alignment requires more memory than is available.");
        }
    }

    /**
     * Release all the images and remove it from the heap.
     */
    public void clearList(){
        IJ.log("[ALIGNMENT] Clear List");
        this.alignedImages.forEach(AlignedImage::releaseImage);
        this.alignedImages.clear();
        System.gc();
    }


    /**
     * With this method we return all the images aligned. If the thread is running an empty list is returned otherwise
     * this method returns all the images.
     * @return all the images aligned.
     */
    public List<AlignedImage> alignedImages(){
        return this.isAlive() ? Collections.emptyList() : this.alignedImages;
    }

    /**
     * Returns the thread status.
     * @return the thread status.
     */
    public boolean isAlive(){
        return this.thread.isAlive();
    }

    /**
     * This method checks if there are almost one RGB image in the images to align.
     * @return true if there is almost one RGB image, false otherwise
     */
    private boolean isOnly8Bit() {
        boolean isOnly8Bit = true;

        for (final ImagePoints img : this.imagesToAlign) {
            if (img.getImagePlus().getType() == ImagePlus.COLOR_RGB) {
                isOnly8Bit = false;
                break;
            }
        }

        return isOnly8Bit;
    }
}
