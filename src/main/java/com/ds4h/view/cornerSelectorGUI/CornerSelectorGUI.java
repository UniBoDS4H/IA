package com.ds4h.view.cornerSelectorGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import ij.ImagePlus;
import ij.io.Opener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CornerSelectorGUI extends JPanel implements MouseListener, MouseMotionListener {
    private JPanel listPanel;
    private ImagePlus currentImage;
    private JScrollPane listScroller;
    private List<Point> points = new ArrayList<>();
    private List<Point> selectedPoints = new ArrayList<>();
    private List<Integer> offsetX = new ArrayList<>();
    private List<Integer> offsetY = new ArrayList<>();
    private boolean isDragging;
    public CornerSelectorGUI() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setCurrentImage(ImagePlus image){
        this.currentImage = image;
    }

    public void loadImages(File[] files) {

        if(files.length>0){
            Opener opener = new Opener();
            ImagePlus image = opener.openImage(files[0].getPath());
            setCurrentImage(image);
            repaint();
        }

    }
    @Override
    public void paintComponent(Graphics g) {
        if(this.currentImage != null){
            super.paintComponent(g);
            //scaling the dimension of the image
            Dimension newDimension = DisplayInfo.getScaledImageDimension(
                    new Dimension(this.currentImage.getBufferedImage().getWidth(this),
                    this.currentImage.getBufferedImage().getHeight(this)),
                    new Dimension(this.getWidth(), this.getHeight()));
            g.drawImage(this.currentImage.getBufferedImage(),0,0,(int)newDimension.getHeight(),(int)newDimension.getWidth(),this);
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
