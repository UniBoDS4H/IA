package com.ds4h.model.image.imagePoints;

import com.drew.lang.annotations.NotNull;

public class ImagePointsFactory {

    @NotNull
    public static ImagePoints createFromFilePath(@NotNull final String filePath) {
        return new ImagePoints(filePath);
    }

}
