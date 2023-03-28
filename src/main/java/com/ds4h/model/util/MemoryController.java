package com.ds4h.model.util;

import com.ds4h.model.imagePoints.ImagePoints;
import ij.IJ;
import org.opencv.core.Size;

import java.util.List;

public class MemoryController {

    private final static long  MEMORY_LIMIT = 150_000; //(150MB);
    private final static Runtime runtime = Runtime.getRuntime();

    private MemoryController(){

    }

    public static void controllMemory(final Size size){
        if(runtime.totalMemory() - runtime.freeMemory() <= (size.width*size.height * 8)){
            throw new OutOfMemoryError("The remaining memory is not enough. Please consider " +
                    "to expand your memory in order to perform this operation without having problems. " +
                    "You can expand the memory by going to the Fiji/ImageJ menu and click on:" +
                    "Edit > Options > Memory & Thread, put inside the \"Maximum Memory\" a value higher than 4000." +
                    "After that you will have to re-start Fiji/ImageJ, so export your project if you need it.");
        }
    }

    public static void controllMemory(final List<ImagePoints> inputImaes){
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

        if(memorySize/(double)totalMemory >= 0.35){
            throw new OutOfMemoryError("The remaining memory is not enough. Please consider " +
                    "to expand your memory in order to perform this operation without having problems. " +
                    "You can expand the memory by going to the Fiji/ImageJ menu and click on:" +
                    "Edit > Options > Memory & Thread, put inside the \"Maximum Memory\" a value higher than 4000." +
                    "After that you will have to re-start Fiji/ImageJ, so export your project if you need it.");
        }
    }

    public static void controllMemory(){
        IJ.log("[FREE MEMORY] Total Free Memory: " + (runtime.totalMemory() - runtime.freeMemory()));
        if(runtime.totalMemory() - runtime.freeMemory() <= MEMORY_LIMIT){
            throw new OutOfMemoryError("The remaining memory is not enough. Please consider " +
                    "to expand your memory in order to perform this operation without having problems. " +
                    "You can expand the memory by going to the Fiji/ImageJ menu and click on:" +
                    "Edit > Options > Memory & Thread, put inside the \"Maximum Memory\" a value higher than 4000." +
                    "After that you will have to re-start Fiji/ImageJ, so export your project if you need it.");
        }
    }
}
