package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_ANYCOLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class HomographyAlignment extends AlignmentAlgorithm {


    public HomographyAlignment(){
        super();
    }

    /**
     * Manual alignment using the Homography alignment
     * @param source : the source image used as reference
     * @param  target : the target to align
     * @return : the list of all the images aligned to the source
     */
    @Override
    protected   Optional<ImagePlus> align(final ImageCorners source, final ImageCorners target){

     try {

            final Mat matReference = super.toGrayscale(source.getMatImage());
            // Compute the Homography alignment
            final Mat matDest = super.toGrayscale(target.getMatImage());
            final MatOfPoint2f referencePoint = source.getMatOfPoint();
            final MatOfPoint2f targetPoint = target.getMatOfPoint();
            final Mat H = Imgproc.getAffineTransform(referencePoint, targetPoint);
            //Calib3d.findHomography(referencePoint, targetPoint, Calib3d.RANSAC, 5);
            final Mat warpedMat = new Mat();
            Imgproc.warpAffine(matDest, warpedMat, H, matReference.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);
            //Imgproc.warpPerspective(matReference, warpedMat, H, new Size(matDest.cols(), matDest.rows()));
            // Try to convert the image, if it is present add it in to the list.
             new ImagePlus("Transformed", HighGui.toBufferedImage(warpedMat)).show();
             new ImagePlus("Origin", HighGui.toBufferedImage(matReference)).show();
            return this.convertToImage(target.getFile(), warpedMat);
        }catch (Exception ex){
            IJ.showMessage(ex.getMessage());
        }
        return Optional.empty();

    }


    @Override
    public List<ImagePlus> alignImages(final CornerManager cornerManager){
        /*
        Arrays.stream(cornerManager.getSourceImage().get().getCorners()).forEach(System.out::println);
        cornerManager.getCornerImagesImages().forEach(i->{
            Arrays.stream(i.getCorners()).forEach(System.out::println);
        });
        */
        List<ImagePlus> images = new LinkedList<>();
        if(Objects.nonNull(cornerManager) && cornerManager.getSourceImage().isPresent()) {
            final ImageCorners source = cornerManager.getSourceImage().get();
            cornerManager.getImagesToAlign().forEach(image ->{

                    this.align(source, image).ifPresent(images::add);
                    }
            );
        }
        return images;
    }
}
