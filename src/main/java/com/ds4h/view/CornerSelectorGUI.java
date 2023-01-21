package com.ds4h.view;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Panel;
import java.awt.Image;

public class CornerSelectorGUI extends Panel {

    private Image image;

    public CornerSelectorGUI() {
    }

    public void loadImages(File[] files) {
        try {
            if(files.length>0){
                image = ImageIO.read(files[0]);
                setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
                repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

}
