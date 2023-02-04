package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.stream.Collectors;

public class CornerSelectorMenuGUI extends JPanel {
    private final CornerController cornerController;
    private final ImageCorners image;
    private final JButton deleteButton;
    private final JLabel copyToLabel;
    private final JButton copyButton;
    private final JComboBox<ImageCorners> copyToCombo;
    private final CornerSelectorGUI container;
    public CornerSelectorMenuGUI(CornerController controller, ImageCorners image, CornerSelectorGUI container){
        this.container = container;
        this.image = image;
        this.cornerController = controller;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        ImageCorners[] options = this.cornerController.getCornerImagesImages().stream()
                .filter(i-> !i.equals(this.image))
                .collect(Collectors.toList()).toArray(new ImageCorners[0]);
        copyToCombo = new JComboBox<>(options);
        copyToCombo.setEditable(false);
        copyToCombo.setSelectedIndex(0);
        copyToLabel = new JLabel("Copy to");
        deleteButton = new JButton("Delete");
        copyButton = new JButton("Copy");

        this.addComponents();
        this.addListeners();
    }
    public void addListeners() {
        this.deleteButton.addActionListener(e->{
            container.getSelectedPoints().forEach(p-> image.removeCorner(p));
            container.clearSelectedPoints();
            container.repaintPanel();
        });
        this.copyButton.addActionListener(e->{
            ImageCorners img = (ImageCorners)copyToCombo.getSelectedItem();
            container.getSelectedPoints().forEach(p-> img.addCorner(p));
        });
    }
    public void addComponents(){
        this.add(copyToLabel);
        this.add(copyToCombo);
        this.add(copyButton);
        this.add(deleteButton);
    }
}
