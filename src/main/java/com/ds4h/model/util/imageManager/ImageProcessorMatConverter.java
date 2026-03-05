package com.ds4h.model.util.imageManager;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.util.logger.Logger;
import com.ds4h.model.util.logger.LoggerFactory;
import ij.IJ;
import ij.process.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.Objects;

public class ImageProcessorMatConverter {
    private final static Logger myLogger = LoggerFactory.getImageJLogger(ImageProcessorMatConverter.class);
    private ImageProcessorMatConverter(){

    }

    @NotNull
    private static Mat toMat(@NotNull final ShortProcessor sp){
        myLogger.log("From ShortProcessor");
        final Mat matrix = new Mat(sp.getHeight(), sp.getWidth(), CvType.CV_16U);
        matrix.put(0,0, (short[]) sp.getPixels());
        return  matrix;
    }

    @NotNull
    private static Mat toMat(@NotNull final ColorProcessor cp) {
        myLogger.log("From ColorProcessor");
        final Mat matrix = new Mat(cp.getHeight(), cp.getWidth(), CvType.CV_8UC3);
        final int[] pixels = (int[]) cp.getPixels();
        final byte[] bData = new byte[cp.getWidth() * cp.getHeight() * 3];

        // convert int-encoded RGB values to byte array
        for (int i = 0; i < pixels.length; i++) {
            bData[i * 3] = (byte) ((pixels[i] >> 16) & 0xFF);	// red
            bData[i * 3 + 1] = (byte) ((pixels[i] >> 8) & 0xFF);	// grn
            bData[i * 3 + 2] = (byte) ((pixels[i]) & 0xFF);	// blu
        }

        matrix.put(0,0, bData);
        return  matrix;
    }

    @NotNull
    private static Mat toMat(@NotNull final FloatProcessor fp){
        myLogger.log("From FloatProcessor");
        final Mat matrix = new Mat(fp.getHeight(), fp.getWidth(), CvType.CV_32FC1);
        matrix.put(0,0, (float[]) fp.getPixels());
        return  matrix;
    }

    @NotNull
    private static Mat toMat(@NotNull final ByteProcessor bp){
        myLogger.log("From ByteProcessor");
        final Mat matrix = new Mat(bp.getHeight(), bp.getWidth(), CvType.CV_8U);
        matrix.put(0,0, (byte[]) bp.getPixels());
        return  matrix;
    }

    /**
     * Convert the input "ImageProcessor" in to the corresponding Matrix.
     * @param ip the input processor, It represents the original Image.
     * @return the matrix with all the data of the image.
     * @throws IllegalArgumentException if the "ImageProcessor" is null.
     */
    @NotNull
    public static Mat convert(@NotNull final ImageProcessor ip) throws IllegalArgumentException{
        myLogger.log("Converting ImageProcessor");
        if(Objects.nonNull(ip)){
            if(ip instanceof ColorProcessor){
                return ImageProcessorMatConverter.toMat((ColorProcessor) ip);
            }else if(ip instanceof ShortProcessor){
                return ImageProcessorMatConverter.toMat((ShortProcessor) ip);
            }else if(ip instanceof FloatProcessor){
                return ImageProcessorMatConverter.toMat((FloatProcessor) ip);
            }else if(ip instanceof ByteProcessor){
                return ImageProcessorMatConverter.toMat((ByteProcessor) ip);
            }
        }
        throw new IllegalArgumentException("The type of your image processor is not handled.");
    }

    /**
     * Convert the input "ImageProcessor" in to a gray scale matrix.
     * @param ip the input "ImageProcessor".
     * @return the gray scale matrix.
     * @throws IllegalArgumentException if the input is null.
     */
    @NotNull
    public static Mat convertGray(@NotNull final ImageProcessor ip) throws IllegalArgumentException {
        if(Objects.nonNull(ip)) {
            final Mat matrix = ImageProcessorMatConverter.convert(ip);
            Core.normalize(matrix, matrix, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
            return matrix;
        }
        throw new IllegalArgumentException("The image processor is null. The conversion to gray can not be done.");
    }
}
