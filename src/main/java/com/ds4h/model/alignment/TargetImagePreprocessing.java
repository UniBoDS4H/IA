package com.ds4h.model.alignment;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.manual.TranslationalAlignment;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import ij.IJ;
import ij.ImagePlus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import javax.swing.text.html.Option;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TargetImagePreprocessing {
    private TargetImagePreprocessing(){}
    static public ImagePoints process(final ImagePoints targetImage, final List<ImagePoints> imagesToAlign) throws IllegalArgumentException{
        Pair<Mat, MatOfPoint2f> target = new Pair<>(targetImage.getMatImage(),targetImage.getMatOfPoint());
        for (final ImagePoints image : imagesToAlign) {
            target = TargetImagePreprocessing.singleProcess(target.getFirst(), target.getSecond(), image);
        }
        String path = DirectoryCreator.createTemporaryDirectory("DS4H_processedTarget");
        Optional<ImagePlus> iP = ImagingConversion.fromMatToImagePlus(target.getFirst(),targetImage.getFile().getName());
        if(iP.isPresent()){
            IJ.save(iP.get(), System.getProperty("java.io.tmpdir") + "/" + path+"/"+ FilenameUtils.removeExtension(targetImage.getFile().getName()));
            ImagePoints result = new ImagePoints(new File(System.getProperty("java.io.tmpdir") + "/" +path+"/"+ targetImage.getFile().getName()));
            target.getSecond().toList().forEach(point->result.addPoint(point));
            return result;
        }else{
            throw new IllegalArgumentException("the file doesn't exist");
        }

    }

    private static Pair<Mat,MatOfPoint2f> singleProcess(Mat targetMat, MatOfPoint2f targetPoints, ImagePoints imagePoints) {
        try {
            final Mat imageToShiftMat = imagePoints.getMatImage();

            final Point[] srcArray = targetPoints.toArray();
            final Point[] dstArray = imagePoints.getMatOfPoint().toArray();
            Mat translationMatrix = TranslationalAlignment.getTransformationMatrix(dstArray, srcArray);

            int h1 = targetMat.rows();
            int w1 = targetMat.cols();
            int h2 = imageToShiftMat.rows();
            int w2 = imageToShiftMat.cols();

            MatOfPoint2f pts1 = new MatOfPoint2f(new Point(0, 0), new Point(0, h1), new Point(w1, h1), new Point(w1, 0));
            MatOfPoint2f pts2 = new MatOfPoint2f(new Point(0, 0), new Point(0, h2), new Point(w2, h2), new Point(w2, 0));
            MatOfPoint2f pts2_ = new MatOfPoint2f();
            Core.perspectiveTransform(pts2, pts2_, translationMatrix);

            MatOfPoint2f pts = new MatOfPoint2f();
            Core.hconcat(Arrays.asList(pts1, pts2_), pts);
            Point pts_min = new Point(pts.toList().stream().map(p->p.x).min(Double::compareTo).get(), pts.toList().stream().map(p->p.y).min(Double::compareTo).get());
            Point pts_max = new Point(pts.toList().stream().map(p->p.x).max(Double::compareTo).get(), pts.toList().stream().map(p->p.y).max(Double::compareTo).get());

            int xmin = (int) Math.floor(pts_min.x - 0.5);
            int ymin = (int) Math.floor(pts_min.y - 0.5);
            int xmax = (int) Math.ceil(pts_max.x + 0.5);
            int ymax = (int) Math.ceil(pts_max.y + 0.5);
            double[] t = {-xmin, -ymin};
            Mat Ht = Mat.eye(3, 3, CvType.CV_32FC1);

            Ht.put(0, 2, t[0]);
            Ht.put(1, 2, t[1]);
            Mat s = new Mat();
            Core.multiply(Ht,translationMatrix, s);

            Mat alignedImage = new Mat();
            Imgproc.warpPerspective(imageToShiftMat, alignedImage, Ht.mul(translationMatrix), new Size(xmax-xmin, ymax-ymin));
            targetMat.copyTo(alignedImage.submat(new Rect((int) t[0], (int) t[1], w1, h1)));
            MatOfPoint2f points = new MatOfPoint2f();
            points.fromList(targetPoints.toList().stream().map(p-> new Point(p.x+t[0], p.y+t[1])).collect(Collectors.toList()));
            return new Pair<>(alignedImage, points);
        }catch (Exception ex){
            throw ex;
        }
    }
}
