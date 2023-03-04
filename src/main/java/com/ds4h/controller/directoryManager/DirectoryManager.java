package com.ds4h.controller.directoryManager;

import com.ds4h.model.util.directoryManager.directoryDeleter.DirectoryDeleter;
import com.ds4h.model.util.saveProject.SaveImages;

public class DirectoryManager {

    private static final String PROJECT_NAME = "DS4H";

    private DirectoryManager(){

    }

    public static void deleteTMPDirectories(){
        DirectoryDeleter.deleteTMP(PROJECT_NAME);
    }
}
