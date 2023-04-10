package com.ds4h.model.reuse;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.MemoryController;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used for the functionality : "reuse as source", in order to use the result of the alignment as new images for the project.
 * In order to implement this function, we must first save all the images that we want to use in a Temporary Folder of the current OS.
 * After saving all the needed images inside the TMP folder, we can create a List of ImageCorners that will be used for the project.
 */
public class ReuseSources {

    /**
     *
     */
    private ReuseSources(){

    }

    /**
     * Method used in order to create a new project with the selected images from the input.
     * @param pointManager : Inside the corner manager we will be going to store the now images
     * @param images : The list of images that we want to use
     */
    public static void reuseSources(final PointManager pointManager, final List<AlignedImage> images) throws OutOfMemoryError{
        if(!images.isEmpty()) {
            pointManager.clearList();
            pointManager.clearProject();
            pointManager.addImages(ReuseSources.convertImages(images));
            MemoryController.controllMemory(pointManager.getCornerImages());
            System.gc();
        }
    }

    private static List<ImagePoints> convertImages(final List<AlignedImage> images){
        return images.stream()
                .map(AlignedImage::getAlignedImage)
                .map(imagePlus -> {
                    final ImagePoints image = new ImagePoints(imagePlus.getTitle(), imagePlus.getProcessor());
                    image.setTitle(imagePlus.getTitle());
                    imagePlus = null;
                    System.gc();
                    return image;
                })
                .collect(Collectors.toList());
    }
}
