package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.NameBuilder;
import com.ds4h.model.util.Pair;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is used for the alignment algorithms. Inside this class we can found all the methods to perform the alignment.
 * Every child class must implement the 'align' method.
 */
public abstract class AlignmentAlgorithm implements AlignmentAlgorithmInterface, Runnable{
    private final static int RGB = 3;
    private Optional<ImageCorners> source;
    private final List<ImageCorners> imagesToAlign;
    private final List<AlignedImage> imagesAligned;
    private Thread thread;
    protected AlignmentAlgorithm(){
        source = Optional.empty();

        this.imagesToAlign = new LinkedList<>();
        this.imagesAligned = new CopyOnWriteArrayList<>();
    }
    /**
     * Convert the new matrix in to an image
     * @param file : this will be used in order to get the name and store used it for the creation of the new file.
     * @param matrix : the image aligned matrix
     * @return : the new image created by the Matrix.
     */
    protected Optional<ImagePlus> convertToImage(final File file, final Mat matrix){
        return ImagingConversion.fromMatToImagePlus(matrix, file.getName());
    }

    protected Optional<AlignedImage> align(final ImageCorners source, final ImageCorners target) throws NoSuchMethodException {
        throw new NoSuchMethodException("Method not implemented");
    }

    protected Mat toGrayscale(final Mat mat) {
        Mat gray = new Mat();
        if (mat.channels() == RGB) {
            // Convert the BGR image to a single channel grayscale image
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            // If the image is already single channel, just return it
            gray = mat;
        }
        return gray;
    }

    protected Optional<AlignedImage> warpMatrix(final Mat source, final Mat destination, final Mat H, final Size size, final File targetImage){
        final Mat alignedImage1 = new Mat();
        // Align the first image to the second image using the homography matrix
        Imgproc.warpPerspective(source, alignedImage1, H, destination.size());
        final Optional<ImagePlus> finalImage = this.convertToImage(targetImage, alignedImage1);
        return finalImage.map(imagePlus -> new AlignedImage(alignedImage1, H, imagePlus));
    }

    /**
     * Align the images stored inside the cornerManager. All the images will be aligned to the source image
     * @param cornerManager : container where all the images are stored
     * @throws IllegalArgumentException:
     * @return the List of all the images aligned to the source
     */
    @Override
    public List<AlignedImage> alignImages(final CornerManager cornerManager) throws IllegalArgumentException{
        if(Objects.nonNull(cornerManager) && cornerManager.getSourceImage().isPresent()) {
            this.source = cornerManager.getSourceImage();
            this.imagesAligned.add(new AlignedImage(this.source.get().getMatImage(), this.source.get().getImage()));
            try {
                this.imagesToAlign.addAll(cornerManager.getImagesToAlign());
                this.thread = new Thread(this);
                this.thread.start();
                return this.imagesAligned;
            }catch (final Exception ex){
                throw new IllegalArgumentException("Error: " + ex.getMessage());
            }
        }else{
            throw new IllegalArgumentException("In order to do the alignment It is necessary to have a target," +
                    " please pick a target image.");
        }
    }

    @Override
    public void run(){
        try {
            if(this.source.isPresent()) {
                for (ImageCorners image : this.imagesToAlign) {
                    final Optional<AlignedImage> output = this.align(this.source.get(), image);
                    output.ifPresent(this.imagesAligned::add);
                }
            }
            //System.out.print(this.imagesAligned.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<AlignedImage> alignedImages(){
        return new LinkedList<>(this.imagesAligned);
    }

    public boolean isAlive(){
        return Objects.nonNull(this.thread) && this.thread.isAlive();
    }

    /**
     * Number of needed points in order to perform the alignment algorithm.
     * @return an integer showing how many points the algorithm needs for the alignment.
     * @throws NoSuchMethodException in case this method is not override.
     */
    public int neededPoints() throws NoSuchMethodException{
        throw new NoSuchMethodException("Not implemented");
    }

}
