package com.ds4h.model.util.directoryCreator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.time.LocalDate;
import java.time.LocalTime;

public class DirectoryCreator {
    private DirectoryCreator(){

    }

    public static String createTemporaryDirectory(final String dirName){
        final String path = System.getProperty("java.io.tmpdir");
        return DirectoryCreator.createDirectory(path, dirName);
    }

    public static String createDirectory(final String path, final String dirName){
        final String finalName = DirectoryCreator.createName(dirName);
        final String baseDirectoryName = path+"/"+ finalName;
        File directory = new File(baseDirectoryName);
        return directory.mkdir() ? finalName : "";
            /*
        }else{
            int counter = 1;
            String directoryName = dirName;
            String name = dirName;
            while(directory.exists()){
                directoryName = baseDirectoryName+"-"+counter;
                name = dirName + "-" + counter;
                directory = new File(directoryName);
                counter++;
            }
            return directory.mkdirs() ? name : "";
        }
        */
    }

    private static String createName(final String baseName){
        final LocalDate date = LocalDate.now();
        final LocalTime time = LocalTime.now();
        // Extract Year, Month and Day
        final String year = String.valueOf(date.getYear());
        final String month = date.getMonthValue() <= 9 ? "0" + String.valueOf(date.getMonthValue()) : String.valueOf(date.getMonthValue());
        final String day = date.getDayOfMonth() <= 9 ?  "0" + String.valueOf(date.getDayOfMonth()) : String.valueOf(date.getDayOfMonth());
        //Extract Hour and Minute
        final String hour = time.getHour() <= 9 ?  "0" + String.valueOf(time.getHour()) : String.valueOf(time.getHour());
        final String minute = time.getMinute() <= 9 ?  "0" + String.valueOf(time.getMinute()) : String.valueOf(time.getMinute());
        return (year+month+day+"_"+hour+minute + "_" + baseName);
    }
}
