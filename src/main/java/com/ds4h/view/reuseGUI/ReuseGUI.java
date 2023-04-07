package com.ds4h.view.reuseGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.controller.imageController.ImageController;
import com.ds4h.view.outputGUI.AlignmentOutputGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class ReuseGUI extends JFrame implements StandardGUI {
    private final JButton reuseButton;
    private final ReuseImagesPanel imagesPane;
    private final MainMenuGUI mainGUI;
    private final ImageController controller;
    private final PointController pointController;
    private final AlignmentOutputGUI outputGUI;

    public ReuseGUI(final PointController pointController, final ImageController controller, MainMenuGUI mainGUI, AlignmentOutputGUI alignmentOutputGUI){
        this.setSize();
        this.mainGUI = mainGUI;
        this.outputGUI = alignmentOutputGUI;
        this.pointController = pointController;
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.reuseButton = new JButton("Reuse");
        this.imagesPane = new ReuseImagesPanel(this.controller);
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
        this.reuseButton.addActionListener(event -> {
            try {
                this.pointController.reuseSource(this.imagesPane.getImagesToReuse());
                this.mainGUI.reloadImages();
                this.outputGUI.dispose();
                this.dispose();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void addComponents() {
        this.add(this.imagesPane, BorderLayout.CENTER);
        this.add(this.reuseButton, BorderLayout.SOUTH);
    }

    private void setSize(){
        final Dimension screenSize = DisplayInfo.getDisplaySize(80);
        final int min_width = (screenSize.width/5);
        final int min_height =(screenSize.height/2);
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
    }
}
