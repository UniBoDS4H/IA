package com.ds4h.model.alignment;

import com.ds4h.model.util.CheckImage;
import com.ds4h.model.util.ImagingConverion;
import com.ds4h.model.util.NameBuilder;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
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

    public static List<ImagePlus> align(final File impReference,
                                        final Point[] pointsReference,
                                        final Map<File, Point[]> images){
        final List<ImagePlus> output = new LinkedList<>();
        final Mat matReference = imread(impReference.getPath(), IMREAD_ANYCOLOR);
        for (Map.Entry<File, Point[]> image :  images.entrySet()) {
            // TODO : check immagine
            if(CheckImage.checkImage(image.getKey())){
                final Mat matDest = imread(image.getKey().getPath(), IMREAD_ANYCOLOR);
                final Mat h = Imgproc.getAffineTransform( new MatOfPoint2f(pointsReference), new MatOfPoint2f(image.getValue()));
                final Mat warpedMat = new Mat();
                Imgproc.warpAffine(matDest, warpedMat, h, matReference.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);
                // TODO : Add the image inside the list output
                //ImagePlus imp2Warped
                /*
                ImagePlusMatConverter b = new ImagePlusMatConverter();
                ImagePlusMatConverter.toMat(new ImagePlus().get)
                ImageJ2OpenCVConverter a = new ImageJ2OpenCVConverter();
*/

            }
        }
        return null;
    }

    private Optional<ImagePlus> convertToImage(final File file, final Mat matrix){
        final Optional<ImagePlus> impOutput = ImagingConverion.fromMatToImagePlus(matrix, file.getName(), NameBuilder.DOT_SEPARATOR);
        return impOutput;
    }
}
