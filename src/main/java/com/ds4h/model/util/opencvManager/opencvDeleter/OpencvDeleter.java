package com.ds4h.model.util.opencvManager.opencvDeleter;

import com.ds4h.model.util.opencvManager.opencvLoader.OpenCVLoader;
import java.io.File;
import java.util.Objects;

public class OpencvDeleter {
    private final static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");
    private OpencvDeleter(){

    }
    private static void delete(final String path, final String tmpName){
        final File directory = new File(path);
        if(directory.isDirectory()){
            for(final File file : Objects.requireNonNull(directory.listFiles())){
                if(file.isFile() && file.getName().contains(tmpName)){
                    //TODO: read the value and understand what to do if it is false
                    file.delete();
                }
            }
        }

    }

    public static void deleteOpenCV(){
        OpencvDeleter.delete(TEMPORARY_PATH, OpenCVLoader.TMP_LIBRARY_NAME);
    }
}
