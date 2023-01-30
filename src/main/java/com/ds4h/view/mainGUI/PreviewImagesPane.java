package com.ds4h.view.mainGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Arrays;

public class PreviewImagesPane extends JPanel {
    private final CornerController controller;
    private JPanel currentPanel;
    PreviewImagesPane(CornerController controller){
        this.controller = controller;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setVisible(true);
    }

    public void showPreviewImages(){
        this.removeAll();
        this.revalidate();
        for (ImageCorners image : this.controller.getCornerImagesImages()) {
            PreviewListItem panel = new PreviewListItem(controller, image, this);
            panel.setPreferredSize(this.getPreferredSize());
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(panel);
        }
        this.revalidate();
    }

    public void updateList(){
        //rightPanel.setCurrentImage(image.getImage());
        this.showPreviewImages();
        this.repaint();
    }
    public void setCurrentPanel(JPanel panel) {
        this.currentPanel = panel;
    }
    public JPanel getCurrentPanel(){
        return this.currentPanel;
    }

}
