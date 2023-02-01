package com.ds4h.view.overlapImages;

import com.ds4h.controller.AlignmentController.ManualAlignmentController.ManualAlignmentController;
import com.ds4h.view.configureImageGUI.ConfigureImagesGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;
import javax.swing.ImageIcon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OverlapImagesGUI extends JFrame implements StandardGUI {
    private final JLayeredPane panel;
    private final List<ImagePlus> images;
    private final JMenuBar menu;
    private final JMenu settingsMenu;
    private final JMenuItem settingsImages;
    public OverlapImagesGUI(final List<ImagePlus> images){
        this.setTitle("Final Result");
        this.images = images;
        this.panel = new JLayeredPane();
        this.menu = new JMenuBar();
        this.settingsMenu = new JMenu("Settings");
        this.settingsImages = new JMenuItem("configure images");
        this.addComponents();
        // TODO : ADD THE POSSIBILITY TO CHANGE FOR EACH IMAGE THE OPACITY AND THE RGB COLOR


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
        this.settingsImages.addActionListener(event -> {
            new ConfigureImagesGUI();
        });
    }

    @Override
    public void addComponents() {
        this.overlapImages();
        this.add(this.panel);
        this.menu.add(this.settingsMenu);
        this.settingsMenu.add(this.settingsImages);
        this.setJMenuBar(this.menu);
        this.setSize(new Dimension(this.images.get(0).getWidth(), this.images.get(0).getHeight()));
    }

    private void overlapImages(){
        int layer = 0;
        for(ImagePlus image : this.images){
            final ImagePanel imagePanel = new ImagePanel(image.getImage());
            imagePanel.setBounds(new Rectangle(image.getWidth(), image.getHeight()));
            imagePanel.setOpaque(false);
            imagePanel.setOpacity(0.2f);
            this.panel.add(imagePanel, new Integer(layer++));
        }
    }
    private class ImagePanel extends JPanel {
        private final Image image;
        private float opacity = 0.1f;

        public ImagePanel(final Image image) {
            this.image = image;
            this.setSize(this.getPreferredSize());
        }

        public void setOpacity(final float opacity) {
            this.opacity = opacity;
        }

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, this.opacity));
            g2d.drawImage(this.image, 0, 0, null);
            g2d.dispose();
        }
        @Override
        public Dimension getPreferredSize() {
            if (images.isEmpty()) {
                return new Dimension(100, 100);
            } else {
                return new Dimension(images.get(0).getWidth(), images.get(0).getHeight());
            }
        }
    }
}
