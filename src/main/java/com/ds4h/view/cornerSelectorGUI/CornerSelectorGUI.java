package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;
import org.opencv.core.Point;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class CornerSelectorGUI extends ImageWindow {

    private final PointController pointController;
    private final CornerSelectorPanelGUI panel = null;
    private final ImagePoints image;
    private List<Point> selectedPoints;
    private final CornerSelectorMenuGUI menu;
    private final MainMenuGUI mainMenu;


    public CornerSelectorGUI(final ImagePoints image, final PointController controller, MainMenuGUI mainMenu){
        super("JJ");
        //super(image.getImage(), new CornerSelectorPanelGUI(null, image.getImage()));
        this.mainMenu = mainMenu;
        this.pointController = controller;
        this.image = image;
        //TODO: use the RENDERIMAGE in order to apply contrast and other options
        //this.panel = new CornerSelectorPanelGUI(this, image.getImage());
        //this.panel.setCurrentImage(image);
        this.menu = new CornerSelectorMenuGUI(this.pointController, this.image, this);
        this.selectedPoints = new ArrayList<>();
        setLayout(new BorderLayout());
        this.setFrameSize();
        this.addListeners();
        this.addComponents();
    }

    public void showDialog() {
        setVisible(true);
    }

    public void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    public void addComponents() {
        this.add(this.menu, BorderLayout.SOUTH);
    }
    public void repaintPanel(){

    }

    public List<Point> getSelectedPoints(){
        return this.selectedPoints;
    }

    private void setFrameSize(){
        /*final Dimension newDimension = DisplayInfo.getScaledImageDimension(
                new Dimension(this.image.getBufferedImage().getWidth(this.panel),
                        this.image.getBufferedImage().getHeight(this.panel)),
                DisplayInfo.getDisplaySize(80));
        newDimension.setSize(newDimension.getWidth()+this.menu.getWidth(), newDimension.getHeight()+this.menu.getHeight());
        setSize((int)newDimension.getWidth(), (int)newDimension.getHeight());
        setMinimumSize(newDimension);

         */
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
       // this.panel.setPointerColor(selectedColor);
    }

    public void setSelectedPointerColor(Color selectedColor) {
       // this.panel.setSelectedPointerColor(selectedColor);
    }

    public void setTextColor(Color selectedColor) {
       // this.panel.setTextColor(selectedColor);
    }

    public CornerSelectorPanelGUI getCornerPanel() {
        return this.panel;
    }
    public ImagePoints getImage(){
        return this.image;
    }
    public void setImage(final ImagePlus imagePlus){
        //this.image.useProcessed();
        //ImagingConversion.fromImagePlus2Mat(imagePlus).ifPresent(this.image::setProcessedImage);
        this.panel.setCurrentImage(this.image);
        this.repaintPanel();
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
    private static class RenderImage{
        private final ImagePoints ImagePoints;
        private ImagePlus imagePlus;

        public RenderImage(final ImagePoints ImagePoints, final ImagePlus imagePlus){
            this.ImagePoints = ImagePoints;
            this.imagePlus = imagePlus;
        }

        public void setImage(final ImagePlus imagePlus){
            this.imagePlus = imagePlus;
        }

        public ImagePoints getImagePoints(){
            return this.ImagePoints;
        }
    }
}
