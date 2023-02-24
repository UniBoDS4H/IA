package com.ds4h.model.util.directoryManager.directoryCreator;

import com.ds4h.model.util.DateTime;

import java.io.File;

/**
 * This class is used in order to create directory for the project. This class can create directory inside a specific path,
 * selected by the user interface, It can also create directories inside the TMP folder, this method is used when we call the
 * "Reuse as Source", because we need to store the aligned images inside the file system before to use them. Every Aligned algorithm is
 * based on "ImageCorners" so we must save the AlignedImages inside the TMP folder, and then we can use it as "ImageCorners".
 */
public class DirectoryCreator {
    private static final String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");
    private DirectoryCreator(){

    }

    /**
     * This method is used for the creation of a folder inside the Temporary directory.
     * This function will automatically add in front of the dirName : YEAR+MONTH+"_"+DAY_HOUR+MINUTE+SECONDS+"_"+dirName
     * In order to give more information about the directory
     * @param dirName : the name of the directory
     * @return the name of the directory if It is successfully created otherwise It returns an empty string.
     */
    public static String createTemporaryDirectory(final String dirName){
        final String finalName = DirectoryCreator.createTMPName(dirName);
        final String baseDirectoryName = TEMPORARY_PATH+"/"+ finalName;
        final File directory = new File(baseDirectoryName);
        directory.deleteOnExit();
        return directory.mkdir() ? finalName : "";
    }

    /**
     * This method creates a directory inside the input path.
     * This function will automatically add in front of the dirName : YEAR+MONTH+"_"+DAY_HOUR+MINUTE+"_"+dirName
     * In order to give more information about the directory
     * @param path : where to create the directory
     * @param dirName : the directory name
     * @return the name of the directory if It is successfully created otherwise It returns an empty string.
     */
    public static String createDirectory(final String path, final String dirName){
        if(!path.isEmpty() && !dirName.isEmpty()) {
            final String finalName = DirectoryCreator.createName(dirName);
            final String baseDirectoryName = path + "/" + finalName;
            final File directory = new File(baseDirectoryName);
            //TODO: when the creation returns false, it menas that already exists another directory with the same name. Add random values to it
            return directory.mkdir() ? finalName : "";
        }else{
            //TODO: Add more controls
            //throw IllegalArgumentException("");
            return "";
        }
    }

    /**
     * This method append in front of the baseName YEAR, MONTH, DAY and HOUR, MINUTE, SECONDS
     * @param baseName : the base name of the folder
     * @return the new name of the folder
     */
    private static String createTMPName(final String baseName){
        final DateTime today = new DateTime();
        return (today.year()+today.month()+today.day()+"_"+ today.hour()+today.minute()+today.seconds()+ "_" + baseName);
    }
    /**
     * This method append in front of the baseName YEAR, MONTH, DAY and HOUR, MINUTE
     * @param baseName : the base name of the folder
     * @return the new name of the folder
     */
    private static String createName(final String baseName){
        final DateTime today = new DateTime();
        return (today.year()+today.month()+today.day()+"_"+today.hour()+today.minute()+ "_" + baseName);
    }
}
