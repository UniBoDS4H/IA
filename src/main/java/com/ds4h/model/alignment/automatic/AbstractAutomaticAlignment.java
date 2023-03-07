package com.ds4h.model.alignment.automatic;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.Optional;

public abstract class AbstractAutomaticAlignment {

    private final PointDetector pointDetector;
    private final AlignmentAlgorithm alignmentAlgorithm;
    protected AbstractAutomaticAlignment(final PointDetector pointDetector, final AlignmentAlgorithm algorithm){
        this.pointDetector = pointDetector;
        this.alignmentAlgorithm = algorithm;
    }

    protected PointDetector pointDetector(){
        return this.pointDetector;
    }

    protected AlignmentAlgorithm alignmentAlgorithm(){
        return this.alignmentAlgorithm;
    }

    //Detect dei punti delle due immagini.
    public abstract void detectPoint(final Mat targetMat, final ImagePoints imagePoints);
    //Aggiungo i punti nelle due immagini.
    public abstract void mergePoint(final ImagePoints targetImage, final ImagePoints imagePoints);

    public Mat getTransformationMatrix(final ImagePoints targetImage, final ImagePoints imagePoints){
        final MatOfPoint2f points1_ = new MatOfPoint2f();
        points1_.fromList(this.pointDetector().getPoints1());
        final MatOfPoint2f points2_ = new MatOfPoint2f();
        points2_.fromList(this.pointDetector().getPoints2());
        //final Mat H = new TranslationalAlignment().getTransformationMatrix(imageToAlign,targetImage);
        return Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, 5);
    }

    public void transform(final Mat source, final Mat destination, final Mat H){
        this.alignmentAlgorithm.transform(source, destination, H);
    }

    public Optional<AlignedImage> align(final MatOfPoint2f targetPoints, final ImagePoints imagePoints, final Size targetSize){
        final Mat imagePointMat = this.toGrayscale(Imgcodecs.imread(imagePoints.getPath(), Imgcodecs.IMREAD_ANYCOLOR));
        final Mat H = Calib3d.findHomography(imagePoints.getMatOfPoint(), targetPoints, Calib3d.RANSAC, 5);
        // Align the first image to the second image using the homography matrix
        return this.warpMatrix(imagePointMat, H, targetSize, imagePoints.getFile());



        //return this.alignmentAlgorithm.align(targetPoints.toList(), imagePoints, targetSize);
    }

    protected Mat toGrayscale(final Mat mat) {
        Mat gray = new Mat();
        if (mat.channels() == 3) {
            // Convert the BGR image to a single channel grayscale image
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            // If the image is already single channel, just return it
            return mat;
        }
        return gray;
    }

    /**
     * This method is used for the warping of the final Matrix, this warping is used in order to store the new aligned image.
     * @param source this is the original Matrix of the image to align
     * @param H the Homography Matrix
     * @param size the size of the TargetMatrix
     * @param alignedFile the name of the aligned file
     * @return an Optional where is stored the new Aligned Image
     */
    private Optional<AlignedImage> warpMatrix(final Mat source, final Mat H, final Size size, final File alignedFile){
        final Mat alignedImage1 = new Mat();
        // Align the first image to the second image using the homography matrix
        Imgproc.warpPerspective(source, alignedImage1, H, size);
        final Optional<ImagePlus> finalImage = this.convertToImage(alignedFile, alignedImage1);
        return finalImage.map(imagePlus -> new AlignedImage(alignedImage1, H, imagePlus));
    }
    private Optional<ImagePlus> convertToImage(final File file, final Mat matrix){
        return ImagingConversion.fromMatToImagePlus(matrix, file.getName());
    }

}
