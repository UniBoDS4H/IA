package com.ds4h.model.util.opencvManager.opencvLoader;

import ij.IJ;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.opencv.core.Core;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class OpenCVLoader {
    private static final String  OPENCV = "/opencv/";
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String WINDOWS = "windows", MAC = "mac", LINUX = "linux", ARM = "arm", ARMV = "armv";
    private static final String WINDOWS_FORMAT = ".dll", MAC_FORMAT = ".dylib", LINUX_FORMAT = ".so";
    public static final String TMP_LIBRARY_NAME = "DS4H_OpenCVLibrary";
    private final static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir");
    private static final String WINDOWS_LIB = "opencv_java455",
            MAC_LIB_INTEL = "opencv_java455",
            MAC_LIB_ARM = "libopencv_arm_java455",
            LINUX_LIB = "libopencv_java455";
    private OpenCVLoader(){

    }

    /**
     * Load the OpenCV library inside the TMP folder. The library is selected from the current OS.
     */
    public static void loadOpenCV(){
        if(OS.contains(WINDOWS)){
            OpenCVLoader.loadWindows();
        }else if(OS.contains(MAC)){
            final String arch = System.getProperty("os.arch").toLowerCase();
            if (arch.equals(ARM) || arch.startsWith(ARMV)) {
                OpenCVLoader.loadMacArm();
            } else {
                try {
                    OpenCVLoader.loadMacIntel();
                }catch (IOException a){

                }
            }

        }else if(OS.contains(LINUX)){
            try {
                OpenCVLoader.loadLinux();
            }catch (IOException a){
                IJ.log("UEILA " + a.getMessage());
            }
        }
    }
    private static void loadWindows(){
        OpenCVLoader.loadLib(WINDOWS_LIB, WINDOWS_FORMAT);
    }
    private static void loadMacIntel()  throws IOException{

        String sourcePath = "/opencv/mac_lib"; // La tua directory
        List<String> fileList = new ArrayList<>();
        //Purtroppo non riesco a capire perche non legga i file dal jar con il metodo qua sotto,
        //per ora siamo costretti a forzare tutti i file a mano.
        fileList.add("libopencv_alphamat.4.7.0.dylib");
        fileList.add("libopencv_aruco.4.7.0.dylib");
        fileList.add("libopencv_barcode.4.7.0.dylib");
        fileList.add("libopencv_bgsegm.4.7.0.dylib");
        fileList.add("libopencv_bioinspired.4.7.0.dylib");
        fileList.add("libopencv_calib3d.4.7.0.dylib");
        fileList.add("libopencv_ccalib.4.7.0.dylib");
        fileList.add("libopencv_core.4.7.0.dylib");
        fileList.add("libopencv_datasets.4.7.0.dylib");
        fileList.add("libopencv_dnn.4.7.0.dylib");
        fileList.add("libopencv_dnn_objdetect.4.7.0.dylib");
        fileList.add("libopencv_dnn_superres.4.7.0.dylib");
        fileList.add("libopencv_dpm.4.7.0.dylib");
        fileList.add("libopencv_face.4.7.0.dylib");
        fileList.add("libopencv_features2d.4.7.0.dylib");
        fileList.add("libopencv_flann.4.7.0.dylib");
        fileList.add("libopencv_freetype.4.7.0.dylib");
        fileList.add("libopencv_fuzzy.4.7.0.dylib");
        fileList.add("libopencv_gapi.4.7.0.dylib");
        fileList.add("libopencv_hfs.4.7.0.dylib");
        fileList.add("libopencv_highgui.4.7.0.dylib");
        fileList.add("libopencv_img_hash.4.7.0.dylib");
        fileList.add("libopencv_imgcodecs.4.7.0.dylib");
        fileList.add("libopencv_imgproc.4.7.0.dylib");
        fileList.add("libopencv_intensity_transform.4.7.0.dylib");
        fileList.add("libopencv_java470.dylib");
        fileList.add("libopencv_line_descriptor.4.7.0.dylib");
        fileList.add("libopencv_mcc.4.7.0.dylib");
        fileList.add("libopencv_ml.4.7.0.dylib");
        fileList.add("libopencv_objdetect.4.7.0.dylib");
        fileList.add("libopencv_optflow.4.7.0.dylib");
        fileList.add("libopencv_phase_unwrapping.4.7.0.dylib");
        fileList.add("libopencv_photo.4.7.0.dylib");
        fileList.add("libopencv_plot.4.7.0.dylib");
        fileList.add("libopencv_quality.4.7.0.dylib");
        fileList.add("libopencv_rapid.4.7.0.dylib");
        fileList.add("libopencv_reg.4.7.0.dylib");
        fileList.add("libopencv_rgbd.4.7.0.dylib");
        fileList.add("libopencv_saliency.4.7.0.dylib");
        fileList.add("libopencv_sfm.4.7.0.dylib");
        fileList.add("libopencv_shape.4.7.0.dylib");
        fileList.add("libopencv_stereo.4.7.0.dylib");
        fileList.add("libopencv_stitching.4.7.0.dylib");
        fileList.add("libopencv_structured_light.4.7.0.dylib");
        fileList.add("libopencv_superres.4.7.0.dylib");
        fileList.add("libopencv_surface_matching.4.7.0.dylib");
        fileList.add("libopencv_text.4.7.0.dylib");
        fileList.add("libopencv_tracking.4.7.0.dylib");
        fileList.add("libopencv_video.4.7.0.dylib");
        fileList.add("libopencv_videoio.4.7.0.dylib");
        fileList.add("libopencv_videostab.4.7.0.dylib");
        fileList.add("libopencv_viz.4.7.0.dylib");
        fileList.add("libopencv_wechat_qrcode.4.7.0.dylib");
        fileList.add("libopencv_xfeatures2d.4.7.0.dylib");
        fileList.add("libopencv_ximgproc.4.7.0.dylib");
        fileList.add("libopencv_xobjdetect.4.7.0.dylib");
        fileList.add("libopencv_xphoto.4.7.0.dylib");


        Path tmpDir = Files.createTempDirectory("DS4HOpenCV_MAC");
        IJ.log(fileList.toString());
        // Copy the files from the Resources directory to the TMP directory
        for (final String file : fileList) {
            try (InputStream inputStream = OpenCVLoader.class.getResourceAsStream(sourcePath + "/" + file)) {
                IJ.log("file name: " + tmpDir.resolve(file));
                Files.copy(inputStream, tmpDir.resolve(file), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        System.setProperty("java.library.path", tmpDir.toString());
        System.out.println(tmpDir.toString());
        System.out.println(System.getProperty("java.library.path"));
        //System.loadLibrary("libopencv_java470.dylib");
        System.load(tmpDir.toString() + "/libopencv_java470.dylib");
    }
    private static void loadMacArm(){
        OpenCVLoader.loadLib(MAC_LIB_ARM, MAC_FORMAT);
    }
    private static void loadLinux() throws IOException {
        OpenCVLoader.loadLib(LINUX_LIB, LINUX_FORMAT);
    }

    private static List<String> getResourceFiles(final String path) throws IOException {
        List<String> fileList = new ArrayList<>();
        IJ.log("My Path: " + path);
        try (InputStream inputStream = OpenCVLoader.class.getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            IJ.log("QUA DENTRO");
            while ((line = reader.readLine()) != null) {
                IJ.log(line);
                fileList.add(line);
            }
        }
        return fileList;
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
