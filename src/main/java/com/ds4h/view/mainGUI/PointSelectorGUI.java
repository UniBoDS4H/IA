package com.ds4h.view.mainGUI;

import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.TextRoi;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PointSelectorGUI extends ImageWindow {
    public PointSelectorGUI(ImagePlus ima) {
        super(ima, new PointSelectorCanvas(ima));

        this.getCanvas().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Point p = mouseEvent.getPoint();
                double magnification = getCanvas().getMagnification();
                Rectangle srcRect = getCanvas().getSrcRect();
                int x = (int) ((p.x / magnification) + srcRect.x);
                int y = (int) ((p.y / magnification) + srcRect.y);
                if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
                    Graphics g = getGraphics();
                    g.setColor(Color.RED);
                    g.fillRect(p.x - 1, p.y - 1, 3, 3);
                    getCanvas().paint(g);
                }

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }
}
