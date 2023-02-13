package com.ds4h.model.util.directoryCreator;

import com.ds4h.model.util.save.SaveImages;

import java.io.File;

public class DirectoryCreator {
    private DirectoryCreator(){

    }

    public static String createDirectory(final String path, final String dirName){
        final String baseDirectoryName = path+"/"+ dirName;
        File directory = new File(baseDirectoryName);
        if(!directory.exists()){
            return directory.mkdir() ? dirName : "";
        }else{
            int counter = 1;
            String directoryName = dirName;
            String name = dirName;
            while(directory.exists()){
                directoryName = baseDirectoryName+"-"+counter;
                name = dirName + "-" + counter;
                directory = new File(directoryName);
                counter++;
            }
            return directory.mkdirs() ? name : "";
        }
    }
}
