package com.ds4h.model.util;


import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.twelvemonkeys.contrib.tiff.TIFFUtilities;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.opencv.imgproc.Imgproc.COLOR_GRAY2RGB;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class ImagingConversion {

    private static final String TMP_DIRECTORY_NAME = "DS4H_Images";
    private static final String TMP_DIRECTORY_NAME_MAT = "DS4H_ImagesMat";
    private static final String TMP_DIRECTORY = System.getProperty("java.io.tmpdir");
    private ImagingConversion(){}

    public static Optional<ImagePlus> fromMatToImagePlus(final Mat matrix, final String fileName){
        try {
            if (!matrix.empty() && !fileName.isEmpty()) {
                //final String imgFinalName = new NameBuilder().parseName(fileName).splitBy().getFinalName();
                final ImagePlus imp = new ImagePlus(fileName, HighGui.toBufferedImage(matrix));
                return Optional.of(imp);
            }
        }catch (Exception e){
            IJ.showMessage(e.getMessage());
        }
        return Optional.empty();
    }

    public static List<File> fromPath(final List<String> paths) throws IOException{
        return paths.stream().map(File::new)
                .filter(File::isFile)
                .filter(CheckImage::checkImage)
                .flatMap(file -> {
                    try{
                        return ImagingConversion.isMulti(file);
                    }catch (final IOException exception){
                        return Stream.empty();
                    }
                })
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
    public static Mat getMatFromImagePlus(ImagePlus img){
        BufferedImage bimg = img.getBufferedImage();
        Mat mat = new Mat(bimg.getHeight(), bimg.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bimg.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        Core.flip(mat, mat, 0);
        return mat;
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
