package com.ds4h.model.imagePoints;
import com.ds4h.model.util.converter.ImageProcessorMatConverter;
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
    private long address=-1;
    private Size matSize = null;
    private Mat matrix = null;
    private boolean improveMatrix = false;
    /**
     *
     * @param path
     */
    public ImagePoints(final String path){
        super(Objects.requireNonNull(path));
        this.path = path;
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
    }

    /**
     *
     * @param path
     * @param rows
     * @param cols
     * @param matAddress
     */
    public ImagePoints(final String path, final int rows, final int cols, final long matAddress) {
        this(path);
        this.rows = rows;
        this.cols = cols;
        this.address = matAddress;
        this.matSize = new Size(cols, rows);
        IJ.log("New ImagePoints --> Rows: " + this.rows + " Cols: " + this.cols + " Address: " + this.address);
    }

    /**
     *
     * @param path
     * @param mat
     */
    public ImagePoints(final String path, final Mat mat){
        this(path);
        matrix = mat;
        this.rows = mat.rows();
        this.cols = mat.cols();
        this.matSize = matrix.size();
        IJ.log("[IMAGE POINTS] Image created");
    }

    /**
     *
     * @param path
     * @param ip
     */
    public ImagePoints(final String path, final ImageProcessor ip){
        super(Objects.requireNonNull(path));
        this.setProcessor(ip);
        this.path = path;
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
    }

    public void improve(){
        this.improveMatrix = true;
    }

    public void useStock(){
        this.improveMatrix = false;
    }

    public boolean toImprove(){
        return this.improveMatrix;
    }

    /**
     *
     * @return
     */
    public Point[] getPoints(){
        return this.pointList.toArray(new Point[0]);
    }

    /**
     *
     * @param point
     * @return
     */
    public int getIndexOfPoint(final Point point){
        return this.pointList.indexOf(point) + 1;
    }

    /**
     *
     * @param point
     */
    public void addPoint(final Point point){
        this.pointList.add(point);
    }

    /**
     *
     * @param point
     */
    public void removePoint(final Point point){
        this.pointList.removeIf(p -> p.equals(point));
    }

    /**
     *
     * @return
     */
    public MatOfPoint2f getMatOfPoint(){
        final MatOfPoint2f mat = new MatOfPoint2f();
        mat.fromList(this.pointList);
        return mat;
    }

    /**
     *
     * @param points
     */
    public void addPoints(final List<Point> points){
        this.pointList.addAll(points);
    }

    /**
     *
     * @return
     */
    public List<Point> getListPoints(){
        return this.pointList;
    }

    /**
     *
     * @return
     */
    public ImagePlus getImagePlus(){
        return this;
    }



    /**
     *
     * @return
     */
    public Mat getGrayScaleMat(){
        return ImageProcessorMatConverter.convertGray(this.getProcessor());
    }

    /**
     *
     */
    public void clearPoints(){
        this.pointList.clear();
    }

    /**
     *
     * @return
     */
    public Mat getMatImage(){
        return Objects.nonNull(this.matrix) ? this.matrix :
                this.address > 0 ? new Mat(this.address) :
                        ImageProcessorMatConverter.convert(this.getProcessor());
    }

    /**
     *
     * @return
     */
    public int getRows(){
        return rows;
    }

    /**
     *
     * @return
     */
    public int getCols(){
        return cols;
    }

    /**
     *
     * @return
     */
    public Size getMatSize(){
        return this.matSize;
    }

    /**
     *
     * @param point
     * @param newPoint
     */
    public void movePoint(final Point point, final Point newPoint){
        this.pointList.set(this.pointList.indexOf(point), newPoint);
    }

    /**
     *
     * @return
     */
    public int numberOfPoints(){
        return this.pointList.size();
    }

    /**
     *
     * @param indexToEdit
     * @param newIndex
     */
    public void editPointIndex(final int indexToEdit, final int newIndex) {
        final Point pointToMove = this.pointList.get(indexToEdit);
        if(this.pointList.contains(pointToMove)){
            this.pointList.remove(indexToEdit);
            this.pointList.add(newIndex, pointToMove);
        }
    }

    /**
     *
     * @return
     */
    public String getPath(){
        return this.path;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return super.getTitle();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getTitle();
    }
}
