package com.ds4h.model.util.directoryCreator;

import com.ds4h.model.util.save.SaveImages;

import java.io.File;

public class DirectoryCreator {
    private DirectoryCreator(){

    }

    public static boolean createDirectory(final String path, final String nameDirectory){
        final File directory = new File(path+"/"+ nameDirectory);
        return directory.exists() || directory.mkdir();
    }
}
