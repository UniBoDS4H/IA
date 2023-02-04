package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import org.opencv.core.Point;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class CornerSelectorGUI extends Frame implements StandardGUI {

    private final CornerController cornerController;
    private final CornerSelectorPanelGUI panel;
    private ImageCorners image;
    private List<Point> selectedPoints;
    private final CornerSelectorMenuGUI menu;
    public CornerSelectorGUI(final ImageCorners image, final CornerController controller){
        this.cornerController = controller;
        this.image = image;
        this.panel = new CornerSelectorPanelGUI(this);
        this.panel.setCurrentImage(image);
        this.menu = new CornerSelectorMenuGUI(this.cornerController, this.image, this);
        this.selectedPoints = new ArrayList<>();
        setLayout(new BorderLayout());
        this.setFrameSize();
        this.addListeners();
        this.addComponents();
    }

    @Override
    public void showDialog() {
        setVisible(true);
    }

    @Override
    public void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void addComponents() {
        this.add(this.panel, BorderLayout.CENTER);
        this.add(this.menu, BorderLayout.SOUTH);
    }
    public void repaintPanel(){
        this.panel.repaint();
    }

    public List<Point> getSelectedPoints(){
        return this.selectedPoints;
    }

    private void setFrameSize(){
        Dimension newDimension = DisplayInfo.getScaledImageDimension(
                new Dimension(this.image.getBufferedImage().getWidth(this.panel),
                        this.image.getBufferedImage().getHeight(this.panel)),
                DisplayInfo.getDisplaySize(80));
        setSize((int)newDimension.getWidth(), (int)newDimension.getHeight());
        setMinimumSize(newDimension);
    }

    public void setSelectedPoints(List<Point> points) {
        this.selectedPoints = points;
    }

    public void removeSelectedPoint(Point point) {
        this.selectedPoints.remove(point);
    }

    public void addSelectedPoint(Point point) {
        this.selectedPoints.add(point);
    }

    public void clearSelectedPoints() {
        this.selectedPoints.clear();
    }
}
