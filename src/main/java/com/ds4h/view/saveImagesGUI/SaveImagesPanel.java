package com.ds4h.view.saveImagesGUI;

import com.ds4h.controller.imageController.ImageController;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.view.displayInfo.DisplayInfo;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class SaveImagesPanel extends JPanel {
    private final ImageController controller;
    private final JScrollPane scrollPane;
    JPanel innerPanel;
    public SaveImagesPanel(final ImageController controller){
        this.controller = controller;
        this.scrollPane = new JScrollPane();
        this.innerPanel = new JPanel();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setVisible(true);
    }
    public void showPreviewImages() {
        this.removeAll();
        this.revalidate();
        this.innerPanel.removeAll();

        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        this.controller.getAlignedImages().forEach(image->{
            final SaveListItem panel = new SaveListItem(image, this.controller.getAlignedImages().indexOf(image)+1);
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            final Dimension screenSize = DisplayInfo.getDisplaySize(80);
            panel.setPreferredSize(new Dimension(0,screenSize.height/8));
            innerPanel.add(panel);
        });
        scrollPane.setViewportView(innerPanel);
        this.add(scrollPane);
        this.revalidate();
        this.repaint();
    }

    public List<AlignedImage> getImagesToSave() {
        final List<AlignedImage> images = new LinkedList<>();
        for (final Component panel : this.innerPanel.getComponents()) {
            if (((SaveListItem)panel).toSave()) {
                images.add(((SaveListItem)panel).getImage());
            }
        }
        return images;
    }
}
