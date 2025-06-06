package com.ds4h.model.image;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public interface DataImage extends PointRepository {
    /**
     * Get from the current image, its gray scale matrix.
     * @return Gray scale matrix
     */
    Mat getGrayScaleMat();

    /**
     * Enable/Disable the possibility of improving this image.
     */
    void improve();

    /**
     * Check if the image has to be improved.
     * @return true if the image has to be improved otherwise false.
     */
    boolean toImprove();
}
