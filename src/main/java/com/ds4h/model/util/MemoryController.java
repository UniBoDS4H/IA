package com.ds4h.model.util;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.image.imagePoints.ImagePoints;
import com.ds4h.model.util.logger.Logger;
import com.ds4h.model.util.logger.LoggerFactory;
import com.sun.management.OperatingSystemMXBean;
import ij.IJ;

import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * This class is used in order to check the memory when the images are loaded inside the Plugin.
 */
public class MemoryController {
    private final static Logger myLogger = LoggerFactory.getImageJLogger(MemoryController.class);
    private final static double MEMORY_PERCENTAGE_LIMIT = 30.0;

    private MemoryController(){

    }

    /**
     * Control the memory after the "inputImages" are loaded.
     * @param imagePoints the images loaded from the Plugin.
     * @throws OutOfMemoryError if the 30% of the memory is used by the images.
     */
    public static void controlMemory(@NotNull final List<ImagePoints> imagePoints) throws OutOfMemoryError{
        final long totalMemory = (IJ.maxMemory()/(1024*1024));
        final long currentMemory = (IJ.currentMemory() / (1024 * 1024));
        final double usedMemory = ((double) currentMemory / totalMemory) * 100;
        myLogger.log("Total Size: " + currentMemory + " MB");
        myLogger.log("Total Memory: " + totalMemory+ " MB");
        myLogger.log("Percentage: " + usedMemory+ " %");
        if(usedMemory >= MEMORY_PERCENTAGE_LIMIT){
            throw new OutOfMemoryError(MemoryController.getError());
        }
    }

    /**
     * Returns the error message.
     * @return the error message.
     */
    private static String getError(){
        final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        final long physicalMemorySize = osBean.getTotalPhysicalMemorySize() -
                (osBean.getFreePhysicalMemorySize() + osBean.getFreeSwapSpaceSize()) ;
        final long ramSizeGB = physicalMemorySize / (1024 * 1024);
        return "The memory is not enough.\n"+
                "Please, expand your memory to use this Plugin.\n " +
                "You can expand the memory from the Fiji/ImageJ menu:\n" +
                "\"Edit\" > \"Options\" > \"Memory & Thread\".\n" +
                "Write inside \"Maximum Memory\" a value higher than 4000 but lower than " + ramSizeGB + ".\n" +
                "After, re-start Fiji/ImageJ.";
    }
}
