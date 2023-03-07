package com.ds4h.model.alignment.automatic;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.Mat;

import java.awt.*;

public class AutomaticAlgorithm extends AbstractAutomaticAlignment {

    public AutomaticAlgorithm(final PointDetector pointDetector, final AlignmentAlgorithm algorithm){
        super(pointDetector, algorithm);
    }

    @Override
    public void detectPoint(final Mat targetPoint, final ImagePoints imagePoints) {
        super.pointDetector().detectPoint(targetPoint, imagePoints);
    }

    @Override
    public void mergePoint(final ImagePoints targetImage, final ImagePoints imagePoints) {
        super.pointDetector().matchPoint(targetImage, imagePoints);
    }
}
