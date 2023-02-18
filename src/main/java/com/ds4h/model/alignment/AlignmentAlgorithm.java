package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.NameBuilder;
import com.ds4h.model.util.Pair;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.*;

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
        return ImagingConversion.fromMatToImagePlus(matrix, file.getName());
    }

    protected Optional<AlignedImage> align(final ImageCorners source, final ImageCorners target) throws NoSuchMethodException {
        throw new NoSuchMethodException("Method not implemented");
    }

    protected Mat toGrayscale(final Mat mat) {
        Mat gray = new Mat();
        if (mat.channels() == RGB) {
            // Convert the BGR image to a single channel grayscale image
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            // If the image is already single channel, just return it
            gray = mat;
        }
        return gray;
    }
    /**
     * Align the images stored inside the cornerManager. All the images will be aligned to the source image
     * @param cornerManager : container where all the images are stored
     * @return the List of all the images aligned to the source
     */
    @Override
    public List<AlignedImage> alignImages(final CornerManager cornerManager){
        List<AlignedImage> images = new LinkedList<>();
        if(Objects.nonNull(cornerManager) && cornerManager.getSourceImage().isPresent()) {
            final ImageCorners source = cornerManager.getSourceImage().get();
            images.add(new AlignedImage(source.getMatImage(), source.getImage()));
            try {
                for(ImageCorners image : cornerManager.getImagesToAlign()){
                    final Optional<AlignedImage> output = this.align(source, image);
                    output.ifPresent(images::add);
                }
            }catch (Exception e){
                IJ.showMessage(e.getMessage());
            }
        }
        return images;
    }

}
