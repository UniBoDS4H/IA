package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.stream.Collectors;

public class CornerSelectorGUI extends Frame implements StandardGUI {

    private final CornerController cornerController;
    private final CornerSelectorPanelGUI panel;
    private ImageCorners image;
    private final JPanel menu;
    public CornerSelectorGUI(final ImageCorners image, final CornerController controller){
        this.cornerController = controller;
        this.image = image;
        this.panel = new CornerSelectorPanelGUI();
        this.panel.setCurrentImage(image);
        this.menu = new JPanel();
        setLayout(new BorderLayout());
        this.setFrameSize();
        this.addListeners();
        this.addComponents();
    }

    @Override
    public void showDialog() {
        setVisible(true);
    }

    @Override
    public void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void addComponents() {
        this.add(this.panel, BorderLayout.CENTER);
        menu.setLayout(new FlowLayout(FlowLayout.LEFT));

        String[] options = this.cornerController.getCornerImagesImages().stream()
                .filter(i-> !i.equals(this.image))
                .map(i-> this.cornerController.getCornerImagesImages().indexOf(i)+1 + " - " + i.getFile().getName())
                .collect(Collectors.toList()).toArray(new String[0]);
        JComboBox<String> copyToCombo = new JComboBox<>(options);
        copyToCombo.setEditable(false);
        copyToCombo.setSelectedIndex(0);
        //copyToCombo.setPreferredSize(new Dimension(100, 25));

        JLabel copyToLabel = new JLabel("Copy to");
        JButton deleteButton = new JButton("Delete");
        JButton copyButton = new JButton("Copy");


        menu.add(copyToLabel);
        menu.add(copyToCombo);
        menu.add(copyButton);
        menu.add(deleteButton);

        this.add(menu, BorderLayout.SOUTH);
    }
    private void setFrameSize(){
        Dimension newDimension = DisplayInfo.getScaledImageDimension(
                new Dimension(this.image.getBufferedImage().getWidth(this.panel),
                        this.image.getBufferedImage().getHeight(this.panel)),
                DisplayInfo.getDisplaySize(80));
        setSize((int)newDimension.getWidth(), (int)newDimension.getHeight());
        setMinimumSize(newDimension);
    }
}
