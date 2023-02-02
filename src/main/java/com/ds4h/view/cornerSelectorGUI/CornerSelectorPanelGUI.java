package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.CoordinateConverter;
import org.opencv.core.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

public class CornerSelectorPanelGUI extends JPanel {
    private ImageCorners currentImage;
    private Set<Point> selectedPoints;
    private Point referencePoint;
    private final int POINT_DIAMETER = 10;
    private final int RADIUS = 10;
    public CornerSelectorPanelGUI() {
        this.selectedPoints = new HashSet<>();
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(referencePoint != null) {
                    Point newPoint = getMatIndexFromPoint(new Point(e.getX(), e.getY()));
                    //currentImage.moveCorner(referencePoint, newPoint);
                    moveAllSelected(referencePoint, newPoint);
                    referencePoint = newPoint;
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = getMatIndexFromPoint(new Point(e.getX(),e.getY()));

                if(imageContains(point)){//point already present in the image
                    Point actualPoint = getActualPoint(point); //getting the exact pressed point
                    referencePoint = actualPoint;
                    if(e.isControlDown()){
                        if(selectedPoints.contains(actualPoint)){//if the point is already selected
                            selectedPoints.remove(actualPoint);
                        }else{
                            selectedPoints.add(actualPoint);
                        }
                    }
                }else{
                    currentImage.addCorner(getMatIndexFromPoint(new Point(e.getX(), e.getY())));
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                referencePoint = null;
                super.mouseReleased(mouseEvent);
            }
        });

    }
    private void moveAllSelected(Point oldPoint, Point newPoint){
        int xGap = (int)(newPoint.x-oldPoint.x);
        int yGap = (int)(newPoint.y-oldPoint.y);
        System.out.println(this.selectedPoints.size());
        this.selectedPoints.forEach(p->{
            this.currentImage.moveCorner(p, new Point(p.x+xGap,p.y+yGap));
        });
        this.selectedPoints.stream().forEach(System.out::println);
        System.out.println();
        this.selectedPoints = this.selectedPoints.stream().map(p-> new Point(p.x+xGap,p.y+yGap)).collect(Collectors.toSet());
        this.selectedPoints.stream().forEach(System.out::println);
    }
    private Point getMatIndexFromPoint(Point p){
        return CoordinateConverter.getMatIndexFromPoint(p, currentImage.getMatImage().rows(), currentImage.getMatImage().cols(), getWidth(), getHeight());
    }

    private Point getPointFromMatIndex(Point p){
        return CoordinateConverter.getPointFromMatIndex(p,this.currentImage.getMatImage().rows(), this.currentImage.getMatImage().cols(), this.getWidth(), this.getHeight());
    }
    private boolean imageContains(Point point){
        return Arrays.stream(this.currentImage.getCorners())
                .anyMatch(p-> p.x < point.x+RADIUS && p.x > point.x-RADIUS && p.y < point.y+RADIUS && p.y > point.y-RADIUS);
    }

    /**
     * Gets the right pressed corner considering some gap of selection
     * @param selected : selected corner (matrix index)
     * @return the corresponding actual point (matrix index)
     */
    private Point getActualPoint(Point selected){
       Optional<Point> point = Arrays.stream(this.currentImage.getCorners()).filter(p -> p.x < selected.x + RADIUS && p.x > selected.x - RADIUS && p.y < selected.y + RADIUS && p.y > selected.y - RADIUS).findFirst();
        if(point.isPresent()) {
            System.out.println("actual" + point.get());
            return point.get();
        }else{
            throw new IllegalArgumentException("selected point was not among the image ones");
        }
    }

    public void setCurrentImage(ImageCorners image){
        this.currentImage = image;
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        if(this.currentImage != null){
            super.paintComponent(g);
            g.drawImage(this.currentImage.getBufferedImage(),0,0,this.getWidth(),this.getHeight(),this);
        }

        Arrays.stream(this.currentImage.getCorners())
                .map(p-> new AbstractMap.SimpleEntry<Point,Point>(this.getPointFromMatIndex(p),p))
                .forEach(point->{ //point.getValue() -> is the matrix index of the point.      point.getKey() -> is the position of the point to show
                    g.setColor(Color.YELLOW);
                    g.drawString(Integer.toString(this.currentImage.getIndexOfCorner(point.getValue())),(int)point.getKey().x - 15, (int)point.getKey().y+5);
                    if(!this.selectedPoints.contains(point.getValue())){
                        g.setColor(Color.RED);
                    }else{
                        g.setColor(Color.YELLOW);
                    }
                    g.fillOval((int)point.getKey().x - 5, (int)point.getKey().y - 5, POINT_DIAMETER, POINT_DIAMETER);
                });

    }

}