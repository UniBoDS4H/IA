package com.ds4h.model.util.opencvManager.opencvDeleter;

import com.ds4h.model.util.opencvManager.opencvLoader.OpenCVLoader;
import ij.IJ;

import java.io.File;
import java.util.Objects;

public class OpencvDeleter {
    private final static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");
    private OpencvDeleter(){

    }
    private static void delete(){
        final File directory = new File(TEMPORARY_PATH);
        if(directory.isDirectory()){
            for(final File file : Objects.requireNonNull(directory.listFiles())){
                if(file.isFile() && file.getName().contains(OpenCVLoader.TMP_LIBRARY_NAME)){
                    if(file.delete()){
                        IJ.log("[OPENCV DELETER] Library removed successfully!");
                    }
                }
            }
        }
    }

    /**
     *
     */
    public static void deleteOpenCV(){
        OpencvDeleter.delete();
    }
}
