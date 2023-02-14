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
        loadLib(LINUX_LIB, ".dll");
    }
    private static void loadMac(){
        loadLib(LINUX_LIB, ".dylib");
    }
    private static void loadLinux(){
       loadLib(LINUX_LIB, ".so");
    }

    private static void loadLib(String name, String extension) {
        try {
            InputStream in = OpenCVLoader.class.getResourceAsStream("/opencv/" + name + extension);
            File fileOut = File.createTempFile("lib", extension);
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
