package com.ds4h.model.util.directoryCreator;

import com.ds4h.model.util.DateTime;

import java.io.File;

public class DirectoryCreator {
    private static final String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");
    private DirectoryCreator(){

    }

    public static String createTemporaryDirectory(final String dirName){
        final String finalName = DirectoryCreator.createTMPName(dirName);
        final String baseDirectoryName = TEMPORARY_PATH+"/"+ finalName;
        final File directory = new File(baseDirectoryName);
        return directory.mkdir() ? finalName : "";
    }

    public static String createDirectory(final String path, final String dirName){
        final String finalName = DirectoryCreator.createName(dirName);
        final String baseDirectoryName = path+"/"+ finalName;
        final File directory = new File(baseDirectoryName);
        //TODO: when the creation returns false, it menas that already exists another directory with the same name. Add random values to it
        return directory.mkdir() ? finalName : "";
    }

    private static String createTMPName(final String baseName){
        final DateTime today = new DateTime();
        return (today.year()+today.month()+today.day()+"_"+ today.hour()+today.minute()+today.seconds()+ "_" + baseName);
    }

    private static String createName(final String baseName){
        final DateTime today = new DateTime();
        return (today.year()+today.month()+today.day()+"_"+today.hour()+today.minute()+ "_" + baseName);
    }
}
