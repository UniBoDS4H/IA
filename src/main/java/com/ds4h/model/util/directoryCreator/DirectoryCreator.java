package com.ds4h.model.util.directoryCreator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.time.LocalDate;
import java.time.LocalTime;

public class DirectoryCreator {
    private static final String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalTime NOW = LocalTime.now();

    private static final String YEAR = String.valueOf(TODAY.getYear());
    private static final String MONTH = TODAY.getMonthValue() <= 9 ? "0" + String.valueOf(TODAY.getMonthValue()) : String.valueOf(TODAY.getMonthValue());
    private static final String DAY = TODAY.getDayOfMonth() <= 9 ?  "0" + String.valueOf(TODAY.getDayOfMonth()) : String.valueOf(TODAY.getDayOfMonth());
    private static final String HOUR = NOW.getHour() <= 9 ?  "0" + String.valueOf(NOW.getHour()) : String.valueOf(NOW.getHour());
    private static final String MINUTE = NOW.getMinute() <= 9 ?  "0" + String.valueOf(NOW.getMinute()) : String.valueOf(NOW.getMinute());
    private static final String SECONDS = NOW.getSecond() <= 9 ? "0" + String.valueOf(NOW.getSecond()) : String.valueOf(NOW.getSecond());
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
        System.out.println(baseDirectoryName);
        //TODO: when the creation returns false, it menas that already exists another directory with the same name. Add random values to it
        return directory.mkdir() ? finalName : "";
    }

    private static String createTMPName(final String baseName){

        return (YEAR+MONTH+DAY+"_"+HOUR+MINUTE+SECONDS+ "_" + baseName);
    }

    private static String createName(final String baseName){
        return (YEAR+MONTH+DAY+"_"+HOUR+MINUTE+ "_" + baseName);
    }
}
