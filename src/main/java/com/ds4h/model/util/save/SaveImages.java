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
        final String dir = DirectoryCreator.createDirectory(path, DIRECTORY);
        if(!dir.isEmpty()){
            final File directory = new File(path + "/"+dir);
            SaveImages.save(images, path+"/" + dir);
        }else{
            SaveImages.save(images, path);
        }
    }



    public static void save(final List<ImagePlus> images, final String path){
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
