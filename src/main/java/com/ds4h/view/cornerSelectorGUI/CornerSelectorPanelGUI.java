package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.view.displayInfo.DisplayInfo;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class CornerSelectorPanelGUI extends JPanel implements MouseListener, MouseMotionListener {
    private JPanel listPanel;
    private ImagePlus currentImage;
    private JScrollPane listScroller;
    private List<Point> points = new ArrayList<>();
    private List<Point> selectedPoints = new ArrayList<>();
    private List<Integer> offsetX = new ArrayList<>();
    private List<Integer> offsetY = new ArrayList<>();
    private boolean isDragging;
    public CornerSelectorPanelGUI() {
    }

    public void setCurrentImage(ImagePlus image){
        this.currentImage = image;
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        if(this.currentImage != null){
            super.paintComponent(g);
            g.drawImage(this.currentImage.getBufferedImage(),0,0,this.getWidth(),this.getHeight(),this);
        }
    }




    @Override
    public void mousePressed(MouseEvent e) {
        for (Point point : points) {
            if (e.getX() >= point.x - 5 && e.getX() <= point.x + 5 && e.getY() >= point.y - 5 && e.getY() <= point.y + 5) {
                if (!selectedPoints.contains(point)) {
                    selectedPoints.add(point);
                    offsetX.add(e.getX() - point.x);
                    offsetY.add(e.getY() - point.y);
                } else {
                    int index = selectedPoints.indexOf(point);
                    selectedPoints.remove(point);
                    offsetX.remove(index);
                    offsetY.remove(index);
                }
                isDragging = true;
                repaint();
                return;
            }
        }
        points.add(new Point(e.getX(), e.getY()));
        isDragging = false;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragging) {
            for (int i = 0; i < selectedPoints.size(); i++) {
                Point point = selectedPoints.get(i);
                int x = e.getX() - offsetX.get(i);
                int y = e.getY() - offsetY.get(i);
                point.x = x;
                point.y = y;
            }
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}


}
