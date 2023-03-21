package com.ds4h.view.carouselGUI;

import com.ds4h.controller.imageController.ImageController;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.saveImagesGUI.SaveImagesGUI;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.StackWindow;
import javax.swing.*;
import java.awt.*;

public class AlignmentOutputGUI extends StackWindow {
    private final String algorithm;
    private final BunwarpjGUI bunwarpjGUI;
    private final JMenuBar menuBar;
    private final JMenu settings;
    private final JMenu reuse;
    private final JMenu save;
    private final JMenuItem reuseItem;
    private final JMenuItem overlappedItem;
    private final JMenuItem saveItem;
    private final JPanel panel;
    private final ImageCanvas canvas;
    private final SaveImagesGUI saveGui;
    private final ImageController controller;

    public AlignmentOutputGUI(final ImagePlus imp, final String algorithm, final BunwarpjGUI bunwarpjGUI, final ImageController controller) {
        super(imp);
        this.canvas = this.getCanvas();
        this.algorithm = algorithm;
        this.bunwarpjGUI = bunwarpjGUI;
        this.controller = controller;
        this.saveGui = new SaveImagesGUI(this.controller);
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        this.menuBar = new JMenuBar();
        this.settings = new JMenu("Settings");
        this.reuse = new JMenu("Reuse");
        this.save = new JMenu("Save");
        this.reuseItem = new JMenuItem("Reuse as resource");
        this.overlappedItem = new JMenuItem("View Overlapped");
        this.saveItem = new JMenuItem("Save Project");
        this.addComponents();
        this.addListeners();
    }
    public void addComponents() {
        this.removeAll();
        this.menuBar.add(this.settings);
        this.menuBar.add(this.save);
        this.menuBar.add(this.reuse);
        this.settings.add(this.overlappedItem);
        this.save.add(this.saveItem);
        this.reuse.add(this.reuseItem);
        this.panel.add(this.canvas, BorderLayout.CENTER);
        this.panel.add(this.menuBar, BorderLayout.NORTH);
        this.panel.add(sliceSelector, BorderLayout.PAGE_END);
        this.add(this.panel, BorderLayout.CENTER);
        this.pack();
    }
    public void addListeners() {
        /*this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.overlappedItem.addActionListener(event -> {
            new OverlapImagesGUI(this.controller.name() ,this.bunwarpjGUI, this.controller, this.pointController, this.previewImagesPane).showDialog();
            this.dispose();
        });
        
         */
        this.saveItem.addActionListener(event -> {
            this.saveGui.showDialog();
        });
        /*
        this.reuseItem.addActionListener(event -> {
            final ReuseGUI reuseGUI = new ReuseGUI(this.previewImagesPane, this.pointController, this.controller);
            reuseGUI.showDialog();
        });

         */
    }
}
