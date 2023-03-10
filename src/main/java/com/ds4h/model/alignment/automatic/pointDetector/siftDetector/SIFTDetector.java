package com.ds4h.model.alignment.automatic.pointDetector.siftDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.features2d.SIFT;

public class SIFTDetector extends PointDetector {

    private final SIFT detector = SIFT.create();
    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint) {

    }
}
