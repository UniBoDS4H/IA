package com.ds4h.view.carouselGUI;

    //TODO:Plot the aligned images in a carousel, navigate the images using the arrows or key as L(left) & R(right)
import com.ds4h.controller.AlignmentController.AutomaticAlignmentController.AutomaticAlignmentController;
import com.ds4h.controller.AlignmentController.ManualAlignmentController.ManualAlignmentController;
import ij.ImagePlus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CarouselGUI extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int SLIDE_DELAY = 1000; // 5 seconds
    private List<ImagePlus> images;
    private int currentImage;
    private final Timer slideTimer;

    public CarouselGUI(ManualAlignmentController m) {
        images = m.getAlignedImages();
        currentImage = 0;

        // Create a timer to automatically change slides
        slideTimer = new Timer(SLIDE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextSlide();
            }
        });
        slideTimer.start();
        JFrame frame = new JFrame("Carousel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the current image
        System.out.println(this.images.size());
        g.drawImage(images.get(currentImage).getImage(), 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        if (images.isEmpty()) {
            return new Dimension(100, 100);
        } else {
            return new Dimension(images.get(0).getWidth(), images.get(0).getHeight());
        }
    }

    private void nextSlide() {
        currentImage = (currentImage + 1) % images.size();
        repaint();
    }
}

