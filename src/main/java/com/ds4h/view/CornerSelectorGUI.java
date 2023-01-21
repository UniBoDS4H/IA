package com.ds4h.view;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.awt.Panel;
import javax.imageio.ImageIO;

public class CornerSelectorGUI extends Panel {

    private Image image;

    public CornerSelectorGUI() {
    }

    public void loadImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

}
