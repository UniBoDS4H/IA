package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.alignment.preprocessImage.TargetImagePreprocessing;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.Pair;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * This class is used for the alignment algorithms. Inside this class we can found all the methods to perform the alignment.
 * Every child class must implement the 'align' method.
 */
public abstract class AlignmentAlgorithm implements AlignmentAlgorithmInterface, Runnable{
    private final static int RGB = 3;
    private ImagePoints targetImage;
    private final List<ImagePoints> imagesToAlign;
    private final List<AlignedImage> alignedImages;
    private final Map<ImagePoints, Mat> trasformationMatrix;
    private Thread thread;

    protected AlignmentAlgorithm(){
        targetImage = null;
        this.thread = new Thread(this);
        this.imagesToAlign = new LinkedList<>();
        this.alignedImages = new CopyOnWriteArrayList<>();
        this.trasformationMatrix = new HashMap<>();
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

    protected void addMatrix(final ImagePoints key, final Mat matrix){
        if(Objects.nonNull(key) && Objects.nonNull(matrix)) {
            this.trasformationMatrix.putIfAbsent(key, matrix);
        }
    }

    protected void removeMatrix(final ImagePoints key){
        if(Objects.nonNull(key)) {
            this.trasformationMatrix.remove(key);
        }
    }

    public void clearMap(){
        this.trasformationMatrix.clear();
    }

    protected Mat traslationMatrix(final ImagePoints key){
        return this.trasformationMatrix.get(Objects.requireNonNull(key));
    }

    /**
     * This method must be overridden from all the children classes. Inside this method we have the
     * real implementation of the alignment algorithm.
     * @param targetImage the targetImage image for the alignment, this Object will be used in order to align the imagePoints.
     * @param imagePoints the image to align base to the targetImage object
     * @return the Optional aligned containing the final aligned image
     * @throws NoSuchMethodException in case this method is called from a child class without having the implementation of it
     */
    protected Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imagePoints) {
        throw new NotImplementedException();
    }


    /**
     * This method can be used inside the alignment in order to convert an RGB matrix in to a GrayScale matrix, because
     * some alignment can require GrayScale images so because of that It is necessary to convert the current Matrix.
     * @param mat the Matrix to convert from RGB to GrayScale
     * @return the new GrayScale matrix
     */
    protected Mat toGrayscale(final Mat mat) {
        Mat gray = new Mat();
        if (mat.channels() == RGB) {
            // Convert the BGR image to a single channel grayscale image
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            // If the image is already single channel, just return it
            return mat;
        }
        return gray;
    }

    /**
     * This method is used for the warping of the final Matrix, this warping is used in order to store the new aligned image.
     * @param source this is the original Matrix of the image to align
     * @param H the Homography Matrix
     * @param size the size of the TargetMatrix
     * @param alignedFile the name of the aligned file
     * @return an Optional where is stored the new Aligned Image
     */
    protected Optional<AlignedImage> warpMatrix(final Mat source, final Mat H, final Size size, final File alignedFile){
        final Mat alignedImage1 = new Mat();
        // Align the first image to the second image using the homography matrix
        Imgproc.warpPerspective(source, alignedImage1, H, size);
        final Optional<ImagePlus> finalImage = this.convertToImage(alignedFile, alignedImage1);
        return finalImage.map(imagePlus -> new AlignedImage(alignedImage1, H, imagePlus));
    }

    /**
     * When this method is called we start the real process of image alignment. If the thread is not alive and the
     * target image is present we can start the operation. Take in mind that the alignment is done inside a separate Thread,
     * so if you need the images you have to wait until the alignment is not done.
     * @param pointManager  container where all the images are stored with their points
     * @throws IllegalArgumentException If the targetImage is not present or if the cornerManager is null, an exception
     * is thrown.
     */
    @Override
    public void alignImages(final PointManager pointManager) throws IllegalArgumentException{
        if(Objects.nonNull(pointManager) && pointManager.getSourceImage().isPresent()) {
            if(!this.isAlive()) {
                this.targetImage = pointManager.getSourceImage().get();
                this.alignedImages.clear();
                this.imagesToAlign.clear();
                try {
                    this.imagesToAlign.addAll(pointManager.getImagesToAlign());
                    this.thread.start();
                } catch (final Exception ex) {
                    throw new IllegalArgumentException("Error: " + ex.getMessage());
                }
            }
        }else{
            throw new IllegalArgumentException("In order to do the alignment It is necessary to have a target," +
                    " please pick a target image.");
        }
    }

    /**
     * Inside this method we call the 'align' operation, It is strictly necessary that the 'align' method is present,
     * otherwise It will throw a 'NoSuchMethodException'. Each image is stored inside the 'alignedImages' collection.
     * In order to perform the alignment It is necessary that the targetImage is present.
     */
    @Override
    public void run(){
        try {
            if(Objects.nonNull(this.targetImage)) {
                if(this instanceof SurfAlignment){this.alignedImages.add(new AlignedImage(this.targetImage.getMatImage(), this.targetImage.getImage()));

                    this.imagesToAlign.parallelStream()
                            .forEach(img -> this.align(this.targetImage, img).ifPresent(this.alignedImages::add));
                    System.out.println("AA");
                }else {
                    final ImagePoints processedTarget = TargetImagePreprocessing.process(this.targetImage, this.imagesToAlign, this);
                    this.alignedImages.add(new AlignedImage(processedTarget.getMatImage(), processedTarget.getImage()));

                    this.imagesToAlign.parallelStream()
                            .forEach(img -> this.align(processedTarget, img).ifPresent(this.alignedImages::add));

                }
                /*
                final List<Callable<Optional<AlignedImage>>> alignmentTasks = imagesToAlign.stream().parallel()
                        .map(img -> (Callable<Optional<AlignedImage>>)() -> this.align(processedTarget, img))
                        .collect(Collectors.toList());
                ExecutorService executorService = Executors.newFixedThreadPool(imagesToAlign.size());
                try {
                    List<Future<Optional<AlignedImage>>> alignmentResults = executorService.invokeAll(alignmentTasks);
                    alignmentResults.stream().forEach(img -> {
                        try {
                            img.get().ifPresent(this.alignedImages::add);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (InterruptedException e) {
                    // handle exceptions
                } finally {
                    executorService.shutdown();
                }
                 */
            }
            this.thread = new Thread(this);
            this.clearMap();
        } catch (final Exception e) {
            this.thread = new Thread(this);
            this.clearMap();
            throw new RuntimeException(e);
        }
    }

    /**
     * With this method we return all the images aligned. If the thread is running an empty list is returned otherwise
     * this method returns all the images.
     * @return all the images aligned.
     */
    public List<AlignedImage> alignedImages(){
        return this.isAlive() ? Collections.emptyList() : new LinkedList<>(this.alignedImages);
    }

    @Override
    public Mat getTransformationMatrix(final ImagePoints imageToAlign, final ImagePoints targetImage){
        throw new NotImplementedException();
    }

    @Override
    public void transform(final Mat source, final Mat destination, final Mat H){
        throw new NotImplementedException();
    }
    /**
     * This method is called in order to have information about the alignment thread.
     * @return true If it is alive, false otherwise
     */
    public boolean isAlive(){
        return this.thread.isAlive();
    }

}
