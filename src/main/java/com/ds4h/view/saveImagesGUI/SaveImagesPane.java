package com.ds4h.view.saveImagesGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class SaveImagesPane extends JPanel {

    private final AlignmentControllerInterface controller;
    private JPanel currentPanel;
    private final List<PreviewListSaves> listPanels;
    public SaveImagesPane(final AlignmentControllerInterface controller){
        this.controller = controller;
        this.listPanels = new LinkedList<>();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setVisible(true);
    }

    public void showPreviewImages(){
        this.removeAll();
        this.revalidate();
        for (AlignedImage image : this.controller.getAlignedImages()) {
            final PreviewListSaves panel = new PreviewListSaves(controller, image, this);
            panel.setPreferredSize(this.getPreferredSize());
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(panel);
            this.listPanels.add(panel);
        }
        this.revalidate();
    }

    public List<AlignedImage> getImagesToSave(){
        final List<AlignedImage> images = new LinkedList<>();
        for(PreviewListSaves innerPanel : this.listPanels){
            if(innerPanel.toSave()){
                images.add(innerPanel.getImage());
            }
        }
        return images;
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
