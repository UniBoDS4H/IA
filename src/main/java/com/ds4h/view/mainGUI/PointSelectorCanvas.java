package com.ds4h.view.mainGUI;

import com.ds4h.model.imagePoints.ImagePoints;
import ij.gui.*;
import org.opencv.core.Point;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PointSelectorCanvas extends ImageCanvas implements MouseListener {
    private final ImagePoints image;
    private Color textColor;
    private int pointerDimension = (this.imageWidth + this.imageHeight)/100;
    private Color selectedPointerColor;
    private final Overlay overlay;

    private Point referencePoint;
    int cl = 0;
    private List<Point> selectedPoints = new ArrayList<>();

    public PointSelectorCanvas(ImagePoints image) {
        super(image);
        this.overlay = new Overlay();
        this.setOverlay(overlay);
        this.overlay.selectable(false);
        this.image = image;
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(referencePoint != null) {
                    Point newPoint = new Point(getCursorLoc().x, getCursorLoc().y);
                    moveAllSelected(referencePoint, newPoint);
                    referencePoint = newPoint;
                }
                drawPoints();
            }
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (Toolbar.getToolId() != Toolbar.HAND) {
                    Point point = new Point(getCursorLoc().x, getCursorLoc().y);
                    if (imageContains(point)) {//point already present in the image
                        Point actualPoint = getActualPoint(point); //getting the exact pressed point
                        referencePoint = actualPoint;
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
                        }
                    }
                    drawPoints();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                referencePoint = null;
            }
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {}
        });
    }



    private void moveAllSelected(Point oldPoint, Point newPoint){
        int xGap = (int)(newPoint.x-oldPoint.x);
        int yGap = (int)(newPoint.y-oldPoint.y);
        this.selectedPoints.forEach(p-> this.image.movePoint(p, new Point(p.x+xGap,p.y+yGap)));
        this.selectedPoints = this.selectedPoints.stream().map(p-> new Point(p.x+xGap,p.y+yGap)).collect(Collectors.toList());
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
        OvalRoi circle = new OvalRoi(p.x - this.pointerDimension, p.y - this.pointerDimension, this.pointerDimension * 2, this.pointerDimension * 2);
        circle.setStrokeWidth(this.pointerDimension/5);
        PointRoi center = new PointRoi(p.x, p.y);
        int textX = (int) p.x - this.pointerDimension;
        int textY = (int) p.y + this.pointerDimension;
        TextRoi index = new TextRoi(textX, textY, Integer.toString(this.image.getIndexOfPoint(p)));
        Font f = new Font("Serif", Font.BOLD, this.pointerDimension);
        index.setFont(f);
        center.setStrokeColor(c);
        circle.setStrokeColor(c);
        overlay.add(circle);
        overlay.add(center);
        overlay.add(index);
        overlay.selectable(false);
        this.repaint();
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

    @Override
    public void setMagnification(double magnification) {

        System.out.println(magnification);
        super.setMagnification(magnification);
    }

}