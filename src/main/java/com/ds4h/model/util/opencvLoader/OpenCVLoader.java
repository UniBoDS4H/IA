package com.ds4h.model.util.opencvLoader;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class OpenCVLoader {
    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final String RESOURCE_DIRECTORY = "resources", SOURCE = "src", MAIN = "main", OPENCV = "opencv";
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String WINDOWS = "windows", MAC = "mac", LINUX = "linux";
    private static final String WINDOWS_LIB = "opencv_java455.dll", MAC_LIB = "opencv_java455.dylib", LINUX_LIB = "libopencv_java455.so";
    private OpenCVLoader(){

    }

    public static void loadOpenCV(){
        if(OS.contains(WINDOWS)){

        }else if(OS.contains(MAC)){

        }else if(OS.contains(LINUX)){
            loadLinux();
        }
    }
    private static void loadWindows(){
        System.load(PROJECT_DIRECTORY+SEPARATOR+RESOURCE_DIRECTORY+SEPARATOR+WINDOWS_LIB);
    }
    private static void loadMac(){
        System.load(PROJECT_DIRECTORY+SEPARATOR+RESOURCE_DIRECTORY+SEPARATOR+MAC_LIB);
    }
    private static void loadLinux(){
        /*
        final String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, LINUX_LIB);
        final InputStream in = OpenCVLoader.class.getResourceAsStream("/" + LINUX_LIB);
        */
        final String file = PROJECT_DIRECTORY+SEPARATOR+SOURCE+SEPARATOR+MAIN+SEPARATOR+RESOURCE_DIRECTORY+SEPARATOR+OPENCV+SEPARATOR+LINUX_LIB;
        //loadLib((OPENCV+SEPARATOR),LINUX_LIB);
        System.out.println(file);
        System.load(file);
    }

    private static void loadLib(String path, String name) {
        name = System.mapLibraryName(name); // extends name with .dll, .so or .dylib
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = OpenCVLoader.class.getResourceAsStream("/" + path + name);
            File fileOut = new File("/usr/local/lib/");
            System.out.println(fileOut.toString());
            outputStream = new FileOutputStream(fileOut);
            IOUtils.copy(inputStream,outputStream);
            System.out.println(fileOut.toString());
            System.load(fileOut.toString());//loading goes here
        } catch (Exception e) {
            //handle
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //log
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    //log
                }
            }
        }
    }
}
