package com.ds4h.model.alignment;

import bunwarpj.bUnwarpJ_;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.alignment.preprocessImage.TargetImagePreprocessing;
import com.ds4h.model.deformation.BunwarpjDeformation;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.saveProject.SaveImages;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.VirtualStack;
import ij.measure.Calibration;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import org.opencv.core.Mat;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;

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

    public void alignImages(final PointManager pointManager, final AlignmentAlgorithm algorithm,
                            final AlignmentEnum type,
                            final PointDetector pointDetector,
                            final double factor) throws IllegalArgumentException{
        this.pointDetector = (pointDetector);
        this.pointDetector.setFactor(factor);
        this.alignImages(pointManager, algorithm, type);
    }

    private void manual(){
        assert this.targetImage.getProcessor() != null;

        final ImagePoints target =  TargetImagePreprocessing.manualProcess(this.targetImage, this.imagesToAlign, this.algorithm, this.targetImage.getProcessor());
        this.alignedImages.add(new AlignedImage(target.getImagePlus()));
        IJ.log("[MANUAL] Start alignment.");
        this.imagesToAlign.forEach(img ->
                this.algorithm.align(target, img, img.getProcessor())
                        .ifPresent(this.alignedImages::add));
        IJ.log("[MANUAL] End alignment.");
        this.imagesToAlign.clear();
        this.targetImage = null;
        //this.alignedImages.forEach(i -> i.getAlignedImage().show());
        //this.alignedImages.clear();
    }

    private void auto(){
        final Map<ImagePoints, ImagePoints> images = new HashMap<>();
        IJ.log("[AUTOMATIC] Target LUT: " + this.targetImage.getProcessor().getLut());
        this.imagesToAlign.forEach(img->{
            final ImagePoints t = new ImagePoints(this.targetImage.getPath());
            IJ.log("[AUTOMATIC] Start Detection");
            this.pointDetector.detectPoint(t, img);
            IJ.log("[AUTOMATIC] End Detection");
            if(t.numberOfPoints() >= this.algorithm.getLowerBound()){
                images.put(img, t);
            }
        });
        System.gc();
        IJ.log("[AUTOMATIC] Starting preprocess");
        IJ.log("[AUTOMATIC] End preprocess");
        System.gc();
        this.alignedImages.add(new AlignedImage(TargetImagePreprocessing.automaticProcess(this.targetImage.getProcessor(),
                images,
                this.algorithm)));
        IJ.log("[AUTOMATIC] Start aligning the images.");
        this.targetImage = null;
        this.imagesToAlign.clear();
        images.forEach((key, value) -> {
            value.getMatImage().release();
            IJ.log("[AUTOMATIC] Target Size: " + value.getMatSize());
            IJ.log("[AUTOMATIC] LUT: " + key.getProcessor().getLut().getMapSize());
            this.algorithm.align(value, key, key.getProcessor()).ifPresent(this.alignedImages::add);
            System.gc();
        });
        images.clear();
        //this.alignedImages.forEach(i -> i.getAlignedImage().show());
        IJ.log("[AUTOMATIC] The alignment is done.");
        //this.alignedImages.clear();
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
                if(type == AlignmentEnum.MANUAL){
                    manual();
                }else if(type == AlignmentEnum.AUTOMATIC){
                    auto();
                }
            }
            this.thread = new Thread(this);
        } catch (final Exception e) {
            this.thread = new Thread(this);
            throw new RuntimeException(e);
        }
    }


    /**
     * With this method we return all the images aligned. If the thread is running an empty list is returned otherwise
     * this method returns all the images.
     * @return all the images aligned.
     */
    public List<AlignedImage> alignedImages(){
        return this.isAlive() ? Collections.emptyList() : new LinkedList<>(this.alignedImages);
    }

    public boolean isAlive(){
        return this.thread.isAlive();
    }

}
