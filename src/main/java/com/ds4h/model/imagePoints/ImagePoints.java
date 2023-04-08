package com.ds4h.model.imagePoints;
import com.ds4h.model.util.converter.ImageProcessorMatConverter;
import ij.IJ;
import ij.ImagePlus;
import ij.process.*;
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
    private final Size matSize;
    private Mat matrix = null;
    private int type = -1;
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
        this.matSize = new Size(cols, rows);
        this.detectType();
    }

    public ImagePoints(final String path, final boolean improve, final ImageProcessor ip){
        super(Objects.requireNonNull(path));
        this.setProcessor(ip);
        IJ.log("[IMAGE POINT] Processor: " + ip);
        this.improveMatrix = improve;
        this.path = path;
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
        this.matSize = new Size(this.cols, this.rows);
        this.detectType();
    }

    /**
     *
     * @param path
     * @param rows
     * @param cols
     * @param matAddress
     */
    public ImagePoints(final String path, final int rows, final int cols, final int type,  final long matAddress) {
        this(path);
        this.rows = rows;
        this.cols = cols;
        this.address = matAddress;
        this.type = type;
        IJ.log("New ImagePoints --> Rows: " + this.rows + " Cols: " + this.cols + " Address: " + this.address);
    }

    /**
     *
     * @param path
     * @param mat
     */
    public ImagePoints(final String path, final Mat mat){
        super(Objects.requireNonNull(path));
        this.path = path;
        this.matrix = mat;
        this.rows = mat.rows();
        this.cols = mat.cols();
        this.matSize = matrix.size();
        this.type = matrix.type();
        this.pointList = new ArrayList<>(5);
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
        IJ.log("[IMAGE POINT] Processor: " + ip);
        this.path = path;
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
        this.matSize = new Size(this.cols, this.rows);
        this.detectType();
    }

    /**
     *
     */
    private void detectType(){
        final ImageProcessor ip = this.getProcessor();
        if(ip instanceof ColorProcessor){
            this.type = CvType.CV_8UC3;
        }else if(ip instanceof FloatProcessor) {
            this.type = ip.getBitDepth() == 3 ? CvType.CV_32FC3 : CvType.CV_32FC1;
        }else if(ip instanceof ShortProcessor){
            this.type = ip.getBitDepth() == 3 ? CvType.CV_16UC3 : CvType.CV_16UC1;
        }else if(ip instanceof ByteProcessor){
            this.type = ip.getBitDepth() == 3 ? CvType.CV_8UC3 : CvType.CV_8UC1;
        }
    }

    /**
     *
     */
    public void improve(){
        this.improveMatrix = true;
    }

    /**
     *
     */
    public void useStock(){
        this.improveMatrix = false;
    }

    /**
     *
     * @return
     */
    public boolean toImprove(){
        return this.improveMatrix;
    }

    /**
     *
     * @return
     */
    public int type(){
        return this.type;
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
