package com.ds4h.model.alignment;

import ij.ImageJ2OpenCVConverter;
import ij.ImagePlus;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO : Allineo tutte le immagini in base a quella di riferimento.
// TODO : Salvo ogni Output in una lista di immaginiPlus e la ritorno
public class HomographyAlignment {
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private ImagePlus impReference;
    private Point[] pointsReference;
    private Map<ImagePlus, Point[]> images;

    private HomographyAlignment(){}

    public static List<ImagePlus> align(final ImagePlus impReference,
                                        final Point[] pointsReference,
                                        final Map<ImagePlus, Point[]> images){
        final List<ImagePlus> output = new LinkedList<>();
        ImageJ2OpenCVConverter ij2cv = new ImageJ2OpenCVConverter();
        final Mat matReference = Core.
        return null;
    }
}
