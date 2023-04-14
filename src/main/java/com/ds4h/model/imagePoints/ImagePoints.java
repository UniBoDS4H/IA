package com.ds4h.model.imagePoints;
import com.ds4h.model.util.imageManager.ImageProcessorMatConverter;
import ij.IJ;
import ij.ImagePlus;
import ij.process.*;
import org.opencv.core.*;
import org.opencv.core.Point;
import java.util.*;
import java.util.List;

/**
 *
 */
public class ImagePoints extends ImagePlus{
    private final List<Point> pointList;
    private final String path;
    private final int rows, cols;
    private final long address;
    private final Size matSize;
    private Mat matrix = null;
    private int type = -1;
    private boolean improveMatrix = false;
    /**
     * Creates a new ImagePoints object with the specified path.
     * @param path the path of the image file.
     */
    public ImagePoints(final String path){
        super(Objects.requireNonNull(path));
        this.path = path;
        this.address = -1;
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
        this.matSize = new Size(cols, rows);
        this.detectType();
    }

    /**
     *
     * Creates a new ImagePoints object with the specified path, image processing options, and ImageProcessor object.
     * @param path the path to the image file.
     * @param improve a boolean flag indicating whether to improve the matrix or not.
     * @param ip the ImageProcessor object to use for the image.
     */
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
        this.address = -1;
        this.detectType();
    }

    /**
     * Creates a new ImagePoints object with the specific path, and the Matrix address
     * @param path the path to the image file.
     * @param matAddress the address to the image matrix.
     */
    public ImagePoints(final String path, final long matAddress) {
        super(Objects.requireNonNull(path));
        this.path = path;
        this.address = matAddress;
        this.matrix = new Mat(matAddress);
        this.rows = this.matrix.rows();
        this.cols = this.matrix.cols();
        this.matSize = matrix.size();
        this.type = matrix.type();
        this.pointList = new ArrayList<>(5);
    }

    /**
     * Creates a new ImagePoints object with the specific path, and the matrix image.
     * @param path the path to the image file.
     * @param mat the image matrix.
     */
    public ImagePoints(final String path, final Mat mat){
        super(Objects.requireNonNull(path));
        this.address = -1;
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
     * Creates a new ImagePoints object with the specific path, and the ImageProcessor of the Image.
     * @param path the path to the image file.
     * @param ip the ImageProcessor of the image.
     */
    public ImagePoints(final String path, final ImageProcessor ip){
        super(Objects.requireNonNull(path));
        this.setProcessor(ip);
        this.address = -1;
        IJ.log("[IMAGE POINT] Processor: " + ip);
        this.path = path;
        this.rows = this.getHeight();
        this.cols = this.getWidth();
        this.pointList = new ArrayList<>(5);
        this.matSize = new Size(this.cols, this.rows);
        this.detectType();
    }

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
     * Sets the "improveMatrix" instance variable to true.
     */
    public void useStock(){
        this.improveMatrix = false;
    }

    /**
     * Returns the value of the "improveMatrix" instance variable.
     * @return the value of the "improveMatrix" instance variable.
     */
    public boolean toImprove(){
        return this.improveMatrix;
    }

    /**
     * Returns the value of the "type" instance variable.
     * @return the value of the "type" instance variable.
     */
    public int type(){
        return this.type;
    }

    /**
     * Returns all the points saved inside the ImagePoint object.
     * @return all the points saved inside the ImagePoint object.
     */
    public Point[] getPoints(){
        return this.pointList.toArray(new Point[0]);
    }

    /**
     * Returns the index of the specified point in the "pointList" ArrayList, plus one.
     * @param point the point to search for.
     * @return the index of the specified point in the "pointList" ArrayList, plus one.
     */
    public int getIndexOfPoint(final Point point){
        return this.pointList.indexOf(point) + 1;
    }

    /**
     * Add a new point inside the "pointList" ArrayList.
     * @param point the new point to add inside the "pointList".
     */
    public void addPoint(final Point point){
        this.pointList.add(point);
    }

    /**
     * Remove the input point from the "pointList" ArrayList.
     * @param point the point to remove from the "pointList".
     */
    public void removePoint(final Point point){
        this.pointList.removeIf(p -> p.equals(point));
    }

    /**
     * Returns the MatOfPoint of all the points stored inside "pointList."
     * @return the MatOfPoint of all the points stored inside "pointList".
     */
    public MatOfPoint2f getMatOfPoint(){
        final MatOfPoint2f mat = new MatOfPoint2f();
        mat.fromList(this.pointList);
        return mat;
    }

    /**
     * Adds the specified list of points to the "pointList" ArrayList.
     * @param points the list of points to add to the "pointList" ArrayList
     */
    public void addPoints(final List<Point> points){
        if(Objects.nonNull(points)) {
            this.pointList.addAll(points);
        }
    }

    /**
     * Returns all the points inside "pointList".
     * @return all the points inside "pointList".
     */
    public List<Point> getListPoints(){
        return this.pointList;
    }

    /**
     * Returns the ImagePlus.
     * @return the ImagePlus.
     */
    public ImagePlus getImagePlus(){
        return this;
    }

    /**
     * Returns a grayscale Mat object converted from the ImageProcessor of the ImagePoints object.
     * @return a grayscale Mat object converted from the ImageProcessor of the ImagePoints object.
     */
    public Mat getGrayScaleMat(){
        return ImageProcessorMatConverter.convertGray(this.getProcessor());
    }

    /**
     * Remove all the points from "pointList".
     */
    public void clearPoints(){
        this.pointList.clear();
    }

    /**
     * Returns a Mat object of the ImagePoints object.
     * @return a Mat object of the ImagePoints object.
     */
    public Mat getMatImage(){
        return Objects.nonNull(this.matrix) ? this.matrix :
                this.address > 0 ? new Mat(this.address) :
                        ImageProcessorMatConverter.convert(this.getProcessor());
    }

    /**
     * Returns the number of rows.
     * @return the number of rows.
     */
    public int getRows(){
        return rows;
    }

    /**
     * Returns the number of cols.
     * @return the number of cols.
     */
    public int getCols(){
        return cols;
    }

    /**
     * Returns the Mat Size.
     * @return the Mat Size.
     */
    public Size getMatSize(){
        return this.matSize;
    }

    /**
     * Replaces the specified point in the "pointList" ArrayList with a new point.
     * @param point the point to replace.
     * @param newPoint the new point to add in place of the old point.
     */
    public void movePoint(final Point point, final Point newPoint){
        if(Objects.nonNull(point) && Objects.nonNull(newPoint)) {
            this.pointList.set(this.pointList.indexOf(point), newPoint);
        }
    }

    /**
     * Returns the total number of points inside "pointList".
     * @return the total number of points inside "pointList".
     */
    public int numberOfPoints(){
        return this.pointList.size();
    }

    /**
    * Moves the point at the specified index in the "pointList" ArrayList to a new index.
    * @param indexToEdit the index of the point to move
    * @param newIndex the index to move the point to
    */
    public void editPointIndex(final int indexToEdit, final int newIndex) {
        final Point pointToMove = this.pointList.get(indexToEdit);
        if(this.pointList.contains(pointToMove)){
            this.pointList.remove(indexToEdit);
            this.pointList.add(newIndex, pointToMove);
        }
    }

    /**
     * Returns the ImagePoint file path.
     * @return the ImagePoint file path.
     */
    public String getPath(){
        return this.path;
    }

    /**
     * Returns the file name.
     * @return the file name.
     */
    public String getName() {
        return super.getTitle();
    }

    /**
     * Returns the toString of the ImagePoints.
     * @return the toString of the ImagePoints.
     */
    @Override
    public String toString() {
        return this.getTitle();
    }
}
