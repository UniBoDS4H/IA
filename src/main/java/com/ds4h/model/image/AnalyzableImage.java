package com.ds4h.model.image;

import ij.ImagePlus;
import org.jetbrains.annotations.NotNull;

public interface AnalyzableImage extends PointRepository, DataImage{
    /**
     * TODO
     * @return
     */
    @NotNull
    ImagePlus getImage();

    /**
     * TODO
     * @return
     */
    @NotNull
    String getName();
}
