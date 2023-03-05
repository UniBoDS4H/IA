package com.ds4h.controller.pointController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.reuse.ReuseSources;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.view.cornerSelectorGUI.MenuItem;
import org.opencv.core.Point;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class PointController {
    private final PointManager pointManager;
    public PointController(){
        this.pointManager = new PointManager();
    }
    public void loadImages(final List<String> paths) throws Exception {
        final List<ImagePoints> imagePoints = ImagingConversion.fromPath(paths)
                    .stream().map(ImagePoints::new)
                    .collect(Collectors.toList());
        System.out.println(imagePoints.size());
        if(imagePoints.size() > 1 || (imagePoints.size() == 1 && this.pointManager.getCornerImages().size() > 0)) {
            this.pointManager.addImages(imagePoints);
        }else {
            throw new IllegalArgumentException("You can not upload one single photo or less.");
        }
    }

    public PointManager getCornerManager(){
        return this.pointManager;
    }

    public List<ImagePoints> getCornerImagesImages() {
        return this.pointManager.getCornerImages();
    }
    public boolean isSource(final ImagePoints image){
        return this.pointManager.getSourceImage().isPresent() && this.pointManager.getSourceImage().get().equals(image);
    }

    public void changeTarget(final ImagePoints newTarget){
        this.pointManager.setAsSource(newTarget);
    }

    public void reuseSource(final List<AlignedImage> alignedImages) throws FileNotFoundException {
        ReuseSources.reuseSources(this.pointManager, alignedImages);
    }

    public boolean isTarget(final ImagePoints image){
        return this.pointManager.getSourceImage().isPresent() && this.pointManager.getSourceImage().get().equals(image);
    }

    public void removeImage(final ImagePoints image){
        if(this.pointManager.getCornerImages().contains(image)){
            this.pointManager.removeImage(image);
        }
    }

    public boolean copyPoints(final List<Point> selectedPoints, final ImagePoints img) {
        boolean res = true;
        for (Point p:selectedPoints) {
            if(this.insideImage(p,img)){
                img.addPoint(p);
            }else{
                res = false;
            }
        }
        return res;
    }

    private boolean insideImage(final Point p, final ImagePoints img) {
        return img.getMatImage().rows() >= p.y && img.getMatImage().cols() >= p.x;
    }

    public MenuItem getMenuItem(final ImagePoints image){
        //TODO: non penso vada bene, non ci deve essere nulla della view per MVC
        return new MenuItem(this.getCornerImagesImages().indexOf(image)+1, image);
    }

    public void clearProject(){
        this.pointManager.clearProject();
    }
}
