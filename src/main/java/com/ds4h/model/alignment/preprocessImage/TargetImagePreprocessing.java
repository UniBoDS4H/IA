package com.ds4h.model.alignment.preprocessImage;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.alignment.manual.AffineAlignment;
import com.ds4h.model.alignment.manual.PerspectiveAlignment;
import com.ds4h.model.alignment.manual.RansacAlignment;
import com.ds4h.model.alignment.manual.TranslationalAlignment;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.ds4h.model.util.saveProject.SaveImages;
import ij.ImagePlus;
import org.opencv.core.*;
import org.opencv.core.Point;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TargetImagePreprocessing {

    private final static String DIRECTORY_NAME = "DS4H_processedTarget";
    private final static String TMP_PATH = System.getProperty("java.io.tmpdir");
    private TargetImagePreprocessing(){}
    static public ImagePoints process(final ImagePoints targetImage, final List<ImagePoints> imagesToAlign, final AlignmentAlgorithm algorithm) throws IllegalArgumentException{
        Pair<Mat, MatOfPoint2f> target = new Pair<>(targetImage.getMatImage(),targetImage.getMatOfPoint());
        for (final ImagePoints image : imagesToAlign) {
            target = TargetImagePreprocessing.singleProcess(target.getFirst(), target.getSecond(), targetImage, image, algorithm);
        }
        final String directoryName = DirectoryCreator.createTemporaryDirectory(TargetImagePreprocessing.DIRECTORY_NAME);
        final Optional<ImagePlus> imagePlus = ImagingConversion.fromMatToImagePlus(target.getFirst(),targetImage.getFile().getName());
        if(imagePlus.isPresent()){
            imagePlus.get().setTitle(targetImage.getFile().getName());
            SaveImages.save(imagePlus.get(), TargetImagePreprocessing.TMP_PATH + "/" + directoryName);
            final ImagePoints result = new ImagePoints(new File(TargetImagePreprocessing.TMP_PATH + "/" +directoryName+"/" + targetImage.getFile().getName()));
            target.getSecond().toList().forEach(result::addPoint);
            return result;
        }else{
            throw new IllegalArgumentException("the file doesn't exist");
        }

    }

    private static Pair<Mat,MatOfPoint2f> singleProcess(final Mat targetMat, final MatOfPoint2f targetPoints, final ImagePoints targetImage,  final ImagePoints imagePoints, final AlignmentAlgorithm algorithm) {
        try {
            final Mat imageToShiftMat = imagePoints.getMatImage();

            final Point[] srcArray = targetPoints.toArray();
            final Point[] dstArray = imagePoints.getMatOfPoint().toArray();
            final Mat translationMatrix = algorithm.getTransformationMatrix(imagePoints, targetImage);
            final int h1 = targetMat.rows();
            final int w1 = targetMat.cols();
            final int h2 = imageToShiftMat.rows();
            final int w2 = imageToShiftMat.cols();

            final MatOfPoint2f pts1 = new MatOfPoint2f(new Point(0, 0), new Point(0, h1), new Point(w1, h1), new Point(w1, 0));
            final MatOfPoint2f pts2 = new MatOfPoint2f(new Point(0, 0), new Point(0, h2), new Point(w2, h2), new Point(w2, 0));
            final MatOfPoint2f pts2_ = new MatOfPoint2f();

            algorithm.transform(pts2, pts2_, translationMatrix);

            pts2.toList().forEach(System.out::println);
            pts2_.toList().forEach(System.out::println);

            final MatOfPoint2f pts = new MatOfPoint2f();
            Core.hconcat(Arrays.asList(pts1, pts2_), pts);
            final Point pts_min = new Point(pts.toList().stream().map(p->p.x).min(Double::compareTo).get(), pts.toList().stream().map(p->p.y).min(Double::compareTo).get());
            final Point pts_max = new Point(pts.toList().stream().map(p->p.x).max(Double::compareTo).get(), pts.toList().stream().map(p->p.y).max(Double::compareTo).get());


            final int xmin = (int) Math.floor(pts_min.x - 0.5);
            final int ymin = (int) Math.floor(pts_min.y - 0.5);
            final int xmax = (int) Math.ceil(pts_max.x + 0.5);
            final int ymax = (int) Math.ceil(pts_max.y + 0.5);
            final double[] t = {-xmin, -ymin};
            System.out.println(-xmin + " " + -ymin);

            final Size s = new Size(xmax-xmin, ymax-ymin);
            final Mat alignedImage = Mat.zeros(s,imageToShiftMat.type());
            targetMat.copyTo(alignedImage.submat(new Rect((int) t[0], (int) t[1], w1, h1)));
            final MatOfPoint2f points = new MatOfPoint2f();
            points.fromList(targetPoints.toList().stream().map(p-> new Point(p.x+t[0], p.y+t[1])).collect(Collectors.toList()));
            return new Pair<>(alignedImage, points);
        }catch (Exception ex){
            throw ex;
        }
    }
}
