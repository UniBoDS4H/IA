package com.ds4h.model.util.opencvLoader;

import ij.IJ;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.*;
import java.util.Objects;

/**
 *
 */
public class OpenCVLoader {
    private static final String  OPENCV = "/opencv/";
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String WINDOWS = "windows", MAC = "mac", LINUX = "linux", ARM = "arm", ARMV = "armv";
    private static final String WINDOWS_FORMAT = ".dll", MAC_FORMAT = ".dylib", LINUX_FORMAT = ".so";
    private static final String TMP_LIBRARY_NAME = "DS4H_OpenCVLibrary";
    private final static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");
    private static final String WINDOWS_LIB = "opencv_java455",
            MAC_LIB_INTEL = "opencv_java455",
            MAC_LIB_ARM = "libopencv_arm_java455",
            LINUX_LIB = "libopencv_java455";
    private OpenCVLoader(){

    }

    public static void loadOpenCV(){
        if(OS.contains(WINDOWS)){
            OpenCVLoader.loadWindows();
        }else if(OS.contains(MAC)){
            final String arch = System.getProperty("os.arch").toLowerCase();
            if (arch.equals(ARM) || arch.startsWith(ARMV)) {
                OpenCVLoader.loadMacArm();
            } else {
                OpenCVLoader.loadMacIntel();
            }

        }else if(OS.contains(LINUX)){
            OpenCVLoader.loadLinux();
        }
    }
    private static void loadWindows(){
        OpenCVLoader.loadLib(WINDOWS_LIB, WINDOWS_FORMAT);
    }
    private static void loadMacIntel(){
        OpenCVLoader.loadLib(MAC_LIB_INTEL, MAC_FORMAT);
    }
    private static void loadMacArm(){
        OpenCVLoader.loadLib(MAC_LIB_ARM, MAC_FORMAT);
    }
    private static void loadLinux(){
       OpenCVLoader.loadLib(LINUX_LIB, LINUX_FORMAT);
    }

    private static void loadLib(final String name, final String extension) {
        try {
            if (!OpenCVLoader.alreadyLoaded()) {
                final InputStream in = OpenCVLoader.class.getResourceAsStream(OPENCV + name + extension);
                final File fileOut = File.createTempFile(TMP_LIBRARY_NAME, extension);
                try (final OutputStream out = FileUtils.openOutputStream(fileOut)) {
                    if (Objects.nonNull(in)) {
                        IOUtils.copy(in, out);
                        in.close();
                        out.close(); // Without this line it doesn't work on windows, so, just leave it there, avoid even the check for the OS
                        System.load(fileOut.toString());
                    }
                } catch (IOException e) {
                    IJ.showMessage(e.getMessage());
                }
            }
        } catch (IOException e) {
            IJ.showMessage(e.getMessage());
        }
    }
    private static boolean alreadyLoaded(){
        final File tmpDirectory = new File(TEMPORARY_PATH);
        if(tmpDirectory.isDirectory()){
            final File[] files = tmpDirectory.listFiles();
            if(Objects.nonNull(files)) {
                for (File file : files) {
                    if (file.isFile() && file.getName().contains(TMP_LIBRARY_NAME)) {
                        System.load(file.toString());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
