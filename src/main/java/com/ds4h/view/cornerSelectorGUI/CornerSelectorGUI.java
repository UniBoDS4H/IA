package com.ds4h.view.cornerSelectorGUI;
import com.ds4h.view.displayInfo.DisplayInfo;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CornerSelectorGUI extends JPanel implements MouseListener {

    private Image image;
    private List<Integer[]> points = new ArrayList<>();

    public CornerSelectorGUI() {
        addMouseListener(this);
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

    public void paint(Graphics g) {
        if(image != null){
            System.out.println(DisplayInfo.isVertical(new Dimension(image.getWidth(this), image.getHeight(this))));
            Dimension newDimension = DisplayInfo.getScaledImageDimension(new Dimension(image.getWidth(this), image.getHeight(this)),new Dimension(this.getWidth(), this.getHeight()));
            g.drawImage(image,0,0,(int)newDimension.getHeight(),(int)newDimension.getWidth(),this);
            g.setColor(Color.RED);
            for (Integer[] point : points) {
                g.fillOval(point[0]-5, point[1]-5, 10, 10);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        points.add(new Integer[]{x, y});
        repaint();
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
