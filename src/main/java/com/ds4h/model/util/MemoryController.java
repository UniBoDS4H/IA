package com.ds4h.model.util;

import com.ds4h.model.imagePoints.ImagePoints;
import ij.IJ;
import org.opencv.core.Size;

import java.util.List;

public class MemoryController {

    private final static long  MEMORY_LIMIT = 150_000;
    private final static double MEMORY_PERCENTAGE_LIMIT = 0.35;
    private final static String errorMSG = "The remaining memory is not enough. Please consider\n " +
            "to expand your memory in order to perform this operation without having problems.\n " +
            "You can expand the memory by going to the Fiji/ImageJ menu and click on:\n" +
            "Edit > Options > Memory & Thread, put inside the \"Maximum Memory\" a value higher than 4000.\n" +
            "After that you will have to re-start Fiji/ImageJ, so export your project if you need it.";

    private MemoryController(){

    }


    public static void controllMemory(final List<ImagePoints> inputImaes) throws OutOfMemoryError{
        long memorySize = 0;
        final long totalMemory = (IJ.maxMemory()/(1024*1024));
        for(final ImagePoints img : inputImaes){
            final int width = img.getWidth();
            final int height = img.getHeight();
            final int bitdepth = img.getBitDepth();
            memorySize += (((long) width *height*bitdepth)/8);
        }
        memorySize = memorySize/(1024*1024);
        IJ.log("[MEMORY SIZE IMAGE] TotalSize: " + memorySize);
        IJ.log("[MEMORY SIZE IMAGE] Total Memory: " + totalMemory);
        IJ.log("[MEMORY SIZE IMAGE] Percentage: " + (memorySize/(double)totalMemory));

        if(memorySize/(double)totalMemory >= MEMORY_PERCENTAGE_LIMIT){
            throw new OutOfMemoryError(MemoryController.errorMSG);
        }
    }
}
