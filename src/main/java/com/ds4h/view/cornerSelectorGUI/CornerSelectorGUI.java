package com.ds4h.view.cornerSelectorGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import java.awt.*;
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

    private Image image;
    private List<Point> points = new ArrayList<>();
    private List<Point> selectedPoints = new ArrayList<>();
    private List<Integer> offsetX = new ArrayList<>();
    private List<Integer> offsetY = new ArrayList<>();
    private boolean isDragging;
    public CornerSelectorGUI() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void loadImages(File[] files) {
        try {
            if(files.length>0){
                image = ImageIO.read(files[0]);
                setPreferredSize(new Dimension(400, 400));
                repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        if(this.image != null){
            super.paintComponent(g);
            Dimension newDimension = DisplayInfo.getScaledImageDimension(new Dimension(image.getWidth(this), image.getHeight(this)),new Dimension(this.getWidth(), this.getHeight()));
            g.drawImage(image,0,0,(int)newDimension.getHeight(),(int)newDimension.getWidth(),this);
            for (Point point : points) {
                if (selectedPoints.contains(point)) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillOval(point.x - 5, point.y - 5, 10, 10);
            }
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
