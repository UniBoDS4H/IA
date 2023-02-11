package com.ds4h.view.saveImagesGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;

public class PreviewListSaves extends JPanel implements StandardGUI {
    private final JButton removeButton;
    private final JLabel imageLabel;
    private final JLabel removeLabel;
    private final AlignmentControllerInterface controller;
    private final AlignedImage image;
    private final SaveImagesPane container;

    public PreviewListSaves(final AlignmentControllerInterface controller, final AlignedImage image, final SaveImagesPane container){
        this.container = container;
        this.controller = controller;
        this.image = image;
        this.removeButton = new JButton("REMOVE");
        this.removeLabel = new JLabel("INCLUDED IN THE SAVING");
        this.imageLabel = new JLabel(new ImageIcon(this.image.getAlignedImage().getBufferedImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //we set the Target label visible only if this is the taret image
        this.setVisibilityTargetLabel();
        this.setSelectedPanel();
        this.addComponents();
        this.addListeners();

    }
    private void setVisibilityTargetLabel(){

    }
    private void setSelectedPanel(){

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void addListeners() {
        this.removeButton.addActionListener(event -> {
            if(this.removeButton.getBackground() == Color.RED) {
                this.removeLabel.setText("INCLUDED IN THE SAVING");
                this.removeButton.setBackground(Color.GREEN);
            }else{
                this.removeLabel.setText("EXCLUDED FROM SAVING");
                this.removeButton.setBackground(Color.RED);
            }
        });
    }

    @Override
    public void addComponents() {
        this.add(this.imageLabel);
        this.add(this.removeButton);
        this.add(this.removeLabel);
    }
}
