package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import org.opencv.core.Point;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class CornerSelectorGUI extends Frame implements StandardGUI {

    private final CornerController cornerController;
    private final CornerSelectorPanelGUI panel;
    private final ImageCorners image;
    private List<Point> selectedPoints;
    private final CornerSelectorMenuGUI menu;
    private final MainMenuGUI mainMenu;


    public CornerSelectorGUI(final ImageCorners image, final CornerController controller, MainMenuGUI mainMenu){
        super(controller.getMenuItem(image).toString());
        this.mainMenu = mainMenu;
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
        this.panel.initZoom();
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
        final Dimension newDimension = DisplayInfo.getScaledImageDimension(
                new Dimension(this.image.getBufferedImage().getWidth(this.panel),
                        this.image.getBufferedImage().getHeight(this.panel)),
                DisplayInfo.getDisplaySize(80));
        newDimension.setSize(newDimension.getWidth()+this.menu.getWidth(), newDimension.getHeight()+this.menu.getHeight());
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

    public void setPointerColor(Color selectedColor) {
        this.panel.setPointerColor(selectedColor);
    }

    public void setSelectedPointerColor(Color selectedColor) {
        this.panel.setSelectedPointerColor(selectedColor);
    }

    public void setTextColor(Color selectedColor) {
        this.panel.setTextColor(selectedColor);
    }

    public CornerSelectorPanelGUI getCornerPanel() {
        return this.panel;
    }
    public ImageCorners getImage(){
        return this.image;
    }

    public void updateMenu() {
        this.menu.updateView();
    }

    public void updateSettings() {
        this.menu.updateSettings();
    }
    public void updatePointsForAlignment(){
        System.out.println("A");
        this.mainMenu.checkPointsForAlignment();
    }
}
