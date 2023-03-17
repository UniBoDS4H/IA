package com.ds4h.view.mainGUI;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.CoordinateConverter;
import ij.ImageJ;
import ij.gui.*;
import ij.plugin.tool.PlugInTool;
import org.opencv.core.Point;

import javax.tools.Tool;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PointSelectorCanvas extends ImageCanvas implements MouseListener {
    private final ImagePoints image;
    private Color textColor;
    private int pointerDimension;
    private Color selectedPointerColor;
    private final Overlay overlay;

    private final int POINT_DIAMETER = 6;
    int cl = 0;
    private List<Point> selectedPoints = new ArrayList<>();

    public PointSelectorCanvas(ImagePoints image) {
        super(image);
        this.overlay = new Overlay();
        this.setOverlay(overlay);
        this.overlay.selectable(false);
        this.image = image;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (Toolbar.getToolId() != Toolbar.HAND) {
                    Point point = new Point(getCursorLoc().x, getCursorLoc().y);
                    // Point point = getMatIndexFromPoint(getScaledPoint(e));
                    if (imageContains(point)) {//point already present in the image
                        Point actualPoint = getActualPoint(point); //getting the exact pressed point
                        //referencePoint = actualPoint;
                        if (e.isControlDown()) {
                            if (selectedPoints.contains(actualPoint)) {//if the point is already selected
                                selectedPoints.remove(actualPoint);
                                //container.removeSelectedPoint(actualPoint);
                            } else {
                                selectedPoints.add(actualPoint);
                                //container.addSelectedPoint(actualPoint);
                            }
                        } else {
                            if (!selectedPoints.contains(actualPoint)) {
                                selectedPoints.clear();
                                selectedPoints.add(actualPoint);
                            }
                        }
                    } else {
                        //ADD new point
                        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                            //first i add the new point then i call the updatePointsForAlignment for checking if we can enable the manual alignment button
                            image.addPoint((point));
                            //container.updatePointsForAlignment();
                        } else {
                            selectedPoints.clear();
                            //if I single click a place where there is no point I clear the selection
                            //container.clearSelectedPoints();
                            //released = false;
                            // startPoint = MouseInfo.getPointerInfo().getLocation();
                        }
                    }
                    drawPoints();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                /*
        referencePoint = null;
        released = true;
        if(dragger){
            repaint();
        }
        super.mouseReleased(mouseEvent);

         */
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                //mouseClicked(e);
        /*
        Point point = getMatIndexFromPoint(getScaledPoint(e));
        if(imageContains(point)){//point already present in the image
            Point actualPoint = getActualPoint(point); //getting the exact pressed point
            if(!e.isControlDown()){
                container.clearSelectedPoints();
                container.addSelectedPoint(actualPoint);
            }
            repaint();
        }
         */
            }
        });
    }



    private void drawPoints(){
        overlay.clear();
        this.image.getListPoints().forEach(this::drawPoint);
    }
    private void drawPoint(Point p) {
        Color c;
        if(this.selectedPoints.contains(p)){
            c = Color.BLUE;
        }else{
            c = Color.RED;
        }
        OvalRoi circle = new OvalRoi(p.x-20, p.y-20, 40,40);
        circle.setStrokeWidth(4);
        PointRoi center = new PointRoi(p.x, p.y);
        center.setStrokeColor(c);
        circle.setStrokeColor(c);
        overlay.add(circle);
        overlay.add(center);
        overlay.selectable(false);
        this.repaint();
    }
    private Point getScaledPoint(MouseEvent e){
        return null;
        //return new Point((-xOffset/zoomFactor)+(e.getX()/zoomFactor), (-yOffset/zoomFactor)+(e.getY()/zoomFactor));
    }
    private Point getActualPoint(Point point){
        for (int i = 0; i < overlay.size(); i++) {
            Object element = overlay.get(i);
            if (element instanceof OvalRoi) {
                if(((OvalRoi)element).contains((int)point.x,(int)point.y)){
                    OvalRoi o = ((OvalRoi)element);
                    return new Point(o.getXBase()+o.getFloatHeight()/2, o.getYBase()+o.getFloatHeight()/2);
                }
            }
        }
        throw new IllegalArgumentException("the point is not contained in the image");
    }
    private boolean imageContains(Point point){
        for (int i = 0; i < overlay.size(); i++) {
            Object element = overlay.get(i);
            if (element instanceof OvalRoi) {
                if(((OvalRoi)element).contains((int)point.x,(int)point.y)){
                    return true;
                }
            }
        }
        return false;
    }


}
