package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.alignmentAlgorithm.TranslationalAlignment;
import com.ds4h.model.alignment.automatic.pointDetector.surfDetector.SURFDetector;
import com.ds4h.model.alignment.preprocessImage.TargetImagePreprocessing;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.Pair;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class is used for the alignment algorithms. Inside this class we can found all the methods to perform the alignment.
 * Every child class must implement the 'align' method.
 */
public class ManualAlgorithm implements Runnable{
    private ImagePoints targetImage;
    private final List<ImagePoints> imagesToAlign;
    private final List<AlignedImage> alignedImages;
    private Thread thread;

    public ManualAlgorithm(){
        this.thread = new Thread(this);
        this.imagesToAlign = new LinkedList<>();
        this.alignedImages = new CopyOnWriteArrayList<>();
    }

    /**
     * This method must be overridden from all the children classes. Inside this method we have the
     * real implementation of the alignment algorithm.
     *
     * @param targetImage the targetImage image for the alignment, this Object will be used in order to align the imagePoints.
     * @param imagePoints the image to align base to the targetImage object
     * @param targetSize
     * @return the Optional aligned containing the final aligned image
     * @throws NoSuchMethodException in case this method is called from a child class without having the implementation of it
     */
    public Optional<AlignedImage> align(final MatOfPoint2f targetImage, final ImagePoints imagePoints, Size targetSize) {
        throw new NotImplementedException();
    }


    /**
     * When this method is called we start the real process of image alignment. If the thread is not alive and the
     * target image is present we can start the operation. Take in mind that the alignment is done inside a separate Thread,
     * so if you need the images you have to wait until the alignment is not done.
     * @param pointManager  container where all the images are stored with their points
     * @throws IllegalArgumentException If the targetImage is not present or if the cornerManager is null, an exception
     * is thrown.
     */
    public void alignImages(final PointManager pointManager) throws IllegalArgumentException{
        if(Objects.nonNull(pointManager) && pointManager.getSourceImage().isPresent()) {
            if(!this.isAlive()) {
                this.targetImage = pointManager.getSourceImage().get();
                this.alignedImages.clear();
                this.imagesToAlign.clear();
                try {
                    this.imagesToAlign.addAll(pointManager.getImagesToAlign());
                    this.thread.start();
                } catch (final Exception ex) {
                    throw new IllegalArgumentException("Error: " + ex.getMessage());
                }
            }
        }else{
            throw new IllegalArgumentException("In order to do the alignment It is necessary to have a target," +
                    " please pick a target image.");
        }
    }

    private void manual(){
        final TranslationalAlignment translationalAlignment = new TranslationalAlignment();
        final ImagePoints target =  TargetImagePreprocessing.manualProcess(this.targetImage, this.imagesToAlign, translationalAlignment);



        this.alignedImages.add(new AlignedImage(target.getMatImage(), target.getImage()));
        this.imagesToAlign.parallelStream()
                .forEach(img -> translationalAlignment.align(target, img)
                        .ifPresent(this.alignedImages::add));

    }
    private void auto(){
        final TranslationalAlignment translationalAlignment = new TranslationalAlignment();
        SURFDetector s = new SURFDetector();
        Map<ImagePoints,ImagePoints> images = new HashMap<>();
        this.imagesToAlign.forEach(img->{
            ImagePoints t = new ImagePoints(this.targetImage.getMatImage(), this.targetImage.getName());
            ImagePoints i = new ImagePoints(img.getMatImage(), img.getName());
            s.detectPoint(t, i);
            images.put(i,t);
        });
        images.entrySet().stream().forEach(p->System.out.println("PRIMA: " + p.getValue().getListPoints().get(0)));

        final ImagePoints target =  TargetImagePreprocessing.automaticProcess(images, translationalAlignment);
        images.entrySet().stream().forEach(p->System.out.println("DOPO: " + p.getValue().getListPoints().get(0)));

        this.alignedImages.add(new AlignedImage(target.getMatImage(), target.getImage()));
        images.entrySet().stream().forEach(e->{
            translationalAlignment.align(e.getValue(),e.getKey()).ifPresent(this.alignedImages::add);
        });


        /*images.entrySet().stream().forEach(e->{
            translationalAlignment.align(e.getValue(),e.getKey()).ifPresent(this.alignedImages::add);
        });

         */

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

                //manual();
                auto();


                /*
                images.entrySet().stream().forEach(e->{
                    translationalAlignment.align(e.getValue(),e.getKey()).ifPresent(this.alignedImages::add);
                });
                */







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
