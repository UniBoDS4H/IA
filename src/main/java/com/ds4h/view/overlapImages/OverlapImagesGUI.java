package com.ds4h.view.overlapImages;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.configureImageGUI.ConfigureImagesGUI;
import com.ds4h.view.exportGUI.ExportZipGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class OverlapImagesGUI extends JFrame implements StandardGUI {
    private final ConfigureImagesGUI configureImagesGUI;
    private ExportZipGUI exportZipGUI;
    private final JLayeredPane panel;
    private final List<AlignedImage> images;
    private final List<ImagePanel> imagePanels;
    private final JMenuBar menu;
    private final JMenu settingsMenu, export;
    private final JMenuItem settingsImages, exportZip;
    private final AlignmentControllerInterface controller;
    public OverlapImagesGUI(final AlignmentControllerInterface controller){
        this.setTitle("Final Result");
        this.controller = controller;
        this.images = controller.getAlignedImages();
        this.imagePanels = new LinkedList<>();
        this.configureImagesGUI = new ConfigureImagesGUI(this.controller);
        this.panel = new JLayeredPane();
        this.menu = new JMenuBar();
        this.settingsMenu = new JMenu("Settings");
        this.export = new JMenu("Export");
        this.settingsImages = new JMenuItem("Configure images");
        this.exportZip = new JMenuItem("Export as zip");
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
        this.exportZip.addActionListener(event -> {
            new ExportZipGUI();
        });
    }

    @Override
    public void addComponents() {
        this.overlapImages();
        this.add(this.panel);
        this.menu.add(this.settingsMenu);
        this.menu.add(this.export);
        this.settingsMenu.add(this.settingsImages);
        this.export.add(this.exportZip);
        this.setJMenuBar(this.menu);
        this.setSize( new Dimension(images.stream().map(AlignedImage::getAlignedImage)
                    .max(Comparator.comparingInt(ImagePlus::getWidth)).get().getWidth(),
                images.stream().map(AlignedImage::getAlignedImage)
                        .max(Comparator.comparingInt(ImagePlus::getHeight)).get().getHeight()));
    }

    private void overlapImages(){
        int layer = 0;
        for(AlignedImage image : this.images){
            final ImagePanel imagePanel = new ImagePanel(image);
            this.imagePanels.add(imagePanel);
            imagePanel.setBounds(new Rectangle(image.getAlignedImage().getWidth(), image.getAlignedImage().getHeight()));
            imagePanel.setOpaque(false);
            this.panel.add(imagePanel, new Integer(layer++));
        }
    }
    public class ImagePanel extends JPanel {
        private final AlignedImage alignedImage;
        public final static float DEFAULT_OPACITY = 0.2f;
        private float opacity = DEFAULT_OPACITY;

        public ImagePanel(final AlignedImage image) {
            this.alignedImage = image;
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
            return this.alignedImage.getAlignedImage();
        }
        public AlignedImage getImage(){
            return this.alignedImage;
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
            g2d.drawImage(this.alignedImage.getAlignedImage().getImage(), 0, 0, null);
            g2d.dispose();
        }
        @Override
        public Dimension getPreferredSize() {
            if (images.isEmpty()) {
                return new Dimension(100, 100);
            } else {
                return new Dimension(images.stream()
                            .map(AlignedImage::getAlignedImage)
                            .max(Comparator.comparingInt(ImagePlus::getWidth)).get().getWidth(),
                        images.stream()
                                .map(AlignedImage::getAlignedImage)
                                .max(Comparator.comparingInt(ImagePlus::getHeight)).get().getHeight());
            }
        }
    }
}
