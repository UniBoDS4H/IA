package com.ds4h.model.util.save;

import com.ds4h.model.alignedImage.AlignedImage;
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

    public static void saveImages(final List<AlignedImage> images, final String path) throws IOException {
        final File directory = new File(path+"/"+SaveImages.DIRECTORY);
        if(!directory.exists()){
            if(directory.mkdir()){
                for(final AlignedImage image : images){
                    IJ.save(image.getAlignedImage(), path+"/"+ SaveImages.DIRECTORY +"/"+image.getAlignedImage().getTitle());
                }
            }else{
                for(final AlignedImage image : images){
                    IJ.save(image.getAlignedImage(), path+"/"+image.getAlignedImage().getTitle());
                }
            }
        }else{
            for(final AlignedImage image : images){
                IJ.save(image.getAlignedImage(), path+"/"+ SaveImages.DIRECTORY +"/"+image.getAlignedImage().getTitle());
            }
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
