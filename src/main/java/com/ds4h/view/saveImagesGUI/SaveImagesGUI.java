package com.ds4h.view.saveImagesGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.imageController.ImageController;
import com.ds4h.controller.savingController.SaveController;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.loadingGUI.LoadingGUI;
import com.ds4h.view.loadingGUI.LoadingType;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.IJ;
import ij.gui.ImageWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;

    public class SaveImagesGUI extends JFrame implements StandardGUI {
    private final JButton saveButton;
    private final SaveImagesPanel imagesPane;
    private final JFileChooser fileChooser;
    private final ImageController controller;
    public SaveImagesGUI(final ImageController controller){
        this.setTitle("Save");
        this.setSize();
        this.controller = controller;
        this.fileChooser = new JFileChooser();
        this.setLayout(new BorderLayout());
        this.saveButton = new JButton("Save");
        this.imagesPane = new SaveImagesPanel(this.controller);
        this.addComponents();
        this.addListeners();
    }

    @Override
    public void showDialog() {
        this.imagesPane.showPreviewImages();
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.saveButton.addActionListener(event -> {
            LoadingGUI loadingGUI = new LoadingGUI(LoadingType.SAVE);
            if(this.imagesPane.getImagesToSave().size() > 0) {
                this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                final int result = this.fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    final File selectedDirectory = this.fileChooser.getSelectedFile();
                    loadingGUI.showDialog();
                    try {
                        SaveController.saveImages(this.imagesPane.getImagesToSave(), selectedDirectory.getPath());
                        this.dispose();
                        loadingGUI.close();
                    } catch (Exception e) {
                        loadingGUI.close();
                        JOptionPane.showMessageDialog(this,
                                e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(this,
                        "You must pick at least one image.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    @Override
    public void addComponents() {
        this.add(this.imagesPane, BorderLayout.CENTER);
        this.add(this.saveButton, BorderLayout.SOUTH);
    }

    private void setSize(){
        final Dimension screenSize = DisplayInfo.getDisplaySize(80);
        final int min_width = (screenSize.width/5);
        final int min_height =(screenSize.height/2);
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
    }
}
