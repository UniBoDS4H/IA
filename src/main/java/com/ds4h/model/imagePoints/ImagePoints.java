package com.ds4h.model.imagePoints;

import com.ds4h.model.util.ImagingConversion;
import ij.ImagePlus;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 */
public class ImagePoints {
    private final File image;
    private final List<Point> points;

    public ImagePoints(final File image){
        this.image = image;
        this.points = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public ImagePlus getImage(){
        final Optional<ImagePlus> img = ImagingConversion.fromSinglePathToImagePlus(this.image.getPath());
        if(img.isPresent()){
            return img.get();
        }
        throw new IllegalArgumentException("The conversion of the Image produced an empty image, please retry.");
    }

    /**
     *
     * @return
     */
    public BufferedImage getBufferedImage(){
        final Mat mat = this.getMatImage();
        final BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_3BYTE_BGR);
        final byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);
        return image;
    }

    /**
     *
     * @return
     */
    public String getPath(){
        return this.image.getPath();
    }

    /**
     *
     * @return
     */
    public Point[] getPoints(){
        return this.points.toArray(new Point[0]);
    }

    /**
     *
     * @param point
     * @return
     */
    public int getIndexOfPoint(final Point point){
        return this.points.indexOf(point)+1;
    }

    /**
     *
     * @param
     * @return
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImagePoints that = (ImagePoints) o;
        return Objects.equals(that.getFile().getName(), this.getFile().getName());
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.image, this.points);
    }

    /**
     *
     * @param point
     */
    public void addPoint(final Point point){
        if(!this.points.contains(point)){
            this.points.add(point);
        }
    }

    /**
     *
     * @return
     */
    public File getFile(){
        return this.image;
    }

    /**
     *
     * @param point
     */
    public void removePoint(final Point point){
        this.points.removeIf(p -> p.equals(point));
    }

    /**
     *
     * @return
     */
    public MatOfPoint2f getMatOfPoint(){
        final MatOfPoint2f mat = new MatOfPoint2f();
        mat.fromList(this.points);

        return mat;
    }

    /**
     *
     * @return
     */
    public MatOfKeyPoint getMatOfKeyPoint(){
        final List<KeyPoint> keypoints = new ArrayList<>();
        for (final Point point : this.points) {
            keypoints.add(new KeyPoint((float)point.x, (float)point.y, 1));
        }
        final MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
        matOfKeyPoints.fromList(keypoints);
        return matOfKeyPoints;
    }

    /**
     *
     * @return
     */
    public Mat getMatImage(){
        return Imgcodecs.imread(this.image.getPath());
    }

    /**
     *
     * @param point
     * @param newPoint
     */
    public void movePoint(final Point point, final Point newPoint){
        this.points.set(this.points.indexOf(point), newPoint);
    }

    /**
     *
     * @return
     */
    public int numberOfPoints(){
        return this.points.size();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getFile().getName();
    }

    /**
     * Change the indexToEdit with the newIndex and translate the others, both of them are 0 based
     * @param indexToEdit
     * @param newIndex
     */
    public void editPointIndex(final int indexToEdit, final int newIndex) {
        final Point pointToMove = this.points.get(indexToEdit);
        if(this.points.contains(pointToMove)){
            this.points.remove(indexToEdit);
            this.points.add(newIndex, pointToMove);
        }
    }
}
