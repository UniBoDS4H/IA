package com.ds4h.model.alignment.manual;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.NameBuilder;
import ij.ImagePlus;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_ANYCOLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;

// TODO : Allineo tutte le immagini in base a quella di riferimento.
// TODO : Salvo ogni Output in una lista di immaginiPlus e la ritorno
// TODO : Fare in modo di avere il percorso dell'immagine
public class HomographyAlignment {
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private HomographyAlignment(){}

    /**
     * Manual alignment using the Homography alignment
     * @param source : the source image used as reference
     * @param  target : the target to align
     * @return : the list of all the images aligned to the source
     */
    private static Optional<ImagePlus> align(final ImageCorners source, final ImageCorners target){
        //Check if the list source is set
            final Mat matReference = imread(source.getPath(), IMREAD_ANYCOLOR);
            final Point[] pointReference = source.getCorners();
            // Compute the Homography alignment
            final Mat matDest = imread(target.getPath(), IMREAD_ANYCOLOR);
            final Mat h = Imgproc.getAffineTransform( new MatOfPoint2f(pointReference), new MatOfPoint2f(target.getCorners()));
            final Mat warpedMat = new Mat();
            Imgproc.warpAffine(matDest, warpedMat, h, matReference.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);
            // Try to convert the image, if it is present add it in to the list.
            return HomographyAlignment.convertToImage(target.getFile(), warpedMat);
    }

    public static List<ImagePlus> alignImages(final CornerManager cornerManager){
        List<ImagePlus> images = new LinkedList<>();
        if(cornerManager.getSourceImage().isPresent()) {
            final ImageCorners source = cornerManager.getSourceImage().get();
            cornerManager.getCornerImagesImages().forEach(image ->
                    HomographyAlignment.align(source, image).ifPresent(images::add)
            );
        }
        return images;
    }

    /**
     * Convert the new matrix in to an image
     * @param file : this will be used in order to get the name and store used it for the creation of the new file.
     * @param matrix : the image aligned matrix
     * @return : the new image created by the Matrix.
     */
    private static Optional<ImagePlus> convertToImage(final File file, final Mat matrix){
        return ImagingConversion.fromMatToImagePlus(matrix, file.getName(), NameBuilder.DOT_SEPARATOR);
    }
}
