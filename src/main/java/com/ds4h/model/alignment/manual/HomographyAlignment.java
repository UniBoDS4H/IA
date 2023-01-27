package com.ds4h.model.alignment.manual;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.CheckImage;
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
import java.util.Map;
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
     * @param cornerManager : container where are stored all the images and their points
     * @return : the list of all the images aligned to the source
     */
    public static List<ImagePlus> align(final CornerManager cornerManager){
        final List<ImagePlus> output = new LinkedList<>();
        //Check if the list source is set
        if(cornerManager.getSourceImage().isPresent()){
            final Mat matReference = imread(cornerManager.getSourceImage().get().getPath(), IMREAD_ANYCOLOR);
            final Point[] pointReference = cornerManager.getSourceImage().get().getCorners();
            for (ImageCorners image :  cornerManager.getCornerImagesImages()) {
                // Compute the Homography alignment
                final Mat matDest = imread(image.getPath(), IMREAD_ANYCOLOR);
                final Mat h = Imgproc.getAffineTransform( new MatOfPoint2f(pointReference), new MatOfPoint2f(image.getCorners()));
                final Mat warpedMat = new Mat();
                Imgproc.warpAffine(matDest, warpedMat, h, matReference.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);
                // Try to convert the image, if it is present add it in to the list.
                Optional<ImagePlus> imageWarped = HomographyAlignment.convertToImage(image.getFile(), warpedMat);
                imageWarped.ifPresent(output::add);
            }
        }
        return output;
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
