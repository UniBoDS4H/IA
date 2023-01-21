package com.ds4h.view;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import ij.ImagePlus;
import java.awt.Panel;
import java.awt.FlowLayout;
import java.awt.Button;

public class CornerSelectorGUI extends Panel implements ActionListener {

    private List<ImagePlus> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private Button[] buttons;

    public CornerSelectorGUI() {
        setLayout(new FlowLayout());
    }

    public void loadImages(ImagePlus[] images) {
        for (ImagePlus image : images) {
            this.images.add(image);
            Button button = new Button(image.getTitle());
            button.addActionListener(this);
            add(button);
        }
        setPreferredSize(new Dimension(images[0].getWidth(), images[0].getHeight()));
        repaint();
    }

    public void paint(Graphics g) {
        g.drawImage(images.get(currentImageIndex).getBufferedImage(), 0, 0, this);
    }

    public void actionPerformed(ActionEvent e) {
        Button button = (Button) e.getSource();
        for (int i = 0; i < images.size(); i++) {
            if (button.getLabel().equals(images.get(i).getTitle())) {
                currentImageIndex = i;
                repaint();
                break;
            }
        }
    }
}
