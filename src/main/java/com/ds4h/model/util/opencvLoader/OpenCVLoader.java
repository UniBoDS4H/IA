package com.ds4h.model.util.opencvLoader;

import ij.IJ;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.*;

public class OpenCVLoader {
    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final String RESOURCE_DIRECTORY = "resources", SOURCE = "src", MAIN = "main", OPENCV = "opencv";
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String WINDOWS = "windows", MAC = "mac", LINUX = "linux";
    private static final String WINDOWS_LIB = "opencv_java455.dll", MAC_LIB = "opencv_java455.dylib", LINUX_LIB = "libopencv_java455";
    private OpenCVLoader(){

    }

    public static void loadOpenCV(){
        IJ.showMessage(OS);
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
        final String file = PROJECT_DIRECTORY+SEPARATOR+SOURCE+SEPARATOR+MAIN+SEPARATOR+RESOURCE_DIRECTORY+SEPARATOR+OPENCV+SEPARATOR+LINUX_LIB+".so";
        loadLib((OPENCV+SEPARATOR),LINUX_LIB);
        IJ.showMessage(file.toString());
        //System.load(file);
    }

    private static void loadLib(String path, String name) {
        try {
            InputStream in = OpenCVLoader.class.getResourceAsStream("/" + path + name +".so");
            File fileOut = File.createTempFile("lib", ".so");
            try (OutputStream out = FileUtils.openOutputStream(fileOut)) {
                if (in != null) {
                    IOUtils.copy(in, out);
                    in.close();
                    out.close(); // Without this line it doesn't work on windows, so, just leave it there, avoid even the check for the OS
                    System.load(fileOut.toString());
                }
            }
        } catch (Exception e) {
            IJ.showMessage(e.getMessage());
        }
    }
}
