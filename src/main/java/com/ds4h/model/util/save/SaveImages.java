package com.ds4h.model.util.save;

import com.ds4h.model.util.directoryCreator.DirectoryCreator;
import ij.IJ;
import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class SaveImages {
    private final static String DIRECTORY = "DS4H_Project";

    private SaveImages(){

    }
    //TODO:ADD DOC
    public static void saveImages(final List<ImagePlus> images, final String path) throws IOException {
        if(DirectoryCreator.createDirectory(path, DIRECTORY)){
            final File directory = new File(path + "/"+DIRECTORY);
            SaveImages.save(images, path+"/" + DIRECTORY);
        }else{
            SaveImages.save(images, path);
        }
    }



    private static void save(final List<ImagePlus> images, final String path){
        for(final ImagePlus image : images){
            IJ.save(image, path+"/"+image.getTitle());
        }
    }

    private static String getFormat(final ImagePlus image){
        try {
            return ImageIO.getImageReaders(ImageIO.createImageInputStream(image.getImage())).next().getFormatName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
