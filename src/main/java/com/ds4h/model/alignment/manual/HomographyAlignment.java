package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.util.*;
import java.util.stream.Stream;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_ANYCOLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class HomographyAlignment extends AlignmentAlgorithm {


    public HomographyAlignment(){
        super();
    }

    /**
     * Manual alignment using the Homography alignment
     * @param source : the source image used as reference
     * @param  target : the target to align
     * @return : the list of all the images aligned to the source
     */
    @Override
    protected   Optional<ImagePlus> align(final ImageCorners source, final ImageCorners target){
     try {

            final Mat matReference = super.toGrayscale(Imgcodecs.imread(source.getPath(), IMREAD_ANYCOLOR));
            final Point[] pointReference = source.getCorners();
            // Compute the Homography alignment
            final Mat matDest = super.toGrayscale(Imgcodecs.imread(target.getPath(), IMREAD_ANYCOLOR));
            final MatOfPoint2f referencePoint = new MatOfPoint2f();
            referencePoint.fromArray(source.getCorners());
            final MatOfPoint2f targetPoint = new MatOfPoint2f();
            targetPoint.fromArray(target.getCorners());
            final Mat H = Imgproc.getAffineTransform(referencePoint, targetPoint);
            //Calib3d.findHomography(referencePoint, targetPoint, Calib3d.RANSAC, 5);
            final Mat warpedMat = new Mat();
            Imgproc.warpAffine(matDest, warpedMat, H, matReference.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);
            //Imgproc.warpPerspective(matReference, warpedMat, H, new Size(matDest.cols(), matDest.rows()));
            // Try to convert the image, if it is present add it in to the list.
            return this.convertToImage(target.getFile(), warpedMat);
        }catch (Exception ex){
            IJ.showMessage(ex.getMessage());
        }
        return Optional.empty();
         /*
        ImagePlus referenceImage = source.getImage();
        ImagePlus targetImage = target.getImage();
        Point[]referencePoints=source.getCorners();
        Point[]targetPoints=target.getCorners();
        System.out.println(targetPoints);
        int width = referenceImage.getWidth();
        int height = referenceImage.getHeight();

        // Calculate the average shift in x and y
        int xShift = 0;
        int yShift = 0;
        for (int i = 0; i < referencePoints.length; i++) {
            xShift += referencePoints[i].x - targetPoints[i].x;
            yShift += referencePoints[i].y - targetPoints[i].y;
        }
        xShift /= referencePoints.length;
        yShift /= referencePoints.length;

        // Shift the target image by the calculated x and y shift
        ByteProcessor shiftedProcessor = new ByteProcessor(width, height);

        ByteProcessor targetProcessor = (ByteProcessor) targetImage.getProcessor();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int targetX = x - xShift;
                int targetY = y - yShift;
                if (targetX >= 0 && targetX < width && targetY >= 0 && targetY < height) {
                    shiftedProcessor.set(x, y, targetProcessor.get(targetX, targetY));
                } else {
                    shiftedProcessor.set(x, y, 0);
                }
            }
        }
        */

        //return Optional.of(new ImagePlus("Aligned Image", shiftedProcessor));
    }

    @Override
    public List<ImagePlus> alignImages(final CornerManager cornerManager){
        /*
        Arrays.stream(cornerManager.getSourceImage().get().getCorners()).forEach(System.out::println);
        cornerManager.getCornerImagesImages().forEach(i->{
            Arrays.stream(i.getCorners()).forEach(System.out::println);
        });
        */
        List<ImagePlus> images = new LinkedList<>();
        if(Objects.nonNull(cornerManager) && cornerManager.getSourceImage().isPresent()) {
            final ImageCorners source = cornerManager.getSourceImage().get();
            cornerManager.getImagesToAlign().forEach(image ->
                    this.align(source, image).ifPresent(images::add)
            );
        }
        return images;
    }
}
