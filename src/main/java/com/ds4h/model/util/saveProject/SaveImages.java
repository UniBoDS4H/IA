package com.ds4h.model.util.saveProject;

import com.ds4h.model.util.directoryCreator.DirectoryCreator;
import ij.IJ;
import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class SaveImages {
    private final static String DIRECTORY = "DS4H_AlignedImages", TMP_DIRECTORY = "TMP_DS4H_AlignedImages";
    private final static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");

    private SaveImages(){

    }
    //TODO:ADD DOC
    public static void saveImages(final List<ImagePlus> images, final String path) throws IOException {
        final String dir = DirectoryCreator.createDirectory(path, DIRECTORY);
        if(!dir.isEmpty()){
            SaveImages.save(images, path+"/" + dir);
        }else{
            SaveImages.save(images, path);
        }
    }

    public static String saveTMPImages(final List<ImagePlus> images){
        final String dir = DirectoryCreator.createTemporaryDirectory(TMP_DIRECTORY);
        if(!dir.isEmpty()){
            SaveImages.save(images, TEMPORARY_PATH+ "/" + dir);
        }
        return TEMPORARY_PATH + "/" + dir;
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
