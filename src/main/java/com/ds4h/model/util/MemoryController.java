package com.ds4h.model.util;

import com.ds4h.model.imagePoints.ImagePoints;
import com.sun.management.OperatingSystemMXBean;
import ij.IJ;
import org.opencv.core.Size;

import java.lang.management.ManagementFactory;
import java.util.List;

public class MemoryController {

    private final static long  MEMORY_LIMIT = 150_000;
    private final static double MEMORY_PERCENTAGE_LIMIT = 0.35;

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
            throw new OutOfMemoryError(MemoryController.getError());
        }
    }

    private static String getError(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long physicalMemorySize = osBean.getTotalPhysicalMemorySize() -
                (osBean.getFreePhysicalMemorySize() + osBean.getFreeSwapSpaceSize()) ;
        long ramSizeGB = physicalMemorySize / (1024 * 1024);
        final String errorMsg = "The memory is not enough.\n"+
                "Please, expand your memory to use this Plugin.\n " +
                "You can expand the memory from the Fiji/ImageJ menu:\n" +
                "\"Edit\" > \"Options\" > \"Memory & Thread\".\n" +
                "Write inside \"Maximum Memory\" a value higher than 4000 but lower than " + ramSizeGB + ".\n" +
                "After, re-start Fiji/ImageJ.";
        return errorMsg;
    }
}
