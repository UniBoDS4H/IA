package com.ds4h.model.util.save;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.IJ;
import ij.ImagePlus;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class SaveImages {
    public final static String DIRECTORY = "DS4H_Project";

    private SaveImages(){

    }
    //TODO:ADD DOC
    public static void saveImages(final List<ImagePlus> images, final String path) throws IOException {
        final File directory = new File(path+"/"+SaveImages.DIRECTORY);
        if(!directory.exists()){
            if(directory.mkdir()){
                SaveImages.save(images,
                        path+"/"+ SaveImages.DIRECTORY);
            }else{
                SaveImages.save(images,
                        path);
            }
        }else{
            SaveImages.save(images,
                    path+"/"+ SaveImages.DIRECTORY);
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
