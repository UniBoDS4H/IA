package com.ds4h.view.overlapImages;

import com.ds4h.controller.AlignmentController.ManualAlignmentController.ManualAlignmentController;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;

public class OverlapImagesGUI extends JFrame implements StandardGUI {
    final JPanel panel;
    public OverlapImagesGUI(final ManualAlignmentController controller){
        //TODO: Overlap the images with different opacities.
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                controller.getAlignedImages().forEach(image ->{

                });
            }
        };
        this.addComponents();
        this.addListeners();
        this.showDialog();
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void addComponents() {
        add(this.panel);
        pack();
    }

}
