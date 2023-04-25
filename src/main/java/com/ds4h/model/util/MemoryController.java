package com.ds4h.model.util;

import com.ds4h.model.imagePoints.ImagePoints;
import com.sun.management.OperatingSystemMXBean;
import ij.IJ;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * This class is used in order to check the memory when the images are loaded inside the Plugin.
 */
public class MemoryController {

    private final static double MEMORY_PERCENTAGE_LIMIT = 0.30;

    private MemoryController(){

    }

    /**
     * Control the memory after the "inputImages" are loaded.
     * @param imagePoints the images loaded from the Plugin.
     * @throws OutOfMemoryError if the 30% of the memory is used by the images.
     */
    public static void controlMemory(final List<ImagePoints> imagePoints) throws OutOfMemoryError{
        long memorySize = 0;
        final long totalMemory = (IJ.maxMemory()/(1024*1024));
        for(final ImagePoints img : imagePoints){
            final int width = img.getWidth();
            final int height = img.getHeight();
            final int depth = img.getBitDepth();
            memorySize += (((long) width *height*depth)/8);
        }
        memorySize = memorySize/(1024*1024);
        IJ.log("[MEMORY SIZE IMAGE] TotalSize: " + memorySize);
        IJ.log("[MEMORY SIZE IMAGE] Total Memory: " + totalMemory);
        IJ.log("[MEMORY SIZE IMAGE] Percentage: " + (memorySize/(double)totalMemory));

        if(memorySize/(double)totalMemory >= MEMORY_PERCENTAGE_LIMIT){
            throw new OutOfMemoryError(MemoryController.getError());
        }
    }

    /**
     * Returns the error message.
     * @return the error message.
     */
    private static String getError(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long physicalMemorySize = osBean.getTotalPhysicalMemorySize() -
                (osBean.getFreePhysicalMemorySize() + osBean.getFreeSwapSpaceSize()) ;
        long ramSizeGB = physicalMemorySize / (1024 * 1024);
        return "The memory is not enough.\n"+
                "Please, expand your memory to use this Plugin.\n " +
                "You can expand the memory from the Fiji/ImageJ menu:\n" +
                "\"Edit\" > \"Options\" > \"Memory & Thread\".\n" +
                "Write inside \"Maximum Memory\" a value higher than 4000 but lower than " + ramSizeGB + ".\n" +
                "After, re-start Fiji/ImageJ.";
    }
}
