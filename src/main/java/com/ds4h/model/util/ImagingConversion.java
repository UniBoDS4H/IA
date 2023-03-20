package com.ds4h.model.util;


import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.ds4h.model.util.saveProject.SaveImages;
import com.twelvemonkeys.contrib.tiff.TIFFUtilities;
import ij.IJ;
import ij.ImagePlus;
import ij.process.*;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.ShortPointer;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.ImageWindow;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.opencv.imgproc.Imgproc.COLOR_GRAY2RGB;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class ImagingConversion {

    private static final String TMP_DIRECTORY_NAME = "DS4H_Images";
    private static final String TMP_DIRECTORY_NAME_MAT = "DS4H_ImagesMat";
    private static final String TMP_DIRECTORY = System.getProperty("java.io.tmpdir");
    private ImagingConversion(){}

    private static ColorProcessor makeColorProcessor(final Mat matrix, final int width, final int height, final ColorModel color){
        IJ.log("[MAKE COLORPROCESSOR] Creating the ColorProcessor");
        final ColorProcessor cp = new ColorProcessor(width, height);
        IntStream.range(0, width).parallel().forEach(col -> {
            IntStream.range(0, height).parallel().forEach(row -> {
                double[] pixelValues = matrix.get(row, col); // read pixel values from the Mat object
                cp.set(col, row,
                        new Color(color.getRed((int)pixelValues[2]),
                        color.getGreen((int) pixelValues[1]),
                        color.getBlue((int) (pixelValues[0])))
                        .getRGB());
            });
        });
        System.gc();
        IJ.log("[MAKE COLORPROCESSOR] The creation is done");
        return cp;
    }

    private static ByteProcessor makeByteProcessor(final Mat matrix, final int width, final int height){
        IJ.log("[MAKE BYTEPROCESSOR] Creating ByteProcessor");
        final ByteProcessor ip = new ByteProcessor(width, height);
        IntStream.range(0, width).parallel().forEach(col -> {
            IntStream.range(0, height).parallel().forEach(row -> {
                ip.putPixelValue(col, row, matrix.get(row, col)[0]);
            });
        });
        System.gc();
        IJ.log("[MAKE BYTEPROCESSOR] Finish creation ByteProcessor");
        return ip;
    }

    public static ImagePlus matToImagePlus(final Mat matrix, final String fileName, final ImageProcessor ip){
        if(!matrix.empty() && !fileName.isEmpty()){
            final ImagePlus finalImage = new ImagePlus(fileName);
            if(matrix.type() == CvType.CV_8UC3){
                return new ImagePlus(fileName, ImagingConversion.makeColorProcessor(matrix, matrix.cols(), matrix.rows(), ip.getColorModel()));
            }else if(matrix.type() == CvType.CV_8UC1){
                finalImage.setProcessor(ImagingConversion.makeByteProcessor(matrix, matrix.cols(), matrix.rows()));
            }else{
                throw new IllegalArgumentException("This program do not support your type of image.");
            }
            return finalImage;
        }else{
            throw new IllegalArgumentException("One of the argument is empty. Please check again the values");
        }
    }


    @Deprecated
    public static Optional<ImagePlus> fromMatToImagePlus(final Mat matrix, final String fileName){
        try {
            if (!matrix.empty() && !fileName.isEmpty()) {
                IJ.log("Saving the matrix: " + matrix);
                final int totalR = matrix.rows(), totalC = matrix.cols();
                if(matrix.type() == CvType.CV_8UC3){
                    IJ.log("Is a ColorProcessor");
                    IJ.log("Created the processor" + "Rows: " + totalR + " Cols: " + totalC);
                    //TODO: FIX THIS
                    final ImagePlus imp = new ImagePlus(fileName, makeColorProcessor(matrix, totalC, totalR, null));
                    imp.show();
                    IJ.log("Created the ImagePlus");
                    return Optional.of(imp);
                }else{
                    ByteProcessor ip = new ByteProcessor(totalC, totalR);
                    for(int col = 0; col < totalC; col++){
                        for(int row = 0; row < totalR; row++){
                            ip.putPixelValue(col, row, matrix.get(row, col)[0]);
                        }
                    }
                    IJ.log("Created the processor" + "Rows: " + totalR + " Cols: " + totalC);
                    IJ.log("Done");
                    final ImagePlus imp = new ImagePlus(fileName, ip);

                    imp.show();
                    IJ.log("Created the ImagePlus");
                    return Optional.of(imp);
                }


            }
        }catch (Exception e){
            IJ.showMessage(e.getMessage());
        }
        return Optional.empty();
    }

    public static List<ImagePoints> fromPath(final List<File> paths) throws IOException{
        return paths.parallelStream()
                .filter(File::isFile)
                .filter(CheckImage::checkImage)
                .flatMap(file -> {
                    try{
                        return ImagingConversion.isMulti(file);
                    }catch (final IOException exception){
                        return Stream.empty();
                    }
                })
                .map(File::getPath)
                .map(ImagePoints::new)
                .collect(Collectors.toList());
    }

    private static Stream<File> isMulti(final File file) throws IOException {
        if (CheckImage.isTiff(file) && TIFFUtilities.getPages(ImageIO.createImageInputStream(file)).size() != 1) {
            final String dir = DirectoryCreator.createTemporaryDirectory(ImagingConversion.TMP_DIRECTORY_NAME);
            final List<File> files = ImagingConversion.split(file, new File( ImagingConversion.TMP_DIRECTORY+ "/" + dir),
                    FilenameUtils.removeExtension(file.getName()));
            return files.stream();
        } else {
            return Stream.of(file);
        }
    }

    private static List<File> split(final File inputFile, final File outputDirectory, final String fileName) throws IOException {
        ImageInputStream input = null;
        final List<File> outputFiles = new ArrayList<>();
        try {
            input = ImageIO.createImageInputStream(inputFile);
            final List<TIFFUtilities.TIFFPage> pages = TIFFUtilities.getPages(input);
            int pageNo = 1;
            for (final TIFFUtilities.TIFFPage tiffPage : pages) {
                final ArrayList<TIFFUtilities.TIFFPage> outputPages = new ArrayList<TIFFUtilities.TIFFPage>(1);
                ImageOutputStream outputStream = null;
                try {
                    final File outputFile = new File(outputDirectory, fileName + "_" + String.format("%04d", pageNo) + ".tif");
                    outputStream = ImageIO.createImageOutputStream(outputFile);
                    //outputPages.clear();
                    outputPages.add(tiffPage);
                    TIFFUtilities.writePages(outputStream, outputPages);
                    outputFiles.add(outputFile);
                }
                finally {
                    if (outputStream != null) {
                        outputStream.flush();
                        outputStream.close();
                    }
                }
                ++pageNo;
            }
        }
        finally {
            if (input != null) {
                input.close();
            }
        }
        return outputFiles;
    }

    public static Optional<ImagePlus> fromSinglePathToImagePlus(final String path){
        try {
            if(Objects.nonNull(path)) {
                return !path.isEmpty() ? Optional.of(IJ.openImage(path)) : Optional.empty();
            }
        }catch(Exception e){
            IJ.showMessage("An error occurred with this file : " + path + ". Are you sure that is correct ?");
        }
        return Optional.empty();
    }

    public static Mat fromGray2Rgb(final Mat matrix){
        final Mat rgb = new Mat();
        Imgproc.cvtColor(matrix, rgb, COLOR_GRAY2RGB);
        return rgb;
    }

    public static Optional<Mat> fromImagePlus2Mat(final ImagePlus imagePlus){
        try{
            final String dir = DirectoryCreator.createTemporaryDirectory(ImagingConversion.TMP_DIRECTORY_NAME_MAT);
            final String path = ImagingConversion.TMP_DIRECTORY+ "/"+dir+"/" + "Converted"+imagePlus.getTitle();
            IJ.save(imagePlus, path);
            return Optional.of(Imgcodecs.imread(path));
        } catch (Exception e) {
            return Optional.empty();
        }
    }




}
