package com.ds4h.view.configureImageGUI;

import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;

public class ConfigureImagesGUI extends JFrame implements StandardGUI {
    private final JPanel panel;
    private final JColorChooser colorChooser;
    private final JComboBox<Integer> comboBox;
    private final JSlider slider;
    private final JList<String> images;
    public ConfigureImagesGUI(){
        this.setSize(new Dimension(1000, 1000));
        this.panel = new JPanel();
        this.comboBox = new JComboBox<>();
        this.colorChooser = new JColorChooser();
        this.slider = new JSlider(0,1);
        this.images = new JList<>();
        this.addComponents();
        this.addListeners();
        this.showDialog();
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void addComponents() {
        this.panel.add(this.images);
        this.panel.add(this.colorChooser);
        this.panel.add(this.slider);
        this.add(this.panel);
    }
}
