package com.ds4h.model.alignment;

import com.ds4h.model.cornerManager.CornerManager;
import ij.ImagePlus;

import java.util.List;

public interface AlignmentAlgorithmInterface {
    List<ImagePlus> alignImages(final CornerManager cornerManager);
}
