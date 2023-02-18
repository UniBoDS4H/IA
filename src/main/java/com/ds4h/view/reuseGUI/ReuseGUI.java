package com.ds4h.view.reuseGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.controller.savingController.SaveController;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.PreviewImagesPane;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.IJ;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class ReuseGUI extends JFrame implements StandardGUI {
    private final JButton reuseButton;
    private final JFileChooser fileChooser;
    private final ReuseImagesPanel imagesPane;
    private final JScrollPane scrollPane;
    private boolean done = false;
    private final AlignmentControllerInterface controller;
    private final CornerController cornerController;
    private final PreviewImagesPane previewImagesPane;
    public ReuseGUI(final PreviewImagesPane previewImagesPane, CornerController cornerController, final AlignmentControllerInterface controller){
        this.setSize();
        this.previewImagesPane = previewImagesPane;
        this.cornerController = cornerController;
        this.controller = controller;
        this.fileChooser = new JFileChooser();
        this.setLayout(new BorderLayout());
        this.scrollPane = new JScrollPane();
        this.reuseButton = new JButton("Reuse");
        this.imagesPane = new ReuseImagesPanel(this.controller);
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
        this.reuseButton.addActionListener(event -> {
            try {
                this.cornerController.reuseSource(this.imagesPane.getImagesToReuse());
                this.previewImagesPane.showPreviewImages();
                this.dispose();
                this.done = true;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isDone(){
        return this.done;
    }

    @Override
    public void addComponents() {
        this.add(this.imagesPane, BorderLayout.CENTER);
        this.add(this.reuseButton, BorderLayout.SOUTH);
    }

    private void setSize(){
        Dimension screenSize = DisplayInfo.getDisplaySize(50);
        int min_width = (int) (screenSize.width);
        int min_height =(int) (screenSize.height);
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
    }
}
