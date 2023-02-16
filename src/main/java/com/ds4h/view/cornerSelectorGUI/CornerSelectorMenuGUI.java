package com.ds4h.view.cornerSelectorGUI;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;
import javax.swing.*;
import java.awt.*;

public class CornerSelectorMenuGUI extends JPanel {
    private final CornerController cornerController;
    private final ImageCorners image;
    private final JButton deleteButton;
    private final JLabel copyToLabel;
    private final JButton copyButton;
    private final JButton cornerSetting;
    private final JButton editIndex;
    private final JComboBox<ImageCorners> copyToCombo;
    private final CornerSelectorGUI container;
    public CornerSelectorMenuGUI(CornerController controller, ImageCorners image, CornerSelectorGUI container){
        this.container = container;
        this.image = image;
        this.cornerController = controller;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        ImageCorners[] options = this.cornerController.getCornerImagesImages().stream()
                .filter(i -> !i.equals(this.image)).toArray(ImageCorners[]::new);
        this.copyToCombo = new JComboBox<>(options);
        this.copyToCombo.setEditable(false);
        this.copyToCombo.setSelectedIndex(0);
        this.copyToLabel = new JLabel("Copy to");
        this.deleteButton = new JButton("Delete");
        this.copyButton = new JButton("Copy");
        this.cornerSetting = new JButton("Corner Settings");
        this.editIndex = new JButton("Edit");

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
            ImageCorners img = (ImageCorners)copyToCombo.getSelectedItem();
            container.getSelectedPoints().forEach(p-> img.addCorner(p));
        });
        this.cornerSetting.addActionListener(e->{
            new CornerSelectorSettingsGUI(container).showDialog();
        });
        this.editIndex.addActionListener(e->{
            container.getCornerPanel().editPoint();
        });
    }
    public void addComponents(){
        this.add(this.copyToLabel);
        this.add(this.copyToCombo);
        this.add(this.copyButton);
        this.add(this.deleteButton);
        this.add(this.cornerSetting);
        this.add(this.editIndex);
    }
}
