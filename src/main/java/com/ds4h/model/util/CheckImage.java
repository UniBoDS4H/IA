package com.ds4h.model.util;

import java.io.File;

public class CheckImage {

    private static final String EXTENSIONS ="jpg, jpeg, png, gif, bmp, tiff, tif, webp, svg, heif, heic, " +
            "raw, arw, cr2, nef, orf, rw2, dng, psd";
    private CheckImage(){}
    public static boolean checkImage(final File file){
        final String fileName = file.getName();
        final String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return CheckImage.EXTENSIONS.contains(fileExtension.toLowerCase());
    }
}
