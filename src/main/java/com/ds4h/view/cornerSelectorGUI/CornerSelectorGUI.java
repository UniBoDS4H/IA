package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CornerSelectorGUI extends Frame implements StandardGUI {

    private final CornerSelectorPanelGUI panel;
    private ImageCorners image;
    public CornerSelectorGUI(ImageCorners image, CornerController controller){
        this.image = image;
        this.panel = new CornerSelectorPanelGUI();
        this.panel.setCurrentImage(image);
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
        this.add(this.panel);
    }
    private void setFrameSize(){
        Dimension newDimension = DisplayInfo.getScaledImageDimension(
                new Dimension(this.image.getImage().getBufferedImage().getWidth(this.panel),
                        this.image.getImage().getBufferedImage().getHeight(this.panel)),
                DisplayInfo.getDisplaySize(80));
        setSize((int)newDimension.getWidth(), (int)newDimension.getHeight());
        setMinimumSize(newDimension);
    }
}
