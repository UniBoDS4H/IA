package com.ds4h.model.util.directoryManager.directoryDeleter;

import ij.IJ;

import java.io.File;
import java.util.Objects;

/**
 *
 */
public class DirectoryDeleter {
    private final static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");
    private DirectoryDeleter(){

    }

    /**
     *
     * @param path a
     * @param directoryName b
     */
    public static void delete(final String path, final String directoryName){
        final File directory = new File(path);
        if(directory.isDirectory()) {
            for (final File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isDirectory() && file.getName().contains(directoryName)) {
                    for (final File image : Objects.requireNonNull(file.listFiles())) {
                        if(image.delete()){
                            IJ.log("Directory deleted.");
                        }
                    }
                    if(file.delete()){
                        IJ.log("Directory deleted.");
                    }
                }
            }
        }
    }

    /**
     *
     * @param directoryName a
     */
    public static void deleteTMP(final String directoryName){
        DirectoryDeleter.delete(TEMPORARY_PATH, directoryName);
    }
}
