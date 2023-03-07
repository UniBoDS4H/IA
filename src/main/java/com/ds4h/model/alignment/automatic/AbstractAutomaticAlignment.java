package com.ds4h.model.alignment.automatic;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.alignment.automatic.pointDetector.surfDetector.SURFDetector;
import com.ds4h.model.alignment.manual.TranslationalAlignment;
import com.ds4h.model.alignment.preprocessImage.TargetImagePreprocessing;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.ds4h.model.util.saveProject.SaveImages;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractAutomaticAlignment implements Runnable{

    private final PointDetector pointDetector;
    private final AlignmentAlgorithm alignmentAlgorithm;
    private ImagePoints targetImage;
    private final List<ImagePoints> imagesToAlign;
    private final List<AlignedImage> alignedImages;
    private Thread alignmentThread;
    protected AbstractAutomaticAlignment(final PointDetector pointDetector, final AlignmentAlgorithm algorithm){
        this.pointDetector = pointDetector;
        this.alignmentAlgorithm = algorithm;
        this.alignmentThread = new Thread(this);
        this.targetImage = null;
        this.imagesToAlign = new LinkedList<>();
        this.alignedImages = new CopyOnWriteArrayList<>();
    }

    protected PointDetector pointDetector(){
        return this.pointDetector;
    }

    protected AlignmentAlgorithm alignmentAlgorithm(){
        return this.alignmentAlgorithm;
    }
    public abstract void detectPoint(final Mat targetMat, final ImagePoints imagePoints);
    public abstract void mergePoint(final ImagePoints targetImage, final ImagePoints imagePoints);

    public Mat getTransformationMatrix(final ImagePoints targetImage, final ImagePoints imagePoints){
        final MatOfPoint2f points1_ = new MatOfPoint2f();
        points1_.fromList(this.pointDetector().getPoints1());
        final MatOfPoint2f points2_ = new MatOfPoint2f();
        points2_.fromList(this.pointDetector().getPoints2());
        return Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, 5);
    }

    public void transform(final Mat source, final Mat destination, final Mat H){
        this.alignmentAlgorithm.transform(source, destination, H);
    }

    public Optional<AlignedImage> align(final MatOfPoint2f targetPoints, final ImagePoints imagePoints, final Size targetSize){
        final Mat imagePointMat = this.toGrayscale(Imgcodecs.imread(imagePoints.getPath(), Imgcodecs.IMREAD_ANYCOLOR));
        final Mat H = Calib3d.findHomography(imagePoints.getMatOfPoint(), targetPoints, Calib3d.RANSAC, 5);
        return this.warpMatrix(imagePointMat, H, targetSize, imagePoints.getFile());
    }

    protected Mat toGrayscale(final Mat mat) {
        Mat gray = new Mat();
        if (mat.channels() == 3) {
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            return mat;
        }
        return gray;
    }
    private Optional<AlignedImage> warpMatrix(final Mat source, final Mat H, final Size size, final File alignedFile){
        final Mat alignedImage1 = new Mat();
        Imgproc.warpPerspective(source, alignedImage1, H, size);
        final Optional<ImagePlus> finalImage = this.convertToImage(alignedFile, alignedImage1);
        return finalImage.map(imagePlus -> new AlignedImage(alignedImage1, H, imagePlus));
    }
    private Optional<ImagePlus> convertToImage(final File file, final Mat matrix){
        return ImagingConversion.fromMatToImagePlus(matrix, file.getName());
    }

    public void alignImages(final PointManager pointManager){
        if(Objects.nonNull(pointManager) && pointManager.getSourceImage().isPresent()) {
            if(!this.isAlive()) {
                this.targetImage = pointManager.getSourceImage().get();
                this.alignedImages.clear();
                this.imagesToAlign.clear();
                try {
                    this.imagesToAlign.addAll(pointManager.getImagesToAlign());
                    this.alignmentThread.start();
                } catch (final Exception ex) {
                    throw new IllegalArgumentException("Error: " + ex.getMessage());
                }
            }
        }else{
            throw new IllegalArgumentException("In order to do the alignment It is necessary to have a target," +
                    " please pick a target image.");
        }
    }

    public boolean isAlive(){
        return this.alignmentThread.isAlive();
    }

    public List<AlignedImage> alignedImages(){
        return this.alignedImages;
    }

    @Override
    public void run(){
        final TargetImagePreprocessing targetImagePreprocessing = new TargetImagePreprocessing();
        final Pair<ImagePoints, Map<ImagePoints, MatOfPoint2f>> k = targetImagePreprocessing.processImage( targetImage, this.imagesToAlign, this);
        final ImagePoints result = k.getFirst();
        this.alignedImages.add(new AlignedImage(result.getMatImage(), result.getImage()));
        System.out.println("MAT OF POINT RESULT : " + result.getMatOfPoint());
        k.getSecond().entrySet().parallelStream().peek(u -> System.out.println("BEFORE ALIGN T POINTS: " + u.getValue()))
                .forEach(img -> this.align(img.getValue(), img.getKey(), result.getMatImage().size())
                        .ifPresent(this.alignedImages::add));
    }

}
