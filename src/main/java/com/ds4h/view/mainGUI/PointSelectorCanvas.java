package com.ds4h.view.mainGUI;

import ij.ImagePlus;
import ij.gui.ImageCanvas;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PointSelectorCanvas extends ImageCanvas implements MouseListener {
    public PointSelectorCanvas(ImagePlus image) {
        super(image);
        addMouseListener(this);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        double magnification = getMagnification();
        Rectangle srcRect = getSrcRect();
        System.out.println(magnification);
        int x = (int) ((p.x / magnification) + srcRect.x);
        int y = (int) ((p.y / magnification) + srcRect.y);
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            Graphics g = getGraphics();
            g.setColor(Color.RED);
            g.fillRect(p.x - 1, p.y - 1, 3, 3);
        }
    }
}
