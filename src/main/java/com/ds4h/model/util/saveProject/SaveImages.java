package com.ds4h.model.util.saveProject;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.ds4h.model.util.saveProject.saveReferenceMatrix.SaveMatrix;
import ij.IJ;
import ij.ImagePlus;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 */
public class SaveImages {
    private final static String DIRECTORY = "DS4H_AlignedImages";

    private SaveImages(){

    }

    /**
     * Save all the images inside the selected path.
     * @param images all the images that will be stored.
     * @param path the path where the images will be stored.
     */
    public static void saveImages(final List<AlignedImage> images, final String path) {
        final String dir = DirectoryCreator.createDirectory(path, DIRECTORY);
        if(!dir.isEmpty()){
            SaveImages.save(images.stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList()), path+"/"+dir);
            SaveMatrix.saveMatrix(images, path+"/"+dir);
        }else{
            SaveImages.save(images.stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList()), path);
        }
    }

    /**
     * Save the images inside the path.
     * @param images the images to be saved.
     * @param path the path where will be stored the images.
     */
    public static void save(final List<ImagePlus> images, final String path){
        //TODO: The parallel can cause out of memory exception.
        images.parallelStream().forEach(image -> IJ.save(image, path+"/"+image.getTitle()));
    }

    /**
     * Save a single image inside the path.
     * @param image the image to be stored.
     * @param path the path where will be stored the image.
     */
    public static void save(final ImagePlus image, final String path){
        if(!path.isEmpty() && Objects.nonNull(image)) {
            IJ.save(image, path + "/" + image.getTitle());
        }
    }
}