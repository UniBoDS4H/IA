package com.ds4h.view.overlapImages;

import com.ds4h.controller.AlignmentController.ManualAlignmentController.ManualAlignmentController;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;
import javax.swing.ImageIcon;

import javax.swing.*;
import java.awt.*;

public class OverlapImagesGUI extends JFrame implements StandardGUI {
    final JLayeredPane panel;
    public OverlapImagesGUI(final ManualAlignmentController controller){
        //TODO: Overlap the images with different opacities.
        this.setTitle("Final Result");
        Dimension screenSize = DisplayInfo.getDisplaySize(80);
        int min_width = (int) (screenSize.width/6);
        int min_height =(int) (screenSize.height);
        // Set the size of the frame to be half of the screen width and height
        // Set the size of the frame to be half of the screen width and height
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
        int layer = 0;
        this.panel = new JLayeredPane();
        for(ImagePlus image : controller.getAlignedImages()){
            final ImagePanel imagePanel = new ImagePanel(image.getImage());
            imagePanel.setBounds(new Rectangle(image.getWidth(), image.getHeight()));
            imagePanel.setOpaque(false);
            imagePanel.setOpacity(0.1f);
            this.panel.add(imagePanel, new Integer(layer++));
        }

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
    static class ImagePanel extends JPanel {
        private final Image image;
        private float opacity = 0.1f;

        public ImagePanel(final Image image) {
            this.image = image;
        }

        public void setOpacity(final float opacity) {
            this.opacity = opacity;
        }

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, this.opacity));
            g2d.fill(new Rectangle(0, 0, 100, 100));
            g2d.drawImage(this.image, 0, 0, null);
            g2d.dispose();
        }
    }
}
