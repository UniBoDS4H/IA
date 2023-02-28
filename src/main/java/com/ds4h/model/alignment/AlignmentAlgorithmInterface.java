package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.util.Pair;
import ij.ImagePlus;
import org.opencv.core.Mat;

import java.util.List;

public interface AlignmentAlgorithmInterface {
    void alignImages(final CornerManager cornerManager);

    boolean isAlive();
}
