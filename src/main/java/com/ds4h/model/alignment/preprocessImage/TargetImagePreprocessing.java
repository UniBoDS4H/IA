package com.ds4h.model.alignment.preprocessImage;

import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.MemoryController;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.converter.MatImageProcessorConverter;
import ij.IJ;
import ij.process.ImageProcessor;
import org.opencv.core.*;
import org.opencv.core.Point;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TargetImagePreprocessing {


    private TargetImagePreprocessing(){}

    static public ImagePoints manualProcess(final ImagePoints targetImage, final List<ImagePoints> imagesToAlign, final AlignmentAlgorithm algorithm, final ImageProcessor ip) throws IllegalArgumentException{
        //ImagePoints target = new ImagePoints(targetImage.getMatImage(), targetImage.getName(), targetImage.getMatOfPoint());
        IJ.log("[MANUAL PREPROCESS] Starting manual process.");
        ImagePoints target = new ImagePoints(targetImage.getPath());
        final String title = targetImage.getTitle();
        target.addPoints(targetImage.getListPoints());
        for (final ImagePoints image : imagesToAlign) {
            final Pair<Mat, Point> res = TargetImagePreprocessing.singleProcess(target, image, algorithm);
            final MatOfPoint2f points = new MatOfPoint2f();
            points.fromList(target.getListPoints().stream().map(p-> new Point(p.x+res.getSecond().x, p.y+res.getSecond().y)).collect(Collectors.toList()));
            target = new ImagePoints(target.getTitle(), res.getFirst().clone());
            target.addPoints(points.toList());
        }
        IJ.log("[MANUAL PREPROCESS] Finish manual process.");
        IJ.log("[MANUAL PREPROCESS] Target Title: " + title);
        target.setProcessor(MatImageProcessorConverter.convert(target.getMatImage(), title, ip));
        return target;
    }

    static public ImageProcessor automaticProcess(final ImageProcessor ip, final Map<ImagePoints, ImagePoints> images, final AlignmentAlgorithm algorithm) throws IllegalArgumentException{
        final List<Map.Entry<ImagePoints, ImagePoints>> s = new ArrayList<>(images.entrySet());
        IJ.log("[AUTOMATIC PREPROCESS] Starting the automatic preprocess");
        final String title = s.get(0).getValue().getTitle();
        IntStream.range(0, s.size()).forEach(i -> {
            final Pair<Mat, Point> res = TargetImagePreprocessing.singleProcess(s.get(i).getValue(), s.get(i).getKey(), algorithm);
            IntStream.range(0, s.size()).forEach(j -> {
                ImagePoints target = s.get(j).getValue();
                List<Point> points = (target.getListPoints()
                        .stream()
                        .map(p-> new Point(p.x+res.getSecond().x, p.y+res.getSecond().y))
                        .collect(Collectors.toList()));
                IJ.log("[AUTOMATIC PREPROCESS] New Matrix : " + res.getFirst().toString());
                IJ.log("[AUTOMATIC PREPROCESS] New Matrix ADDR: " + res.getFirst().getNativeObjAddr());
                target.getMatImage().release();
                target = new ImagePoints(target.getTitle(), res.getFirst());
                target.setTitle(title);
                IJ.log("[AUTOMATIC PREPROCESS] Target Matrix: " + target.getMatImage().toString());
                IJ.log("[AUTOMATIC PREPROCESS] Target Title: " + target.getTitle());
                IJ.log("[AUTOMATIC PREPROCESS] Target ADDR: " + target.getMatImage().getNativeObjAddr());
                target.addPoints(points);
                points.clear();
                System.gc();
                s.set(j, new AbstractMap.SimpleEntry<>(s.get(j).getKey(), target));
            });
        });
        IJ.log("[AUTOMATIC PREPROCESS] Finish automatic preprocess");
        IJ.log("[AUTOMATIC PREPROCESS] Size: " + s.size());
        System.gc();
        images.clear();
        s.forEach(e->images.put(e.getKey(),e.getValue()));
        IJ.log("[AUTOMATIC PREPROCESS] Final Size: " + s.get(s.size()-1).getValue().getMatSize());
        return MatImageProcessorConverter.convert(s.get(s.size()-1).getValue().getMatImage(),
                s.get(s.size()-1).getValue().getTitle(), ip);
    }

    //returns the mat of the new target and the shift of the points
    private static Pair<Mat, Point> singleProcess(final ImagePoints target, final ImagePoints ImagePoints, final AlignmentAlgorithm algorithm) {
        final int h1 = target.getRows();
        final int w1 = target.getCols();
        final int h2 = ImagePoints.getRows();
        final int w2 = ImagePoints.getCols();
        IJ.log("[PREPROCESS] Target Rows: " + h1 + " Target Cols: " + w1);
        IJ.log("[PREPROCESS] ImageP Rows: " + h2 + " ImageP Cols: " + w2);
        final MatOfPoint2f pts1 = new MatOfPoint2f(new Point(0, 0), new Point(0, h1), new Point(w1, h1), new Point(w1, 0));
        final MatOfPoint2f pts2 = new MatOfPoint2f(new Point(0, 0), new Point(0, h2), new Point(w2, h2), new Point(w2, 0));
        final MatOfPoint2f pts2_ = new MatOfPoint2f();
        final Mat H = algorithm.getTransformationMatrix(ImagePoints.getMatOfPoint(), target.getMatOfPoint());
        for(int i = 0; i < H.rows(); i++){
            for(int j = 0; j < H.cols(); j++){
                System.out.print(Arrays.toString(H.get(i, j)));
            }
            System.out.println();
        }
        algorithm.transform(pts2, pts2_, H);
        final MatOfPoint2f pts = new MatOfPoint2f();
        System.out.println("pt1");
        pts1.toList().forEach(System.out::println);
        System.out.println("pt2");
        pts2_.toList().forEach(System.out::println);
        Core.hconcat(Arrays.asList(pts1, pts2_), pts);
        final Point pts_min = new Point(pts.toList().stream().map(p->p.x).min(Double::compareTo).get(), pts.toList().stream().map(p->p.y).min(Double::compareTo).get());
        final Point pts_max = new Point(pts.toList().stream().map(p->p.x).max(Double::compareTo).get(), pts.toList().stream().map(p->p.y).max(Double::compareTo).get());
        final int xmin = (int) Math.floor(pts_min.x - 0.5);
        final int ymin = (int) Math.floor(pts_min.y - 0.5);
        final int xmax = (int) Math.ceil(pts_max.x + 0.5);
        final int ymax = (int) Math.ceil(pts_max.y + 0.5);
        final double[] t = {-xmin, -ymin};
        final Size s = new Size(xmax-xmin, ymax-ymin);
        final Mat alignedImage = Mat.zeros(s, ImagePoints.getMatImage().type());
        IJ.log("[PREPROCESS] Before copy:  " + target.getMatImage());
        target.getMatImage().copyTo(alignedImage.submat(new Rect((int) t[0], (int) t[1], w1, h1)));
        IJ.log("[PREPROCESS] After copy: " + alignedImage);
        target.getMatImage().release();
        return new Pair<>(alignedImage, new Point(t[0], t[1]));
    }
}
