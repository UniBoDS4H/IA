package com.ds4h.model.util;


import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.twelvemonkeys.contrib.tiff.TIFFUtilities;
import ij.IJ;
import ij.ImagePlus;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2RGB;

public class ImagingConversion {

    private static final String TMP_DIRECTORY_NAME = "DS4H_Images";
    private static final String TMP_DIRECTORY_NAME_MAT = "DS4H_ImagesMat";
    private static final String TMP_DIRECTORY = System.getProperty("java.io.tmpdir");

    /**
     *
     */
    private ImagingConversion(){}

    /**
     *
     * @param paths a
     * @return b
     * @throws IOException c
     */
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

    /**
     *
     * @param path a
     * @return b
     */
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

    /**
     *
     * @param matrix a
     * @return b
     */
    public static Mat fromGray2Rgb(final Mat matrix){

        final Mat rgb = new Mat();
        Imgproc.cvtColor(Objects.requireNonNull(matrix), rgb, COLOR_GRAY2RGB);
        return rgb;
    }

    /**
     *
     * @param imagePlus a
     * @return b
     */
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
