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
    private final JComboBox<String> copyToCombo;
    private final CornerSelectorGUI container;
    public CornerSelectorMenuGUI(CornerController controller, ImageCorners image, CornerSelectorGUI container){
        this.container = container;
        this.image = image;
        this.cornerController = controller;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        String[] options = this.cornerController.getCornerImagesImages().stream()
                .filter(i-> !i.equals(this.image))
                .map(i-> this.cornerController.getCornerImagesImages().indexOf(i)+1 + " - " + i.getFile().getName())
                .collect(Collectors.toList()).toArray(new String[0]);
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
    }
    public void addComponents(){
        this.add(copyToLabel);
        this.add(copyToCombo);
        this.add(copyButton);
        this.add(deleteButton);
    }
}
