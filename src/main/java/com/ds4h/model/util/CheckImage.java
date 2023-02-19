package com.ds4h.model.util;

import java.io.File;
import java.util.Objects;

public class CheckImage {

    private static final String EXTENSIONS ="jpg, jpeg, png, gif, bmp, tiff, tif, webp, svg, heif," +
            " heic, raw, arw, cr2, nef, orf, " +
            "rw2, dng, psd, pcx," +
            " ppm, pgm, pbm, dds, hdr, exr, pfm, icns, xbm, xpm, pict, jp2, jpx, pcd";
    private CheckImage(){}
    public static boolean checkImage(final File file){
        if(Objects.nonNull(file)) {
            final String fileName = file.getName();
            final String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            //TODO: Check the size of the image
            return CheckImage.EXTENSIONS.contains(fileExtension.toLowerCase());
        }else{
            return false;
        }
    }
}
