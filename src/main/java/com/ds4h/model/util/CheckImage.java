package com.ds4h.model.util;

import java.io.File;

public class CheckImage {
    private CheckImage(){}
    public static boolean checkImage(final File file){
        try {
            //return format != null;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
