package com.ds4h.view.overlapImages;

import com.ds4h.controller.changeColorController.ChangeColorController;
import com.ds4h.controller.imageController.ImageEnum;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.controller.imageController.ImageController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.carouselGUI.CarouselGUI;
import com.ds4h.view.configureImageGUI.ConfigureImagesGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.loadingGUI.LoadingGUI;
import com.ds4h.view.mainGUI.PreviewImagesPane;
import com.ds4h.view.reuseGUI.ReuseGUI;
import com.ds4h.view.saveImagesGUI.SaveImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import javax.swing.*;
import java.awt.*;
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
    private final JMenu settingsMenu, saveMenu, reuseMenu, transformMenu;
    private final JMenuItem settingsImages, carouselItem, saveImages, reuseItem, elasticItem;
    private final PointController pointController;
    private final PreviewImagesPane previewImagesPane;
    private final ImageController controller;
    private final BunwarpjGUI bunwarpjGUI;
    public OverlapImagesGUI(final String algorithm, final BunwarpjGUI bunwarpjGUI, final ImageController controller, final PointController pointController, final PreviewImagesPane previewImagesPane){
        this.setTitle("Final Alignment: " + algorithm);
        this.setLayout(new BorderLayout());
        this.controller = controller;
        this.bunwarpjGUI = bunwarpjGUI;
        this.previewImagesPane = previewImagesPane;
        this.pointController = pointController;
        this.images = controller.getAlignedImages();
        this.imagePanels = new LinkedList<>();
        this.configureImagesGUI = null;//new ConfigureImagesGUI(this.controller);
        this.panel = new JLayeredPane();
        this.menu = new JMenuBar();
        this.settingsMenu = new JMenu("Settings");
        this.saveMenu = new JMenu("Save");
        this.transformMenu = new JMenu("Elastic Deformation");
        this.reuseMenu = new JMenu("Reuse");
        this.settingsImages = new JMenuItem("Configure images");
        this.saveImages = new JMenuItem("Save Images");
        this.elasticItem = new JMenuItem("Elastic transformation");
        this.carouselItem = new JMenuItem("View as Carousel");
        this.reuseItem = new JMenuItem("Reuse as Source");
        this.saveGui = new SaveImagesGUI(this.controller);

        this.addComponents();
        this.addListeners();
        this.panel.setPreferredSize(this.imagePanels.get(0).getPreferredSize());
        this.pack();
    }

    public void changeImages(final List<ImagePlus> otherImages){
        int layer = 0;
        for(final ImagePlus image : otherImages){
            final ImagePanel imagePanel = new ImagePanel(image);
            this.imagePanels.add(imagePanel);
            imagePanel.setBounds(new Rectangle(image.getWidth(), image.getHeight()));
            imagePanel.setOpaque(false);
            this.panel.add(imagePanel, new Integer(layer++));
        }
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.settingsImages.addActionListener(event -> {
            this.configureImagesGUI.setElements(this.imagePanels);
            this.configureImagesGUI.showDialog();

        });
        this.saveImages.addActionListener(event -> {
            this.saveGui.showDialog();
        });
        this.carouselItem.addActionListener(event -> {
            new CarouselGUI(this.controller.name(), this.bunwarpjGUI, this.controller, this.pointController, this.previewImagesPane).showDialog();
            this.dispose();
        });
        this.reuseItem.addActionListener(event -> {
            final ReuseGUI reuseGUI = new ReuseGUI(this.previewImagesPane, this.pointController, this.controller);
            reuseGUI.showDialog();
        });
        this.elasticItem.addActionListener(event -> {
            if(!this.controller.deformationIsAlive()) {
                this.controller.elastic(this.controller.getAlignedImages());
                final LoadingGUI loadingGUI = new LoadingGUI();
                final Thread myThread = new Thread(() -> {
                    while (this.controller.deformationIsAlive()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            IJ.showMessage(e.getMessage());
                        }
                    }
                    if(controller.getAlignedImages().size() > 0) {
                        final OverlapImagesGUI bunwarpOverlapped = new OverlapImagesGUI(this.controller.name(), this.bunwarpjGUI, this.controller, this.pointController, this.previewImagesPane);
                        bunwarpOverlapped.showDialog();
                        this.dispose();
                    }
                    loadingGUI.close();
                });

                myThread.start();
            }
        });
    }

    @Override
    public void addComponents() {
        this.overlapImages();
        this.add(this.panel, BorderLayout.CENTER);
        this.menu.add(this.settingsMenu);
        this.menu.add(this.saveMenu);
        this.menu.add(this.reuseMenu);
        this.menu.add(this.transformMenu);
        this.transformMenu.add(this.elasticItem);
        this.settingsMenu.add(this.settingsImages);
        this.settingsMenu.add(this.carouselItem);
        this.saveMenu.add(this.saveImages);
        this.reuseMenu.add(this.reuseItem);
        this.setJMenuBar(this.menu);
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
        private Color color = Color.RED;

        public ImagePanel(final ImagePlus image) {
            this.clearImage = image;
            this.alignedImage = image;
            this.setSize(this.getPreferredSize());
        }

        public void changeColor(final Color color){
            this.color = color;
            this.alignedImage = ChangeColorController.changeColor(this.clearImage, color);
            this.repaint();
        }


        public void setDefaultColor(){
            this.color = Color.RED;
        }

        public Color getColor(){
            return this.color;
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

            final Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, this.opacity));
            g2d.drawImage(this.alignedImage.getImage(), 0, 0, (int)this.getPreferredSize().getWidth(), (int)this.getPreferredSize().getHeight(), null);
            g2d.dispose();
        }
        @Override
        public Dimension getPreferredSize() {
            if (images.isEmpty()) {
                return new Dimension(100, 100);
            } else {
                return DisplayInfo.getScaledImageDimension(
                        new Dimension(images.get(0).getAlignedImage().getWidth(),images.get(0).getAlignedImage().getHeight()),
                        DisplayInfo.getDisplaySize(80));
            }
        }
    }
}
