package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
            final MatOfPoint2f referencePoint = source.getMatOfPoint();
            final MatOfPoint2f targetPoint = target.getMatOfPoint();
            final Mat H = Imgproc.getAffineTransform(targetPoint, referencePoint);
            final Mat warpedMat = new Mat();
            Imgproc.warpAffine(target.getMatImage(), warpedMat, H, source.getMatImage().size(),Imgproc.INTER_LINEAR, 0, new Scalar(0, 0, 0));
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
            images.add(source.getImage());
            cornerManager.getImagesToAlign().forEach(image -> this.align(source, image).ifPresent(images::add));
        }
        return images;
    }
}
