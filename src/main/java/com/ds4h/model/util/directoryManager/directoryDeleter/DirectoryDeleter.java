package com.ds4h.model.util.directoryManager.directoryDeleter;

import ij.IJ;

import java.io.File;
import java.util.Objects;

public class DirectoryDeleter {
    private final static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");

    private DirectoryDeleter(){

    }

    public static void delete(final String path, final String directoryName){
        final File directory = new File(path);
        try {
            if(directory.isDirectory()) {
                for (final File file : Objects.requireNonNull(directory.listFiles())) {
                    if (file.isDirectory() && file.getName().contains(directoryName)) {
                        for (final File image : Objects.requireNonNull(file.listFiles())) {
                            image.delete();
                        }
                        file.delete();
                    }
                }
            }
        }catch(final Exception exception){
            IJ.showMessage(exception.getMessage());
        }
    }

    public static void deleteTMP(final String directoryName){
        DirectoryDeleter.delete(TEMPORARY_PATH, directoryName);
    }
}
