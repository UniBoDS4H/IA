package com.ds4h.view.mainGUI;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.CoordinateConverter;
import ij.gui.*;
import org.opencv.core.Point;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private List<RoiPoint> points;

    public PointSelectorCanvas(ImagePoints image) {
        super(image);
        this.overlay = new Overlay();
        this.image = image;
        addMouseListener(this);
    }
    private void drawPoint(Point p) {
        OvalRoi circle = new OvalRoi(p.x-20, p.y-20, 40,40);
        circle.setStrokeWidth(4);
        PointRoi center = new PointRoi(p.x, p.y);
        center.setStrokeColor(Color.RED);
        circle.setStrokeColor(Color.RED);
        overlay.add(circle);
        overlay.add(center);
        this.setOverlay(overlay);
        this.repaint();
        /*
        for (Point p : this.image.getPoints()) {
            //point.getValue() -> is the matrix index of the point.
            //point.getKey() -> is the position of the point to show
            AbstractMap.SimpleEntry<Point, Point> point = new AbstractMap.SimpleEntry<>(this.getPointFromMatIndex(p), p);
            //System.out.println(DisplayInfo.getTextSize(5));
            Font f = new Font("Serif", Font.BOLD, DisplayInfo.getTextSize(5));
            g2d.setColor(this.textColor);
            g2d.setFont(f);
            int textX = (int) point.getKey().x - this.pointerDimension * 3 - 12;
            int textY = (int) point.getKey().y + this.pointerDimension * 3 + 12;
            g2d.drawString(Integer.toString(this.image.getIndexOfPoint(point.getValue())), textX, textY);
            //if the corner I'm printing it's not selected I use the not selected color
            g2d.setColor(this.container.getSelectedPoints().contains(point.getValue()) ? this.selectedPointerColor : this.pointerColor);
            g2d.setStroke(new BasicStroke(3));

            //g2d.drawOval((int) point.getKey().x - this.pointerDimension * 3, (int) point.getKey().y - this.pointerDimension * 3, this.pointerDimension * 6, this.pointerDimension * 6);
            //g2d.fillOval((int) point.getKey().x - 3, (int) point.getKey().y - 3, POINT_DIAMETER, POINT_DIAMETER);
        }

         */
    }
    private Point getScaledPoint(MouseEvent e){
        return null;
        //return new Point((-xOffset/zoomFactor)+(e.getX()/zoomFactor), (-yOffset/zoomFactor)+(e.getY()/zoomFactor));
    }
    private Point getActualPoint(Point selected){
        Optional<Point> point = Arrays.stream(this.image.getPoints()).filter(p -> p.x < selected.x + this.pointerDimension*3 && p.x > selected.x - this.pointerDimension*3 && p.y < selected.y + this.pointerDimension*3 && p.y > selected.y - this.pointerDimension*3).findFirst();
        if(point.isPresent()) {
            return point.get();
        }else{
            throw new IllegalArgumentException("selected point was not among the image ones");
        }
    }
    private Point getMatIndexFromPoint(Point p){
        return CoordinateConverter.getMatIndexFromPoint(p, this.image.getRows(), this.image.getCols(), getWidth(), getHeight());
    }
    private boolean imageContains(Point point){
        return this.points.stream().anyMatch(p->p.inPos(point));
    }
    public void mousePressed(MouseEvent e) {
        // Point point = getMatIndexFromPoint(getScaledPoint(e));
        /*if(imageContains(point)){//point already present in the image
            Point actualPoint = getActualPoint(point); //getting the exact pressed point
            referencePoint = actualPoint;
            if(e.isControlDown()) {
                if (container.getSelectedPoints().contains(actualPoint)) {//if the point is already selected
                    container.removeSelectedPoint(actualPoint);
                } else {
                    container.addSelectedPoint(actualPoint);
                }
            }else {

                if (!container.getSelectedPoints().contains(actualPoint)) {
                    container.clearSelectedPoints();
                    container.addSelectedPoint(actualPoint);
                }
            }
        }else{
         */

        System.out.println("outside");
        //ADD new point
            if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                System.out.println("inside");
                //first i add the new point then i call the updatePointsForAlignment for checking if we can enable the manual alignment button
                Point p = new Point(getCursorLoc().x, getCursorLoc().y);
                this.image.addPoint(getMatIndexFromPoint(p));
                this.drawPoint(p);
                //container.updatePointsForAlignment();
                repaint();
            }else{
                //if I single click a place where there is no point I clear the selection
                //container.clearSelectedPoints();
                repaint();
                //released = false;
               // startPoint = MouseInfo.getPointerInfo().getLocation();
            }
       // }
    }

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

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
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
    private class RoiPoint{
        private final PointRoi center;
        private final OvalRoi circle;

        private final TextRoi index;

        public RoiPoint(PointRoi center, OvalRoi circle, TextRoi index){
            this.center = center;
            this.circle = circle;
            this.index = index;
        }
        public PointRoi getCenter() {
            return center;
        }

        public OvalRoi getCircle() {
            return circle;
        }

        public TextRoi getIndex() {
            return index;
        }
        public boolean inPos(Point p){
            return this.circle.contains((int)p.x,(int)p.y);
        }
    }

}
