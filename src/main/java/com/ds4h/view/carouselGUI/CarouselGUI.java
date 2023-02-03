package com.ds4h.view.carouselGUI;

import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CarouselGUI extends JFrame implements StandardGUI {
    private static final long serialVersionUID = 1L;
    private static final int SLIDE_DELAY = 1000; // 5 seconds
    private final List<ImagePlus> images;
    private final CarouselPanel panel;
    private int currentImage;

    public CarouselGUI(final List<ImagePlus> images) {
        this.setTitle("Final Alignment Result");
        this.panel = new CarouselPanel();
        this.images = images;
        this.currentImage = 0;

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

