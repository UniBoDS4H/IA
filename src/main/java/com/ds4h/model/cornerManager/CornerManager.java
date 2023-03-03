package com.ds4h.model.cornerManager;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.CheckImage;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.twelvemonkeys.contrib.tiff.TIFFUtilities;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class CornerManager {
    private final List<ImagePoints> imagesWithPoints;
    private Optional<ImagePoints> sourceImage;
    public CornerManager(){
        this.sourceImage = Optional.empty();
        this.imagesWithPoints = new ArrayList<>();
    }
    /**
     * Splits all pages from the input TIFF file to one file per page in the
     * output directory.
     *
     * @param inputFile
     * @param outputDirectory
     * @return generated files
     * @throws IOException
     */
    private static List<File> split(File inputFile, File outputDirectory, String fileName) throws IOException {
        ImageInputStream input = null;
        List<File> outputFiles = new ArrayList<>();
        try {
            input = ImageIO.createImageInputStream(inputFile);
            List<TIFFUtilities.TIFFPage> pages = TIFFUtilities.getPages(input);
            int pageNo = 1;
            for (TIFFUtilities.TIFFPage tiffPage : pages) {
                ArrayList<TIFFUtilities.TIFFPage> outputPages = new ArrayList<TIFFUtilities.TIFFPage>(1);
                ImageOutputStream outputStream = null;
                try {
                    File outputFile = new File(outputDirectory, fileName + "_" + String.format("%04d", pageNo) + ".tif");
                    outputStream = ImageIO.createImageOutputStream(outputFile);
                    outputPages.clear();
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

    public void  load(List<String> loadingImages) throws IOException {
        loadingImages.stream().flatMap(path -> {
            try {
                //if we have a multipage tiff we split it into different files
                if(TIFFUtilities.getPages(ImageIO.createImageInputStream(new File(path))).size() != 1) {
                    String dir = DirectoryCreator.createTemporaryDirectory("images");
                    List<File> files = split(new File(path), new File(System.getProperty("java.io.tmpdir") + "/" + dir), FilenameUtils.removeExtension(new File(path).getName()));
                    return files.stream().map(File::getPath);
                }
                else {
                    return Stream.of(path);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })
        .map(File::new)
        .filter(CheckImage::checkImage)
        .map(ImagePoints::new)
        .filter(image -> !this.imagesWithPoints.contains(image))
        .forEach(this.imagesWithPoints::add);
    }
    /**
     *
     * @param loadingImages
     */
    public void loadImages(final List<String> loadingImages){
        if(Objects.nonNull(loadingImages) && !loadingImages.isEmpty()) {
            try {
                this.load(loadingImages);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (this.imagesWithPoints.size() > 0) {
                //setting the first image as default
                this.setAsSource(this.imagesWithPoints.get(0));
            } else {
                throw new IllegalArgumentException("Zero images found");
            }

        }else{
            throw new IllegalArgumentException("There are no input images, please pick some images from a path.");
        }
    }
    /**
     *
     * @param images
     */
    public void addImages(final List<ImagePoints> images){
        if(Objects.nonNull(images) && images.size() > 0) {
            this.imagesWithPoints.addAll(images);
            this.setAsSource(images.get(0));
        }
    }

    /**
     *
     * @param image
     */
    public void removeImage(final ImagePoints image){
        if(Objects.nonNull(image) && this.sourceImage.isPresent() && !this.sourceImage.get().equals(image)) {
            this.imagesWithPoints.removeIf(img -> img.equals(image));
        }
    }

    /**
     *
     */
    public void clearList(){
        this.imagesWithPoints.clear();
    }

    /**
     *
     * @return
     */
    public List<ImagePoints> getCornerImages(){
        return new ArrayList<>(this.imagesWithPoints);
    }

    /**
     *
     * @return
     */
    public List<ImagePoints> getImagesToAlign(){
        return this.sourceImage.map(imageCorners -> this.imagesWithPoints.stream().filter(im -> !im.equals(imageCorners)).collect(Collectors.toList())).orElseGet(() -> new LinkedList<>(this.imagesWithPoints));
    }

    /**
     *
     * @return
     */
    public Optional<ImagePoints> getSourceImage(){
        return this.sourceImage;
    }

    /**
     *
     * @param image
     */
    public void setAsSource(final ImagePoints image){
        if(Objects.nonNull(image) && this.imagesWithPoints.contains(image)){
            this.sourceImage = Optional.of(image);
        }else{
            throw new IllegalArgumentException("The given image was not fount among the loaded or the image input is NULL.");
        }
    }

    public void clearProject(){
        this.sourceImage = Optional.empty();
        this.imagesWithPoints.clear();
    }
}
