package com.ds4h.view.util;

import com.ds4h.model.alignedImage.AlignedImage;
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
            scaledImage = originalImage.getProcessor().resize(40,40).getBufferedImage();//.resize(40,40,"bilinear").getBufferedImage();
            cache.put(key, scaledImage);
        }
        return scaledImage;
    }

    public static BufferedImage getScaledImage(AlignedImage originalImage) {
        String key = originalImage.getName() + "_Aligned_" + originalImage.getAlignedImage().getWidth() + "_" + originalImage.getAlignedImage().getHeight();
        BufferedImage scaledImage = cache.get(key);
        if (scaledImage == null) {
            scaledImage = originalImage.getAlignedImage().getProcessor().resize(40,40).getBufferedImage();//.resize(40,40,"bilinear").getBufferedImage();
            cache.put(key, scaledImage);
        }
        return scaledImage;
    }
    public static void clear() {
        cache.clear();
    }
}
