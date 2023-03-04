package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.util.Pair;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;

public interface AlignmentAlgorithmInterface {
    void alignImages(final CornerManager cornerManager);

    Mat getTransformationMatrix(Point[] dstArray, Point[] srcArray);

    boolean isAlive();
}
