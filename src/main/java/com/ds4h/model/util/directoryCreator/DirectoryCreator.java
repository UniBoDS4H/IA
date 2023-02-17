package com.ds4h.model.util.directoryCreator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

public class DirectoryCreator {
    private DirectoryCreator(){

    }

    public static String createTemporaryDirectory(final String dirName){
        final String path = System.getProperty("java.io.tmpdir");
        return DirectoryCreator.createDirectory(path, dirName);
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
