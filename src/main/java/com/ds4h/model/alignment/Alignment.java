package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.alignment.preprocessImage.TargetImagePreprocessing;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.saveProject.SaveImages;
import ij.IJ;
import ij.ImagePlus;
import ij.measure.Calibration;
import ij.process.ImageProcessor;
import org.opencv.core.Mat;

import java.util.*;
import java.util.List;
import java.util.concurrent.*;

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
        final ImagePoints target =  TargetImagePreprocessing.manualProcess(this.targetImage, this.imagesToAlign, this.algorithm);
        this.alignedImages.add(new AlignedImage(target.getMatImage(), target.getImagePlus()));
        this.imagesToAlign.parallelStream()
                .forEach(img -> this.algorithm.align(target, img)
                        .ifPresent(this.alignedImages::add));

    }

    private ImagePoints scaleDown (final ImagePoints originalImage){
        Calibration originalCalibration = originalImage.getCalibration();
        Calibration downsampledCalibration = new Calibration();
        downsampledCalibration.pixelWidth = originalCalibration.pixelWidth * Math.sqrt(4);
        downsampledCalibration.pixelHeight = originalCalibration.pixelHeight * Math.sqrt(4);
        downsampledCalibration.setUnit(originalCalibration.getUnit());
        ImageProcessor downsampledProcessor = originalImage.getProcessor().resize(originalImage.getWidth() / 4, originalImage.getHeight() / 4);
        ImagePlus downsampledImage = new ImagePlus(originalImage.getTitle(), downsampledProcessor);
        downsampledImage.show();
        final String path = SaveImages.saveTMPImage(downsampledImage) + "/" +  downsampledImage.getTitle();
        return new ImagePoints(path);
    }

    private ImagePlus scaleUp(final ImagePlus originalImage, final ImagePlus downsampledImage){
        Calibration originalCalibration = originalImage.getCalibration();

        Calibration downsampledCalibration = downsampledImage.getCalibration();

        Calibration restoredCalibration = new Calibration();

        restoredCalibration.pixelWidth = originalCalibration.pixelWidth;
        restoredCalibration.pixelHeight = originalCalibration.pixelHeight;
        restoredCalibration.setUnit(originalCalibration.getUnit());

        ImageProcessor restoredProcessor = downsampledImage.getProcessor().resize(originalImage.getWidth(), originalImage.getHeight());
        ImagePlus restoredImage = new ImagePlus(originalImage.getTitle(), restoredProcessor);
        restoredImage.show();
        final String path = SaveImages.saveTMPImage(restoredImage) + "/" +  downsampledImage.getTitle();
        return new ImagePoints(path);
    }
    private void auto(){
        final Map<ImagePoints, ImagePoints> images = new HashMap<>();
        final double factor = this.pointDetector.getFactor();
        final List<Pair<String, ImagePoints>> origianlImages = new ArrayList<>(this.imagesToAlign.size());

        final ImagePoints a = this.scaleDown(this.targetImage);
        origianlImages.add(new Pair<>(this.targetImage.getPath(), a));
        this.targetImage = a;
        this.imagesToAlign.forEach(img->{
            final ImagePoints t = new ImagePoints(this.targetImage.getPath());
            final ImagePoints i = this.scaleDown(img);
            origianlImages.add(new Pair<>(img.getPath(), i));
            System.out.println("UEILA");
            this.pointDetector.detectPoint(t, i);
            System.out.println("AFTER UEILA");
            if(t.numberOfPoints() >= this.algorithm.getLowerBound()){
                images.put(i,t);
            }
        });

        //final ImagePoints target =  this.scaleUp(new ImagePlus(origianlImages.get(0).getFirst()),
         //       TargetImagePreprocessing.automaticProcess(images, this.algorithm).getOriginalImage());
        //this.alignedImages.add(new AlignedImage(target.getOriginalMatImage(), target.getOriginalImage()));

        origianlImages.forEach(p -> {
            this.alignedImages.add(new AlignedImage(this.scaleUp(new ImagePoints(p.getFirst()), p.getSecond())));
        });
        //this.alignedImages.add(new AlignedImage(this.scaleUp(target.getImagePlus()));
        System.gc();
        images.forEach((key, value) -> this.algorithm.align(value, key).ifPresent(this.alignedImages::add));
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
