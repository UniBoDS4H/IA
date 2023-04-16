package com.ds4h.controller.opencvController;

import com.ds4h.model.util.opencvManager.opencvDeleter.OpencvDeleter;
import com.ds4h.model.util.opencvManager.opencvLoader.OpenCVLoader;

/**
 *
 */
public class OpencvController {

    private OpencvController(){

    }

    /**
     * Load the OpenCV library inside the TMP folder.
     */
    public static void loadLibrary(){
        OpenCVLoader.loadOpenCV();
    }

    /**
     * Remove the OpenCV library from the TMP folder.
     */
    public static void deleteLibrary(){
        OpencvDeleter.deleteOpenCV();
    }
}
