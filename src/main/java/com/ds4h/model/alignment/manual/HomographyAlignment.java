package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_ANYCOLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class HomographyAlignment extends AlignmentAlgorithm {
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

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
        //Check if the list source is set
        try {
            final Mat matReference = imread(source.getPath(), IMREAD_ANYCOLOR);
            final Point[] pointReference = source.getCorners();
            // Compute the Homography alignment
            final Mat matDest = imread(target.getPath(), IMREAD_ANYCOLOR);
            final Mat h = Imgproc.getAffineTransform(new MatOfPoint2f(pointReference), new MatOfPoint2f(target.getCorners()));
            final Mat warpedMat = new Mat();
            Imgproc.warpAffine(matDest, warpedMat, h, matReference.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);
            // Try to convert the image, if it is present add it in to the list.
            return this.convertToImage(target.getFile(), warpedMat);
        }catch (Exception ex){
            IJ.showMessage(ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<ImagePlus> alignImages(final CornerManager cornerManager){
        List<ImagePlus> images = new LinkedList<>();
        if(Objects.nonNull(cornerManager) && cornerManager.getSourceImage().isPresent()) {
            final ImageCorners source = cornerManager.getSourceImage().get();
            cornerManager.getCornerImagesImages().forEach(image ->
                    this.align(source, image).ifPresent(images::add)
            );
        }
        return images;
    }
}
