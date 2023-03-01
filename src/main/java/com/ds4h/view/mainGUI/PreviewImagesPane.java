package com.ds4h.view.mainGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imagePoints.ImagePoints;

import javax.swing.*;
import java.awt.*;

public class PreviewImagesPane extends JPanel {
    private final CornerController controller;
    private JPanel currentPanel;
    PreviewImagesPane(final CornerController controller){
        this.controller = controller;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setVisible(true);
    }

    public void showPreviewImages(){
        this.removeAll();
        this.revalidate();
        for (final ImagePoints image : this.controller.getCornerImagesImages()) {
            final PreviewListItem panel = new PreviewListItem(controller, image, this, this.controller.getCornerImagesImages().indexOf(image));
            panel.setPreferredSize(this.getPreferredSize());
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(panel);
        }
        this.revalidate();
    }

    public void clearPanels(){
        this.removeAll();
        this.repaint();
    }

    public void updateList(){
        this.showPreviewImages();
        this.repaint();
    }
    public void setCurrentPanel(JPanel panel) {
        this.currentPanel = panel;
    }
}
