package com.ds4h.model.alignment;

import com.ds4h.model.pointManager.PointManager;

public interface AlignmentAlgorithmInterface {
    void alignImages(final PointManager pointManager);

    boolean isAlive();
}
