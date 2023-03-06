package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.Pair;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public abstract class PointDetector {
    protected final List<KeyPoint> keypoints1List, keypoints2List;
    protected final List<DMatch> matchesList;
    protected final List<Point> points1, points2;
    //K, T-I
    protected final Map<ImagePoints, Pair<ImagePoints, ImagePoints>> goodMatches;
    public PointDetector(){
        this.keypoints1List = new ArrayList<>();
        this.keypoints2List = new ArrayList<>();
        this.matchesList = new ArrayList<>();
        this.points1 = new ArrayList<>();
        this.points2 = new ArrayList<>();
        this.goodMatches = new HashMap<>();
    }

    public abstract void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint);
    public abstract void matchPoint();

    public List<Point> getPoints1(){
        return this.points1;
    }

    public List<Point> getPoints2(){
        return this.points2;
    }

    public Optional<Pair<ImagePoints, ImagePoints>> getMatch(final ImagePoints key){
        return this.goodMatches.containsKey(key) ? Optional.of(this.goodMatches.get(key)) : Optional.empty();
    }

    public void putMatch(final ImagePoints key, final ImagePoints target, final ImagePoints newImagePoint){
        //TODO: add check
        this.goodMatches.put(key, new Pair<>(target, newImagePoint));
    }

    protected Mat toGrayscale(final Mat mat) {
        Mat gray = new Mat();
        if (mat.channels() == 3) {
            // Convert the BGR image to a single channel grayscale image
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            // If the image is already single channel, just return it
            return mat;
        }
        return gray;
    }
}
