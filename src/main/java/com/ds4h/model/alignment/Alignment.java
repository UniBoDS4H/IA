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
import ij.measure.Calibration;
import ij.process.ImageProcessor;
import org.opencv.core.Mat;

import java.awt.*;
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
        final Calibration originalCalibration = originalImage.getCalibration();
        final Calibration downsampledCalibration = new Calibration();
        IJ.log("Original Image " + originalImage.getTitle() + " "+ originalImage.getHeight() + " " + originalImage.getWidth());
        downsampledCalibration.pixelWidth = originalCalibration.pixelWidth * Math.sqrt(4);
        downsampledCalibration.pixelHeight = originalCalibration.pixelHeight * Math.sqrt(4);
        downsampledCalibration.setUnit(originalCalibration.getUnit());
        final ImageProcessor downsampledProcessor = originalImage.getProcessor().resize(originalImage.getWidth() / 8,
                originalImage.getHeight() / 8);
        final ImagePlus downsampledImage = new ImagePlus(originalImage.getTitle(), downsampledProcessor);
        IJ.log("Downscaled Image " + downsampledImage.getTitle() + " "+ downsampledImage.getHeight() + " " + downsampledImage.getWidth());
        final String path = SaveImages.saveTMPImage(downsampledImage) + "/" +  downsampledImage.getTitle();
        return new ImagePoints(path);
    }

    private ImagePlus scaleUp(final ImagePlus originalImage, final ImagePlus downsampledImage){
        IJ.log("Original Image:" + originalImage.getWidth() + " " + originalImage.getHeight());
        final ImageProcessor restoredProcessor = downsampledImage.getProcessor();
        final ImageProcessor originalProcessor = originalImage.getProcessor();

        restoredProcessor.setInterpolationMethod(ImageProcessor.BICUBIC);
        restoredProcessor.setMinAndMax(originalProcessor.getMin(), originalProcessor.getMax());
        restoredProcessor.setRoi(new Rectangle(0, 0, originalImage.getWidth(), originalImage.getHeight()));
        final ImageProcessor scaledProcessor= restoredProcessor.resize(originalImage.getWidth(), originalImage.getHeight());

        final ImagePlus restoredImage = new ImagePlus(originalImage.getTitle(), scaledProcessor);
        //restoredImage.setCalibration(originalImage.getCalibration());
        //final String path = SaveImages.saveTMPImage(restoredImage) + "/" +  downsampledImage.getTitle();
        IJ.log("Finish Scaling Up");
        return restoredImage;
    }
    private void auto(){
        final Map<ImagePoints, ImagePoints> images = new HashMap<>();
        final double factor = this.pointDetector.getFactor();
        final List<Pair<ImagePlus, ImagePlus>> origianlImages = new ArrayList<>(this.imagesToAlign.size());
        //ORIGINALE - SCALATA
        /*
        final ImagePoints scaledTarget =  this.scaleDown(this.targetImage);
        origianlImages.add(new Pair<>(new ImagePlus(this.targetImage.getPath()), scaledTarget));
        this.targetImage = new ImagePoints(scaledTarget.getPath());
        this.imagesToAlign.forEach(img->{
            final ImagePoints t = new ImagePoints(this.targetImage.getPath());
            final ImagePoints i = this.scaleDown(img);
            origianlImages.add(new Pair<>(new ImagePlus(img.getPath()), i));
            IJ.log("Start Detection");
            this.pointDetector.detectPoint(t, i);
            IJ.log("End of the Detection");
            if(t.numberOfPoints() >= this.algorithm.getLowerBound()){
                images.put(i,t);
            }
        });

        IJ.log("Start Preprocess");
        final ImagePoints target = TargetImagePreprocessing.automaticProcess(images, this.algorithm);
        IJ.log("End of the Preprocess");
        this.alignedImages.add(new AlignedImage(target.getImagePlus()));
        images.forEach((key, value) -> this.algorithm.align(value, key).ifPresent(this.alignedImages::add));
        for(final AlignedImage alignedImage : this.alignedImages){
            for(final Pair<ImagePlus, ImagePlus> a : origianlImages){
                if(alignedImage.getAlignedImage().getTitle().equals(a.getFirst().getTitle())){
                    final ImagePlus finalImage = this.scaleUp(a.getFirst(), alignedImage.getAlignedImage());
                    finalImage.show();
                }
            }
        }
         */
        this.imagesToAlign.forEach(img->{
            final ImagePoints t = new ImagePoints(this.targetImage.getPath());
            final ImagePoints i = new ImagePoints(img.getPath());
            IJ.log("Start Detection");
            this.pointDetector.detectPoint(t, i);
            IJ.log("End of the Detection");
            if(t.numberOfPoints() >= this.algorithm.getLowerBound()){
                images.put(i,t);
            }
        });
        System.gc();
        final ImagePoints target = TargetImagePreprocessing.automaticProcess(images, this.algorithm);
        this.alignedImages.add(new AlignedImage(target.getOriginalMatImage(), target.getImagePlus()));
        IJ.log("Start aligning the images.");
        images.entrySet().parallelStream().forEach(e->{
            this.algorithm.align(e.getValue(),e.getKey()).ifPresent(this.alignedImages::add);
        });
        IJ.log("The alignment is done.");
        this.alignedImages.forEach(z -> z.getAlignedImage().show());
        //this.alignedImages.stream().filter(Objects::nonNull).forEach(img -> img.getAlignedImage().show());
        //this.alignedImages.clear();
        //this.alignedImages.addAll(copy);
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
