package com.ds4h.model.imagePoints;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;
import java.util.List;

/**
 *
 */
public class ImagePoints extends ImagePlus{
    private final List<Point> pointList;
    private final String path;
    private int rows, cols;
    private int type = 0;
    private boolean RBG = false;
    private long address=-1;
    private Mat matrix = null;
    public ImagePoints(final String path){
        super(Objects.requireNonNull(path));
        this.path = path;
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
        //this.detectType();
        this.type = CvType.CV_8U;
    }

    public ImagePoints(final String path, final int rows, final int cols, final int type, final long matAddress){
        this(path);
        this.rows = rows;
        this.cols = cols;
        this.type = type;
        this.address = matAddress;
        IJ.log("New ImagePoints --> Rows: " + this.rows + " Cols: "+ this.cols + " Address: " + this.address);
    }
    public ImagePoints(final String path, final Mat mat){
        this(path);
        matrix = mat;
    }

    private void detectType(){
        final ImageProcessor imp = this.getProcessor();
        int bitDepth = imp.getBitDepth();
        int numPixels = imp.getPixelCount();
        if (bitDepth == 8 && numPixels == imp.getWidth() * imp.getHeight()) {
            this.type = CvType.CV_8UC1;
        } else if (bitDepth == 24 && numPixels == imp.getWidth() * imp.getHeight() * 3) {
            this.type = CvType.CV_8UC3;
            this.RBG = true;
        }
        System.gc();
    }

    public Point[] getPoints(){
        return this.pointList.toArray(new Point[0]);
    }

    public int getIndexOfPoint(final Point point){
        return this.pointList.indexOf(point) + 1;
    }

    public void addPoint(final Point point){
        this.pointList.add(point);
    }

    public void removePoint(final Point point){
        this.pointList.removeIf(p -> p.equals(point));
    }

    public MatOfPoint2f getMatOfPoint(){
        final MatOfPoint2f mat = new MatOfPoint2f();
        mat.fromList(this.pointList);
        return mat;
    }

    public void addPoints(final List<Point> points){
        this.pointList.addAll(points);
    }

    public List<Point> getListPoints(){
        return this.pointList;
    }

    public ImagePlus getImagePlus(){
        return this;
    }

    public ImagePlus getOriginalImage(){
        return null;
    }

    public boolean isProcessed(){
        return false;
    }

    public Mat getGrayScaleMat(){
        if(this.RBG){
            final Mat grayImage = new Mat();
            Imgproc.cvtColor(this.getMatImage(), grayImage, Imgproc.COLOR_BGR2GRAY);
            return grayImage;
        }
        return this.getMatImage();
    }

    public Mat getMatImage(){
        return Objects.nonNull(this.matrix) ? this.matrix :
                this.address > 0 ? new Mat(this.address) : Imgcodecs.imread(this.path, CvType.CV_8U);
    }

    public int getType(){
        return this.type;
    }


    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public Mat getOriginalMatImage(){
        return this.address > 0 ? new Mat(this.address) : Imgcodecs.imread(this.path, CvType.CV_8U);
    }

    public void movePoint(final Point point, final Point newPoint){
        this.pointList.set(this.pointList.indexOf(point), newPoint);
    }

    public int numberOfPoints(){
        return this.pointList.size();
    }

    public void editPointIndex(final int indexToEdit, final int newIndex) {
        final Point pointToMove = this.pointList.get(indexToEdit);
        if(this.pointList.contains(pointToMove)){
            this.pointList.remove(indexToEdit);
            this.pointList.add(newIndex, pointToMove);
        }
    }

    public String getPath(){
        return this.path;
    }
    public String getName() {
        return super.getTitle();
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
