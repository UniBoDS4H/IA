package com.ds4h.model.imageCorners;

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
public class ImageCorners {
    private final File image;
    private final List<Point> corners;

    public ImageCorners(final File image){
        this.image = image;
        this.corners = new ArrayList<>();
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
    public Point[] getCorners(){
        return this.corners.toArray(new Point[0]);
    }

    /**
     *
     * @param corner
     * @return
     */
    public int getIndexOfCorner(Point corner){
        return this.corners.indexOf(corner)+1;
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
        ImageCorners that = (ImageCorners) o;
        return Objects.equals(image, that.image);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(image, corners);
    }

    /**
     *
     * @param corner
     */
    public void addCorner(final Point corner){
        if(!this.corners.contains(corner)){
            this.corners.add(corner);
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
        this.corners.removeIf(p -> p.equals(point));
    }

    /**
     *
     * @return
     */
    public MatOfPoint2f getMatOfPoint(){
        final MatOfPoint2f mat = new MatOfPoint2f();
        mat.fromList(this.corners);

        return mat;
    }

    /**
     *
     * @return
     */
    public MatOfKeyPoint getMatOfKeyPoint(){
        final List<KeyPoint> keypoints = new ArrayList<>();
        for (final Point point : this.corners) {
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
     * @param corner
     * @param newCorner
     */
    public void moveCorner(final Point corner,final Point newCorner){
        this.corners.set(this.corners.indexOf(corner), newCorner);
    }

    /**
     *
     * @return
     */
    public int numberOfCorners(){
        return this.corners.size();
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
    public void editCornerIndex(final int indexToEdit, final int newIndex) {
        final Point pointToMove = this.corners.get(indexToEdit);
        if(this.corners.contains(pointToMove)){
            this.corners.remove(indexToEdit);
            this.corners.add(newIndex, pointToMove);
        }
    }
}
