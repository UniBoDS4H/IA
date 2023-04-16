package com.ds4h.controller.directoryManager;

import com.ds4h.model.util.directoryManager.directoryDeleter.DirectoryDeleter;

public class DirectoryManager {

    private static final String PROJECT_NAME = "DS4H";

    private DirectoryManager(){

    }

    /**
     * Delete all the files inside the TMP directory.
     */
    public static void deleteTMPDirectories(){
        DirectoryDeleter.deleteTMP(PROJECT_NAME);
    }
}
