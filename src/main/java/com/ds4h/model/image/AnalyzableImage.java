package com.ds4h.model.image;

import ij.ImagePlus;
import org.jetbrains.annotations.NotNull;

public interface AnalyzableImage extends PointRepository, DataImage{
    /**
     * This method returns the corresponding {@link ImagePlus} linked to this class.
     * @return see above.
     */
    @NotNull
    ImagePlus getImagePlus();

    /**
     * This method returns the image name.
     * @return see above.
     */
    @NotNull
    String getName();
}
