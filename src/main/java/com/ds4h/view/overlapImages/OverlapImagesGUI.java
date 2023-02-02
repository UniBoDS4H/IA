package com.ds4h.view.overlapImages;

import com.ds4h.controller.AlignmentController.ManualAlignmentController.ManualAlignmentController;
import com.ds4h.view.configureImageGUI.ConfigureImagesGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;
import javax.swing.ImageIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class OverlapImagesGUI extends JFrame implements StandardGUI {
    private final ConfigureImagesGUI configureImagesGUI;
    private final JLayeredPane panel;
    private final List<ImagePlus> images;
    private final List<ImagePanel> imagePanels;
    private final JMenuBar menu;
    private final JMenu settingsMenu;
    private final JMenuItem settingsImages;
    public OverlapImagesGUI(final List<ImagePlus> images){
        this.setTitle("Final Result");
        this.images = images;
        this.imagePanels = new LinkedList<>();
        this.configureImagesGUI = new ConfigureImagesGUI(this.images);
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
            this.configureImagesGUI.setElements(this.imagePanels);
            this.configureImagesGUI.showDialog();
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
            final ImagePanel imagePanel = new ImagePanel(image);
            this.imagePanels.add(imagePanel);
            imagePanel.setBounds(new Rectangle(image.getWidth(), image.getHeight()));
            imagePanel.setOpaque(false);
            this.panel.add(imagePanel, new Integer(layer++));
        }
    }
    public class ImagePanel extends JPanel {
        private ImagePlus image;
        public final static float DEFAULT_OPACITY = 0.2f;
        private float opacity = DEFAULT_OPACITY;

        public ImagePanel(final ImagePlus image) {
            this.image = image;
            this.prova();
            this.setSize(this.getPreferredSize());
        }

        private void prova(){
            /*
            BufferedImage originalImage = this.image.getBufferedImage();
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < this.image.getWidth(); i++) {
                for (int j = 0; j < this.image.getHeight(); j++) {
                    if(originalImage.getRGB(i,j) == Color.BLACK.getRGB()) {
                        newImage.setRGB(i, j, Color.cyan.getRGB());
                    }
                }
            }

            this.image = new ImagePlus("Colored Image", newImage);
            this.image.show();

             */
        }

        public float getOpacity(){
            return this.opacity;
        }
        public ImagePlus getImagePlus(){
            return this.image;
        }

        public void setOpacity(final float opacity) {
            this.opacity = opacity;
            this.repaint();
        }

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, this.opacity));
            g2d.drawImage(this.image.getImage(), 0, 0, null);
            g2d.dispose();
        }
        @Override
        public Dimension getPreferredSize() {
            if (images.isEmpty()) {
                return new Dimension(100, 100);
            } else {
                return new Dimension(images.stream().max(Comparator.comparingInt(ImagePlus::getWidth)).get().getWidth(),
                        images.stream().max(Comparator.comparingInt(ImagePlus::getHeight)).get().getHeight());
            }
        }
    }
}
