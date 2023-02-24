package com.ds4h.controller.directoryManager;

import com.ds4h.model.util.directoryManager.directoryDeleter.DirectoryDeleter;
import com.ds4h.model.util.saveProject.SaveImages;

public class DirectoryManager {

    private DirectoryManager(){

    }

    public static void deleteTMPDirectories(){
        DirectoryDeleter.deleteTMP(SaveImages.TMP_DIRECTORY);
    }
}
