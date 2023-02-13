package com.ds4h.model.util.directoryCreator;

import com.ds4h.model.util.save.SaveImages;

import java.io.File;

public class DirectoryCreator {
    private DirectoryCreator(){

    }

    public static boolean createDirectory(final String path, final String dirName){
        final String baseDirectoryName = path+"/"+ dirName;
        File directory = new File(baseDirectoryName);
        if(!directory.exists()){
            return directory.mkdir();
        }else{
            int counter = 1;
            String directoryName = dirName;
            while(directory.exists()){
                directoryName = baseDirectoryName+"-"+counter;
                directory = new File(directoryName);
                counter++;
            }
            return directory.mkdirs();
        }
    }
}
