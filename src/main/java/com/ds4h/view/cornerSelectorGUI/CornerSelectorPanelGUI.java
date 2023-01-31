package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.model.imageCorners.ImageCorners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
                currentImage.addCorner(new Point(e.getPoint().x,e.getPoint().y));
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
            g.drawImage(this.currentImage.getImage().getBufferedImage(),0,0,this.getWidth(),this.getHeight(),this);
        }

        for (Point point : this.currentImage.getCorners()) {
            g.setColor(Color.YELLOW);
            g.drawString(Integer.toString(this.currentImage.getIndexOfCorner(point)),(int)point.x - 15, (int)point.y+5);
            g.setColor(Color.RED);
            g.fillOval((int)point.x - 5, (int)point.y - 5, 10, 10);
        }
    }

}
