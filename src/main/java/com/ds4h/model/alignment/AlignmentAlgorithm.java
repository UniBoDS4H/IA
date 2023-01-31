package com.ds4h.model.alignment;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.NameBuilder;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AlignmentAlgorithm implements AlignmentAlgorithmInterface{
    private final static int RGB = 3;
    protected AlignmentAlgorithm(){

    }
    /**
     * Convert the new matrix in to an image
     * @param file : this will be used in order to get the name and store used it for the creation of the new file.
     * @param matrix : the image aligned matrix
     * @return : the new image created by the Matrix.
     */
    protected Optional<ImagePlus> convertToImage(final File file, final Mat matrix){
        return ImagingConversion.fromMatToImagePlus(matrix, file.getName(), NameBuilder.DOT_SEPARATOR);
    }

    protected Optional<ImagePlus> align(final ImageCorners source, final ImageCorners target){
        return Optional.empty();
    }

    protected Mat toGrayscale(final Mat mat) {
        Mat gray = new Mat();
        try{
            if (mat.channels() == RGB) {
                // Convert the BGR image to a single channel grayscale image
                Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
            } else {
                // If the image is already single channel, just return it
                gray = mat;
            }
            return gray;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ImagePlus> alignImages(final CornerManager cornerManager){
        return Collections.emptyList();
    }

}
