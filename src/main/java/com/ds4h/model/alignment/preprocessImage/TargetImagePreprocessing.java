package com.ds4h.model.alignment.preprocessImage;

import com.ds4h.model.alignment.alignmentAlgorithm.TranslationalAlignment;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.Pair;
import org.opencv.core.*;
import org.opencv.core.Point;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TargetImagePreprocessing {
    static public ImagePoints manualProcess(final ImagePoints targetImage, final List<ImagePoints> imagesToAlign, final TranslationalAlignment algorithm) throws IllegalArgumentException{
        ImagePoints target = new ImagePoints(targetImage.getMatImage(), targetImage.getName(), targetImage.getMatOfPoint());
        for (final ImagePoints image : imagesToAlign) {
            Pair<Mat, Point> res = TargetImagePreprocessing.singleProcess(target, image, algorithm);
            System.out.println("TRASLATION: " + res.getSecond());
            MatOfPoint2f points = new MatOfPoint2f();
            points.fromList(target.getListPoints().stream().map(p-> new Point(p.x+res.getSecond().x, p.y+res.getSecond().y)).collect(Collectors.toList()));
            target = new ImagePoints(res.getFirst(),target.getName(),  points);

        }
        return target;
    }

    static public ImagePoints automaticProcess(Map<ImagePoints,ImagePoints> images, final TranslationalAlignment algorithm) throws IllegalArgumentException{
       // ImagePoints target = new ImagePoints(targetImage.getMatImage(), targetImage.getName(), targetImage.getMatOfPoint());
        List<Map.Entry<ImagePoints, ImagePoints>> s = new ArrayList<>(images.entrySet());
        for(int i = 0; i < s.size(); i++){
            Pair<Mat, Point> res = TargetImagePreprocessing.singleProcess(s.get(i).getValue(), s.get(i).getKey(), algorithm);
            System.out.println("TRASLATION: " + res.getSecond());
            for (int j = 0; j < s.size(); j++){
                ImagePoints target = s.get(j).getValue();
                ImagePoints img = s.get(j).getKey();
                MatOfPoint2f points = new MatOfPoint2f();
                points.fromList(target.getListPoints().stream().map(p-> new Point(p.x+res.getSecond().x, p.y+res.getSecond().y)).collect(Collectors.toList()));
                target = new ImagePoints(res.getFirst(),target.getName(), points);

                //target.getImage().show();
                s.set(j, new AbstractMap.SimpleEntry<>(img,target));
            }

        }
        images.clear();
        s.stream().forEach(e->images.put(e.getKey(),e.getValue()));

        ImagePoints a = s.get(s.size()-1).getValue();
        //a.getImage().show();
        return a;
    }

    //returns the mat of the new target and the shift of the points
    private static Pair<Mat, Point> singleProcess(final ImagePoints target, final ImagePoints imagePoints, final TranslationalAlignment algorithm) {
        //target.getImage().show();
        //imagePoints.getImage().show();
        final Mat imageToShiftMat = imagePoints.getMatImage();
        final Mat targetMat = target.getMatImage();
        final Mat translationMatrix = algorithm.getTransformationMatrix(imagePoints.getMatOfPoint(), target.getMatOfPoint());
        //final Mat translationMatrix = Calib3d.findHomography(imagePoints.getMatOfPoint(), )
        System.out.println("T1");
        printMat(translationMatrix);

        final int h1 = targetMat.rows();
        final int w1 = targetMat.cols();
        final int h2 = imageToShiftMat.rows();
        final int w2 = imageToShiftMat.cols();

        final MatOfPoint2f pts1 = new MatOfPoint2f(new Point(0, 0), new Point(0, h1), new Point(w1, h1), new Point(w1, 0));
        final MatOfPoint2f pts2 = new MatOfPoint2f(new Point(0, 0), new Point(0, h2), new Point(w2, h2), new Point(w2, 0));
        final MatOfPoint2f pts2_ = new MatOfPoint2f();

        algorithm.transform(pts2, pts2_, translationMatrix,target.numberOfPoints());


        final MatOfPoint2f pts = new MatOfPoint2f();
        Core.hconcat(Arrays.asList(pts1, pts2_), pts);
        final Point pts_min = new Point(pts.toList().stream().map(p->p.x).min(Double::compareTo).get(), pts.toList().stream().map(p->p.y).min(Double::compareTo).get());
        final Point pts_max = new Point(pts.toList().stream().map(p->p.x).max(Double::compareTo).get(), pts.toList().stream().map(p->p.y).max(Double::compareTo).get());

        final int xmin = (int) Math.floor(pts_min.x - 0.5);
        final int ymin = (int) Math.floor(pts_min.y - 0.5);
        final int xmax = (int) Math.ceil(pts_max.x + 0.5);
        final int ymax = (int) Math.ceil(pts_max.y + 0.5);
        final double[] t = {-xmin, -ymin};

        final Size s = new Size(xmax-xmin, ymax-ymin);
        final Mat alignedImage = Mat.zeros(s,imageToShiftMat.type());
        targetMat.copyTo(alignedImage.submat(new Rect((int) t[0], (int) t[1], w1, h1)));
        return new Pair(alignedImage, new Point(t[0], t[1]));
        /*final MatOfPoint2f points = new MatOfPoint2f();
        points.fromList(Arrays.stream(target.getPoints()).map(p-> new Point(p.x+t[0], p.y+t[1])).collect(Collectors.toList()));
        return new ImagePoints(alignedImage, target.getName(), points);

         */
    }

    public static void printMat(Mat translationMatrix) {
        System.out.println();
        for(int i = 0; i < translationMatrix.rows(); i++){
            for(int j = 0; j < translationMatrix.cols(); j++){
                System.out.print(Arrays.toString(translationMatrix.get(i, j)));
            }
            System.out.println();
        }
        System.out.println();
    }

}
