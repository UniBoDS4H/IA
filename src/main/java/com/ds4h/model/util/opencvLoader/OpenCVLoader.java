package com.ds4h.model.util.opencvLoader;

import ij.IJ;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.*;
import java.util.Objects;

public class OpenCVLoader {
    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final String RESOURCE_DIRECTORY = "resources", SOURCE = "src", MAIN = "main", OPENCV = "/opencv/";
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String WINDOWS = "windows", MAC = "mac", LINUX = "linux";
    private static final String WINDOWS_FORMAT = ".dll", MAC_FORMAT = ".dylib", LINUX_FORMAT = ".so";
    private static final String WINDOWS_LIB = "opencv_java455",
            MAC_LIB_INTEL = "opencv_java455",
            MAC_LIB_ARM = "libopencv_arm_java455",
            LINUX_LIB = "libopencv_java455";
    private OpenCVLoader(){

    }

    public static void loadOpenCV(){
        IJ.showMessage(OS);
        if(OS.contains(WINDOWS)){
            loadWindows();
        }else if(OS.contains(MAC)){
            final String arch = System.getProperty("os.arch");
            if (arch.equals("arm") || arch.startsWith("armv")) {
                loadMacArm();
            } else {
                loadMacIntel();
            }

        }else if(OS.contains(LINUX)){
            loadLinux();
        }
    }
    private static void loadWindows(){
        loadLib(WINDOWS_LIB, WINDOWS_FORMAT);
    }
    private static void loadMacIntel(){
        loadLib(MAC_LIB_INTEL, MAC_FORMAT);
    }
    private static void loadMacArm(){
        loadLib(MAC_LIB_ARM, MAC_FORMAT);
    }
    private static void loadLinux(){
       loadLib(LINUX_LIB, LINUX_FORMAT);
    }

    private static void loadLib(String name, String extension) {
        try {
            final InputStream in = OpenCVLoader.class.getResourceAsStream(OPENCV + name + extension);
            final File fileOut = File.createTempFile("lib", extension);
            try (final OutputStream out = FileUtils.openOutputStream(fileOut)) {
                if (Objects.nonNull(in)) {
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
