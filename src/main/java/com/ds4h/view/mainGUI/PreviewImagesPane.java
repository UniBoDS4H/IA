package com.ds4h.view.mainGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;

import javax.swing.*;
import java.awt.*;

public class PreviewImagesPane extends JPanel {
    private final CornerController controller;
    private final MainMenuGUI container;
    private JPanel currentPanel;
    PreviewImagesPane(CornerController controller, MainMenuGUI container){
        this.container = container;
        this.controller = controller;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setVisible(true);
    }

    public MainMenuGUI getMainMenu(){
        return this.container;
    }
    public void showPreviewImages(){
        this.removeAll();
        this.revalidate();
        for (ImageCorners image : this.controller.getCornerImagesImages()) {
            PreviewListItem panel = new PreviewListItem(controller, image, this, this.controller.getCornerImagesImages().indexOf(image));
            panel.setPreferredSize(this.getPreferredSize());
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(panel);
        }
        this.revalidate();
    }

    public void updateList(){
        this.showPreviewImages();
        this.repaint();
    }
    public void setCurrentPanel(JPanel panel) {
        this.currentPanel = panel;
    }
}
