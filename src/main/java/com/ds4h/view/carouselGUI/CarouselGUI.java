package com.ds4h.view.carouselGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.controller.imageController.ImageController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.PreviewImagesPane;
import com.ds4h.view.overlapImages.OverlapImagesGUI;
import com.ds4h.view.reuseGUI.ReuseGUI;
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
    private  final JLabel label;
    private final JPanel labelPanel;
    private final JMenuBar menuBar;
    private final JMenu settings;
    private final JMenu save, reuse;
    private final SaveImagesGUI saveGui;
    private final JMenuItem overlappedItem, saveItem, reuseItem;
    private int currentImage;
    private final ImageController controller;
    private final PointController pointController;
    private final PreviewImagesPane previewImagesPane;
    private final int max_number;

    private final BunwarpjGUI bunwarpjGUI;
    public CarouselGUI(final String algorithm, final BunwarpjGUI bunwarpjGUI, final ImageController controller, final PointController pointController, final PreviewImagesPane previewImagesPane) {
        this.setTitle("Final Alignment: " + algorithm);
        this.panel = new CarouselPanel();
        this.bunwarpjGUI = bunwarpjGUI;
        this.previewImagesPane = previewImagesPane;
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.pointController = pointController;
        this.labelPanel = new JPanel();
        this.images = this.controller.getAlignedImages().stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList());
        this.max_number = this.images.size();
        this.label = new JLabel("1/"+this.max_number);
        this.currentImage = 0;
        this.saveGui = new SaveImagesGUI(this.controller);
        this.menuBar = new JMenuBar();
        this.settings = new JMenu("Settings");
        this.reuse = new JMenu("Reuse");
        this.save = new JMenu("Save");
        this.reuseItem = new JMenuItem("Reuse as resource");
        this.overlappedItem = new JMenuItem("View Overlapped");
        this.saveItem = new JMenuItem("Save Project");
        //setSize((int)this.panel.getPreferredSize().getWidth()+ this.menuBar.getWidth(), (int)this.panel.getPreferredSize().getHeight()+this.menuBar.getHeight());

        // Create a timer to automatically change slides

        this.addListeners();
        this.addComponents();
        this.showDialog();
        this.pack();

    }


    private void swipeRight() {
        this.currentImage = (this.currentImage - 1 + this.images.size()) % this.images.size();
        this.label.setText((this.currentImage+1) + "/" + this.max_number);
        this.panel.repaint();
    }

    private void swipeLeft(){
        this.currentImage = (this.currentImage + 1) % this.images.size();
        this.label.setText((this.currentImage+1) + "/" + this.max_number);
        this.panel.repaint();
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.overlappedItem.addActionListener(event -> {
            new OverlapImagesGUI(this.controller.name() ,this.bunwarpjGUI, this.controller, this.pointController, this.previewImagesPane).showDialog();
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
                    swipeRight();
                }else if(key == KeyEvent.VK_R || key == KeyEvent.VK_RIGHT){
                    swipeLeft();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        this.reuseItem.addActionListener(event -> {
            final ReuseGUI reuseGUI = new ReuseGUI(this.previewImagesPane, this.pointController, this.controller);
            reuseGUI.showDialog();
        });
    }

    @Override
    public void addComponents() {
        this.add(this.panel, BorderLayout.CENTER);
        this.labelPanel.add(this.label);
        this.add(this.labelPanel, BorderLayout.SOUTH);
        this.setJMenuBar(this.menuBar);
        this.menuBar.add(this.settings);
        this.menuBar.add(this.save);
        this.menuBar.add(this.reuse);
        this.settings.add(this.overlappedItem);
        this.save.add(this.saveItem);
        this.reuse.add(this.reuseItem);
        //this.setSize(this.panel.getPreferredSize());
    }
    private class CarouselPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the current image
            g.drawImage(images.get(currentImage).getImage(), 0, 0, (int)this.getPreferredSize().getWidth(), (int)this.getPreferredSize().getHeight(), null);
        }

        @Override
        public Dimension getPreferredSize() {
            if (images.isEmpty()) {
                return new Dimension(100, 100);
            } else {
                return DisplayInfo.getScaledImageDimension(
                        new Dimension(images.get(0).getWidth(),images.get(0).getHeight()),
                        DisplayInfo.getDisplaySize(80));
            }
        }
    }
}

