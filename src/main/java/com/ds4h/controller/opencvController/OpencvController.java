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
     *
     */
    public static void loadLibrary(){
        OpenCVLoader.loadOpenCV();
    }

    /**
     *
     */
    public static void deleteLibrary(){
        OpencvDeleter.deleteOpenCV();
    }
}
