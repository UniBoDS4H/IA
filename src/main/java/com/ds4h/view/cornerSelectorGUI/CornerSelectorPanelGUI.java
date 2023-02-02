package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.CoordinateConverter;
import org.opencv.core.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class CornerSelectorPanelGUI extends JPanel {
    private ImageCorners currentImage;
    private final Set<Point> selectedPoints;
    private final int POINT_DIAMETER = 10;
    public CornerSelectorPanelGUI() {
        this.selectedPoints = new HashSet<>();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = new Point(e.getX(),e.getY());

                if(imageContains(point)){//point already present in the image

                    if(isSelected(point)){//if the point is already selected
                        selectedPoints.remove(getPointFromSelection(point));
                    }else{
                        selectedPoints.add(point);
                    }
                }else{
                    currentImage.addCorner(CoordinateConverter.getMatIndexFromPoint(new Point(e.getX(), e.getY()), currentImage.getMatImage().rows(), currentImage.getMatImage().cols(), getWidth(), getHeight()));
                }
                repaint();
            }
        });
    }
    private Point getPointFromMatIndex(Point p){
        return CoordinateConverter.getPointFromMatIndex(p,this.currentImage.getMatImage().rows(), this.currentImage.getMatImage().cols(), this.getWidth(), this.getHeight());
    }
    private boolean imageContains(Point point){
        return Arrays.stream(this.currentImage.getCorners()).map(this::getPointFromMatIndex)
                .anyMatch(p-> p.x < point.x+6 && p.x > point.x-6 && p.y < point.y+6 && p.y > point.y-6);
    }
    private boolean isSelected(Point point){
        return this.selectedPoints.stream().anyMatch(p-> p.x < point.x+6 && p.x > point.x-6 && p.y < point.y+6 && p.y > point.y-6);
    }

    private Point getPointFromSelection(Point selected){
        Optional<Point> point = this.selectedPoints.stream().filter(p -> p.x < selected.x + 6 && p.x > selected.x - 6 && p.y < selected.y + 6 && p.y > selected.y - 6).findFirst();
        if(point.isPresent()) {
            return point.get();
        }else{
            throw new IllegalArgumentException("selected point was not among the selected ones");
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
                    if(!this.isSelected(point.getKey())){
                        g.setColor(Color.RED);
                    }else{
                        g.setColor(Color.YELLOW);
                    }
                    g.fillOval((int)point.getKey().x - 5, (int)point.getKey().y - 5, POINT_DIAMETER, POINT_DIAMETER);
                });

    }

}
