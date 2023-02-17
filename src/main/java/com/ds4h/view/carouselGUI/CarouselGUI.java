package com.ds4h.view.carouselGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.mainGUI.PreviewImagesPane;
import com.ds4h.view.overlapImages.OverlapImagesGUI;
import com.ds4h.view.saveImagesGUI.SaveImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class CarouselGUI extends JFrame implements StandardGUI {
    private static final long serialVersionUID = 1L;
    private final List<ImagePlus> images;
    private final CarouselPanel panel;
    private final JMenuBar menuBar;
    private final JMenu settings;
    private final JMenu save;
    private final SaveImagesGUI saveGui;
    private final JMenuItem overlappedItem, saveItem;
    private int currentImage;
    private final AlignmentControllerInterface controller;
    private final CornerController cornerController;
    private final PreviewImagesPane previewImagesPane;

    public CarouselGUI(final AlignmentControllerInterface controller, final CornerController cornerController, final PreviewImagesPane previewImagesPane) {
        this.setTitle("Final Alignment Result");
        this.panel = new CarouselPanel();
        this.previewImagesPane = previewImagesPane;
        this.controller = controller;
        this.cornerController = cornerController;
        this.images = this.controller.getAlignedImages().stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList());
        this.currentImage = 0;
        this.saveGui = new SaveImagesGUI(this.controller);
        this.menuBar = new JMenuBar();
        this.settings = new JMenu("Settings");
        this.save = new JMenu("Save");
        this.overlappedItem = new JMenuItem("View Overlapped");
        this.saveItem = new JMenuItem("Save Project");
        // Create a timer to automatically change slides
        this.pack();
        this.addListeners();
        this.addComponents();
        this.showDialog();
    }


    private void swipeRight() {
        this.currentImage = (this.currentImage - 1 + this.images.size()) % this.images.size();
        this.panel.repaint();
        this.setSize(this.panel.getPreferredSize());
    }

    private void swipeLeft(){
        this.currentImage = (this.currentImage + 1) % this.images.size();
        this.panel.repaint();
        this.setSize(this.panel.getPreferredSize());
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.overlappedItem.addActionListener(event -> {
            new OverlapImagesGUI(this.controller, this.cornerController, this.previewImagesPane);
            this.dispose();
        });
        this.saveItem.addActionListener(event -> {
            this.saveGui.showDialog();
        });
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                final int key = e.getKeyCode();
                if(key == KeyEvent.VK_L || key == KeyEvent.VK_LEFT){
                    swipeLeft();
                }else if(key == KeyEvent.VK_R || key == KeyEvent.VK_RIGHT){
                    swipeRight();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    @Override
    public void addComponents() {
        this.add(this.panel);
        this.setJMenuBar(this.menuBar);
        this.menuBar.add(this.settings);
        this.menuBar.add(this.save);
        this.settings.add(this.overlappedItem);
        this.save.add(this.saveItem);
        this.setSize(this.panel.getPreferredSize());
    }
    private class CarouselPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the current image
            g.drawImage(images.get(currentImage).getImage(), 0, 0, null);
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

