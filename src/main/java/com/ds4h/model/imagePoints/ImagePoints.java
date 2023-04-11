package com.ds4h.model.imagePoints;
import com.ds4h.model.util.converter.ImageProcessorMatConverter;
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
     *
     * @param path a
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
     * @param path a
     * @param improve b
     * @param ip c
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
     *
     * @param path a
     * @param matAddress b
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
     *
     * @param path a
     * @param mat b
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
     *
     * @param path a
     * @param ip b
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
     * @return a
     */
    public boolean toImprove(){
        return this.improveMatrix;
    }

    /**
     *
     * @return a
     */
    public int type(){
        return this.type;
    }

    /**
     *
     * @return a
     */
    public Point[] getPoints(){
        return this.pointList.toArray(new Point[0]);
    }

    /**
     *
     * @param point a
     * @return b
     */
    public int getIndexOfPoint(final Point point){
        return this.pointList.indexOf(point) + 1;
    }

    /**
     *
     * @param point a
     */
    public void addPoint(final Point point){
        this.pointList.add(point);
    }

    /**
     *
     * @param point a
     */
    public void removePoint(final Point point){
        this.pointList.removeIf(p -> p.equals(point));
    }

    /**
     *
     * @return a
     */
    public MatOfPoint2f getMatOfPoint(){
        final MatOfPoint2f mat = new MatOfPoint2f();
        mat.fromList(this.pointList);
        return mat;
    }

    /**
     *
     * @param points a
     */
    public void addPoints(final List<Point> points){
        this.pointList.addAll(points);
    }

    /**
     *
     * @return a
     */
    public List<Point> getListPoints(){
        return this.pointList;
    }

    /**
     *
     * @return a
     */
    public ImagePlus getImagePlus(){
        return this;
    }



    /**
     *
     * @return a
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
     * @return a
     */
    public Mat getMatImage(){
        return Objects.nonNull(this.matrix) ? this.matrix :
                this.address > 0 ? new Mat(this.address) :
                        ImageProcessorMatConverter.convert(this.getProcessor());
    }

    /**
     *
     * @return a
     */
    public int getRows(){
        return rows;
    }

    /**
     *
     * @return a
     */
    public int getCols(){
        return cols;
    }

    /**
     *
     * @return a
     */
    public Size getMatSize(){
        return this.matSize;
    }

    /**
     *
     * @param point a
     * @param newPoint b
     */
    public void movePoint(final Point point, final Point newPoint){
        this.pointList.set(this.pointList.indexOf(point), newPoint);
    }

    /**
     *
     * @return a
     */
    public int numberOfPoints(){
        return this.pointList.size();
    }

    /**
     *
     * @param indexToEdit a
     * @param newIndex b
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
     * @return a
     */
    public String getPath(){
        return this.path;
    }

    /**
     *
     * @return a
     */
    public String getName() {
        return super.getTitle();
    }

    /**
     *
     * @return a
     */
    @Override
    public String toString() {
        return this.getTitle();
    }
}
