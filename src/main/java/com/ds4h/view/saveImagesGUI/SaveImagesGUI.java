package com.ds4h.view.saveImagesGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;

public class SaveImagesGUI extends JFrame implements StandardGUI {
    private final JButton saveButton;
    private final SaveImagesPane imagesPane;
    private final JScrollPane scrollPane;
    private final AlignmentControllerInterface controller;
    public SaveImagesGUI(final AlignmentControllerInterface controller){
        this.setSize();
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.scrollPane = new JScrollPane();
        this.saveButton = new JButton("Save");
        this.imagesPane = new SaveImagesPane(this.controller);
        this.imagesPane.showPreviewImages();
        this.addComponents();
        this.addListeners();
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
        this.add(this.imagesPane, BorderLayout.CENTER);
        this.add(this.saveButton, BorderLayout.SOUTH);
    }

    private void setSize(){
        Dimension screenSize = DisplayInfo.getDisplaySize(50);
        int min_width = (int) (screenSize.width);
        int min_height =(int) (screenSize.height);
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
    }
}
