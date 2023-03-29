package com.ds4h.view.util;

import com.ds4h.model.imagePoints.ImagePoints;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
    private static final Map<String, BufferedImage> cache = new HashMap<>();

    public static BufferedImage getScaledImage(ImagePoints originalImage) {
        String key = originalImage.toString() + "_" + originalImage.getWidth() + "_" + originalImage.getHeight();
        BufferedImage scaledImage = cache.get(key);
        if (scaledImage == null) {
            scaledImage = originalImage.resize(40,40,"bilinear").getBufferedImage();
            cache.put(key, scaledImage);
        }
        return scaledImage;
    }
}
