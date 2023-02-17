package com.ds4h.view.overlapImages;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.carouselGUI.CarouselGUI;
import com.ds4h.view.configureImageGUI.ConfigureImagesGUI;
import com.ds4h.view.mainGUI.PreviewImagesPane;
import com.ds4h.view.saveImagesGUI.SaveImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

//TODO:ADD DOC
//TODO: ADD THE POSSIBILITY TO SWITCH FROM CAROUSEL AND OVERLAPPED
public class OverlapImagesGUI extends JFrame implements StandardGUI {
    private final ConfigureImagesGUI configureImagesGUI;
    private final JLayeredPane panel;
    private final List<AlignedImage> images;
    private final SaveImagesGUI saveGui;
    private final List<ImagePanel> imagePanels;
    private final JMenuBar menu;
    private final JMenu settingsMenu, saveMenu, reuseMenu;
    private final JMenuItem settingsImages, carouselItem, saveImages, reuseItem;
    private final AlignmentControllerInterface controller;
    private final CornerController cornerController;
    private final PreviewImagesPane previewImagesPane;
    public OverlapImagesGUI(final AlignmentControllerInterface controller, final CornerController cornerController, final PreviewImagesPane previewImagesPane){
        this.setTitle("Final Result");
        this.controller = controller;
        this.previewImagesPane = previewImagesPane;
        this.cornerController = cornerController;
        this.images = controller.getAlignedImages();
        this.imagePanels = new LinkedList<>();
        this.configureImagesGUI = new ConfigureImagesGUI(this.controller);
        this.panel = new JLayeredPane();
        this.menu = new JMenuBar();
        this.settingsMenu = new JMenu("Settings");
        this.saveMenu = new JMenu("Save");
        this.reuseMenu = new JMenu("Reuse");
        this.settingsImages = new JMenuItem("Configure images");
        this.saveImages = new JMenuItem("Save Images");
        this.carouselItem = new JMenuItem("View as Carousel");
        this.reuseItem = new JMenuItem("Reuse as Source");
        this.saveGui = new SaveImagesGUI(this.controller);
        this.addComponents();
        // TODO : ADD THE POSSIBILITY TO CHANGE FOR EACH IMAGE THE OPACITY (DONE) AND THE RGB COLOR

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
        this.saveImages.addActionListener(event -> {
            this.saveGui.showDialog();
        });
        this.carouselItem.addActionListener(event -> {
            new CarouselGUI(this.controller, this.cornerController, this.previewImagesPane).showDialog();
            this.dispose();
        });
        this.reuseItem.addActionListener(event -> {
            try {
                this.cornerController.reuseSource(this.controller.getAlignedImages());
                this.previewImagesPane.showPreviewImages();
                this.dispose();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void addComponents() {
        this.overlapImages();
        this.add(this.panel);
        this.menu.add(this.settingsMenu);
        this.menu.add(this.saveMenu);
        this.menu.add(this.reuseMenu);
        this.settingsMenu.add(this.settingsImages);
        this.settingsMenu.add(this.carouselItem);
        this.saveMenu.add(this.saveImages);
        this.reuseMenu.add(this.reuseItem);
        this.setJMenuBar(this.menu);
        this.setSize( new Dimension(images.stream().map(AlignedImage::getAlignedImage)
                    .max(Comparator.comparingInt(ImagePlus::getWidth)).get().getWidth(),
                images.stream().map(AlignedImage::getAlignedImage)
                        .max(Comparator.comparingInt(ImagePlus::getHeight)).get().getHeight()));
    }

    private void overlapImages(){
        int layer = 0;
        for(AlignedImage image : this.images){
            final ImagePanel imagePanel = new ImagePanel(image.getAlignedImage());
            this.imagePanels.add(imagePanel);
            imagePanel.setBounds(new Rectangle(image.getAlignedImage().getWidth(), image.getAlignedImage().getHeight()));
            imagePanel.setOpaque(false);
            this.panel.add(imagePanel, new Integer(layer++));
        }
    }
    public class ImagePanel extends JPanel {
        private ImagePlus alignedImage;
        private final ImagePlus clearImage;
        public final static float DEFAULT_OPACITY = 0.5f;
        private float opacity = DEFAULT_OPACITY;

        public ImagePanel(final ImagePlus image) {
            this.clearImage = image;
            this.alignedImage = image;
            this.setSize(this.getPreferredSize());
        }

        public void changeColor(final Color color){
            final BufferedImage img = this.clearImage.getBufferedImage();
            final BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < img.getWidth(); i++){
                for(int j = 0; j < img.getHeight(); j++){
                    Color c = new Color(img.getRGB(i,j));
                    int r = c.getRed() + color.getRed();
                    int g = c.getGreen() + color.getGreen();;
                    int b = c.getBlue() + color.getBlue();;
                    int a = c.getAlpha();
                    Color nc = new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255), a);
                    image.setRGB(i, j, nc.getRGB());
                }
            }
            this.alignedImage = new ImagePlus(this.alignedImage.getTitle(), image);
            this.repaint();
        }
        public float getOpacity(){
            return this.opacity;
        }
        public ImagePlus getImagePlus(){
            return this.alignedImage;
        }

        public void resetImage(){
            this.alignedImage = this.clearImage;
            this.repaint();
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
            g2d.drawImage(this.alignedImage.getImage(), 0, 0, null);
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
