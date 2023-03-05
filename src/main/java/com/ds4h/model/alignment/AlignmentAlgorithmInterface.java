package com.ds4h.model.alignment;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.pointManager.PointManager;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.awt.*;

public interface AlignmentAlgorithmInterface {
    void alignImages(final PointManager pointManager);

    Mat getTransformationMatrix(final ImagePoints imageToAlign, final ImagePoints targetImage);

    void transform(final Mat source, final Mat destination, final Mat H);

    boolean isAlive();
}
