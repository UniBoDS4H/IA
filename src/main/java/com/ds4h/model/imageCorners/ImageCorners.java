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

public class ImageCorners {
    private final File image;
    private final List<Point> corners;

    public ImageCorners(File image){
        this.image = image;
        this.corners = new ArrayList<>();
    }
    public ImagePlus getImage(){
        Optional<ImagePlus> img = ImagingConversion.fromSinglePathToImagePlus(this.image.getPath());
        return img.get();
    }
    public BufferedImage getBufferedImage(){
        Mat mat = this.getMatImage();

        BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_3BYTE_BGR);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);


        return image;
    }

    public String getPath(){
        return this.image.getPath();
    }

    public Point[] getCorners(){
        return this.corners.toArray(new Point[0]);
    }
    public int getIndexOfCorner(Point corner){
        return this.corners.indexOf(corner)+1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageCorners that = (ImageCorners) o;
        return Objects.equals(image, that.image) && Objects.equals(corners, that.corners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, corners);
    }

    public void addCorner(Point corner){
        if(!this.corners.contains(corner)){
            this.corners.add(corner);
        }
    }

    public File getFile(){
        return this.image;
    }
    public void removeCorner(Point corner){
        if(!this.corners.remove(corner)){
            throw new IllegalArgumentException("given corner was not found");
        }
    }

    public MatOfPoint2f getMatOfPoint(){
        MatOfPoint2f mat = new MatOfPoint2f();
        mat.fromList(this.corners);
        return mat;
    }

    public MatOfKeyPoint getMatOfKeyPoint(){
        List<KeyPoint> keypoints = new ArrayList<>();
        for (Point point : this.corners) {
            keypoints.add(new KeyPoint((float)point.x, (float)point.y, 1));
        }

        MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
        matOfKeyPoints.fromList(keypoints);
        return matOfKeyPoints;
    }

    public Mat getMatImage(){
        Mat mat = Imgcodecs.imread(this.image.getPath());
        this.corners.forEach(c->{
            for(int j = (int)c.y-10; j < (int)c.y +10; j++) {
                for (int i = (int) c.x - 10; i < (int) c.x + 10; i++) {
                    mat.put(i, j, new double[]{0, 0, 255});
                }
            }

        });
        return mat;
    }
    public void moveCorner(File image, Point corner, Point newCorner){

    }
}
