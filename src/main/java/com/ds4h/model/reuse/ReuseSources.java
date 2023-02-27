package com.ds4h.model.reuse;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.projectManager.importProject.ImportProject;
import com.ds4h.model.util.saveProject.SaveImages;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used for the functionality : "reuse as source", in order to use the result of the alignment as new images for the project.
 * In order to implement this function, we must first save all the images that we want to use in a Temporary Folder of the current OS.
 * After saving all the needed images inside the TMP folder, we can create a List of ImageCorners that will be used for the project.
 */
public class ReuseSources {

    private ReuseSources(){

    }

    /**
     * Method used in order to create a new project with the selected images from the input.
     * @param cornerManager : Inside the corner manager we will be going to store the now images
     * @param images : The list of images that we want to use
     */
    public static void reuseSources(final CornerManager cornerManager, final List<AlignedImage> images) {
        if(!images.isEmpty()) {
            final String path = SaveImages.saveTMPImages(images.stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList()));
            System.out.println(path);
            if (!path.isEmpty()) {
                final List<ImageCorners> backUpList = cornerManager.getCornerImages();
                try {
                    cornerManager.clearList();
                    cornerManager.addImages(ImportProject.importProject(new File(path)));
                }catch(final FileNotFoundException ex){
                    cornerManager.addImages(backUpList);
                }
            }
        }
    }
}
