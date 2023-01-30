package com.ds4h.model.util.opencvLoader;

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
        final String file = PROJECT_DIRECTORY+SEPARATOR+SOURCE+SEPARATOR+MAIN+SEPARATOR+RESOURCE_DIRECTORY+SEPARATOR+OPENCV+SEPARATOR+LINUX_LIB;
        System.out.println(file);
        System.load(file);
    }
}
