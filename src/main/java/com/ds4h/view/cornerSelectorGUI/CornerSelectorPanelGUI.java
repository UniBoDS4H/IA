package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.model.imageCorners.ImageCorners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.AbstractMap;
import java.util.Arrays;
import com.ds4h.model.util.CoordinateConverter;
import org.opencv.core.Point;

public class CornerSelectorPanelGUI extends JPanel {
    private JPanel listPanel;
    private ImageCorners currentImage;
    private JScrollPane listScroller;
    private boolean isDragging;
    public CornerSelectorPanelGUI() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentImage.addCorner(CoordinateConverter.getMatIndexFromPoint(new Point(e.getX(), e.getY()), currentImage.getMatImage().rows(), currentImage.getMatImage().cols(), getWidth(), getHeight()));
                repaint();
            }
        });
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
                .map(p-> new AbstractMap.SimpleEntry<Point,Point>(CoordinateConverter.getPointFromMatIndex(p,this.currentImage.getMatImage().rows(), this.currentImage.getMatImage().cols(), this.getWidth(), this.getHeight()),p))
                .forEach(point->{
                    g.setColor(Color.YELLOW);
                    g.drawString(Integer.toString(this.currentImage.getIndexOfCorner(point.getValue())),(int)point.getKey().x - 15, (int)point.getKey().y+5);
                    g.setColor(Color.YELLOW);
                    g.fillOval((int)point.getKey().x - 5, (int)point.getKey().y - 5, 10, 10);
                });

    }

}
