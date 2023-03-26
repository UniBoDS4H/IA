package com.ds4h.model.imagePoints;
import com.ds4h.model.util.converter.ImagePlusMatConverter;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
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
    private long address=-1;
    private Size matSize = null;
    private Mat matrix = null;
    public ImagePoints(final String path){
        super(Objects.requireNonNull(path));
        this.path = path;
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
    }

    public ImagePoints(final String path, final int rows, final int cols, final int type, final long matAddress) {
        this(path);
        this.rows = rows;
        this.cols = cols;
        this.address = matAddress;
        IJ.log("New ImagePoints --> Rows: " + this.rows + " Cols: " + this.cols + " Address: " + this.address);
    }

    public ImagePoints(final String path, final Mat mat){
        this(path);
        matrix = mat;
        this.rows = mat.rows();
        this.cols = mat.cols();
        this.matSize = matrix.size();
        IJ.log("[IMAGE POINTS] Image created");
    }

    public ImagePoints(final String path, final ImageProcessor ip){
        super(Objects.requireNonNull(path));
        this.path = path;
        this.setProcessor(ip);
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
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
        return ImagePlusMatConverter.convertGray(this.getProcessor());
    }

    public void clearPoints(){
        this.pointList.clear();
    }

    public Mat getMatImage(){
        return Objects.nonNull(this.matrix) ? this.matrix :
                this.address > 0 ? new Mat(this.address) :
                        ImagePlusMatConverter.convert(this.getProcessor());
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public Size getMatSize(){
        return this.matSize;
    }

    public Mat getOriginalMatImage(){
        return this.address > 0 ? new Mat(this.address) : Imgcodecs.imread(this.path, Imgcodecs.IMREAD_COLOR);
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
