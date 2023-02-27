package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class CornerSelectorMenuGUI extends JPanel {
    private final CornerController cornerController;
    private final ImageCorners image;
    private final JButton deleteButton;
    private final JLabel copyToLabel;
    private final JButton copyButton;
    private final JButton cornerSetting;
    private final JComboBox<MenuItem> copyToCombo;
    private final CornerSelectorGUI container;
    private final CornerSelectorSettingsGUI settings;
    public CornerSelectorMenuGUI(CornerController controller, ImageCorners image, CornerSelectorGUI container){
        this.container = container;
        this.image = image;
        this.cornerController = controller;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        final MenuItem[] options = this.cornerController.getCornerImagesImages().stream()
                .filter(i -> !i.equals(this.image)).map(i -> this.cornerController.getMenuItem(i)).toArray(MenuItem[]::new);
        this.copyToCombo = new JComboBox<>(options);
        this.copyToCombo.setEditable(false);
        this.copyToCombo.setSelectedIndex(0);
        this.copyToLabel = new JLabel("Copy to");
        this.deleteButton = new JButton("Delete");
        this.copyButton = new JButton("Copy");
        this.cornerSetting = new JButton("Settings");
        this.settings = new CornerSelectorSettingsGUI(container);

        this.addComponents();
        this.addListeners();
    }
    public void addListeners() {
        this.deleteButton.addActionListener(e->{
            container.getSelectedPoints().forEach(image::removeCorner);
            container.clearSelectedPoints();
            container.repaintPanel();
        });
        this.copyButton.addActionListener(e->{
            MenuItem item = (MenuItem) copyToCombo.getSelectedItem();
            if(!cornerController.copyCorners(container.getSelectedPoints(), item.getImage())){
                JOptionPane.showMessageDialog(CornerSelectorMenuGUI.this, "Some of the points are out of the selected image, they have not been copied");
            }else{
                JOptionPane.showMessageDialog(CornerSelectorMenuGUI.this, "Successfully copied " + container.getSelectedPoints().size() + " points.");
            }
        });
        this.cornerSetting.addActionListener(e->{
            this.settings.showDialog();
        });
    }
    public void addComponents(){
        this.add(this.copyToLabel);
        this.add(this.copyToCombo);
        this.add(this.copyButton);
        this.add(this.deleteButton);
        this.add(this.cornerSetting);
    }
    public void updateView(){
        this.copyButton.setEnabled(this.container.getSelectedPoints().size()!=0);
        this.copyToCombo.setEnabled(this.container.getSelectedPoints().size()!=0);
        this.deleteButton.setEnabled(this.container.getSelectedPoints().size()!=0);
    }
}
