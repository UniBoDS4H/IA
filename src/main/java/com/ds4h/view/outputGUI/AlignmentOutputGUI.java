package com.ds4h.view.outputGUI;

import com.ds4h.controller.imageController.ImageController;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.reuseGUI.ReuseGUI;
import com.ds4h.view.saveImagesGUI.SaveImagesGUI;
import com.ds4h.view.standardGUI.StandardCanvas;
import ij.ImagePlus;
import ij.gui.StackWindow;
import ij.process.LUT;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AlignmentOutputGUI extends StackWindow {
    private static ImagePlus image;
    private final BunwarpjGUI bunwarpjGUI;
    private final MenuBar menuBar;
    private final Menu settings;
    private final Menu reuse;
    private final Menu save;
    private final Menu elastic;
    private final MenuItem elasticItem;
    private final MenuItem reuseItem;
    private final MenuItem saveItem;
    private final JPanel panel;
    private final StandardCanvas canvas;
    private final SaveImagesGUI saveGui;
    private final ImageController controller;
    private final PointController pointController;
    private final MainMenuGUI mainGUI;
    private final LUT[] originalLuts;

    public AlignmentOutputGUI(final ImageController controller, final BunwarpjGUI settingsBunwarpj, final PointController pointController, final MainMenuGUI mainMenuGUI) {
        super(image = controller.getAlignedImagesAsStack(), new StandardCanvas(image));
        this.canvas = (StandardCanvas)this.getCanvas();
        this.removeAll();
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.originalLuts = this.getImagePlus().getLuts();
        this.mainGUI = mainMenuGUI;
        this.pointController = pointController;
        this.bunwarpjGUI = settingsBunwarpj;
        this.saveGui = new SaveImagesGUI(controller);
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        this.menuBar = new MenuBar();
        this.settings = new Menu("Settings");
        this.elastic = new Menu("Elastic");
        this.reuse = new Menu("Reuse");
        this.save = new Menu("Save");
        this.elasticItem = new MenuItem("Elastic deformation");
        this.reuseItem = new MenuItem("Reuse as source");
        this.saveItem = new MenuItem("Save project");
        this.addComponents();
        this.addListeners();
        this.createOutputIcons();
    }

    public void clearStack(){
        image.close();
        System.gc();
    }

    private void createOutputIcons() {
        Thread t1 = new Thread(() -> {
            System.gc();
            reuseItem.setEnabled(true);
        });
        t1.start();
    }

    public void addComponents() {
        this.menuBar.add(this.settings);
        this.menuBar.add(this.save);
        this.menuBar.add(this.reuse);
        this.menuBar.add(this.elastic);
        this.save.add(this.saveItem);
        this.reuse.add(this.reuseItem);
        this.elastic.add(this.elasticItem);
        this.panel.add(this.canvas, BorderLayout.CENTER);
        this.setMenuBar(this.menuBar);
        this.panel.add(sliceSelector, BorderLayout.PAGE_END);
        this.add(this.panel, BorderLayout.CENTER);
        this.reuseItem.setEnabled(false);
        this.pack();
    }
    public void addListeners() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                checkWindowSize();
            }

            private void checkWindowSize() {
                setPreferredSize(getSize());
            }
        });

        this.pack();
        /*this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        */


        this.saveItem.addActionListener(event ->
            this.saveGui.showDialog()
        );

        this.reuseItem.addActionListener(event -> {
            final ReuseGUI reuseGUI = new ReuseGUI(this.pointController, this.controller, this.mainGUI, this);
            reuseGUI.showDialog();
        });

        this.elasticItem.addActionListener(event -> {
            this.controller.elastic(this.controller.getAlignedImages());
            final Thread pollingElastic = new Thread(() -> {
                try {
                    while (this.controller.deformationIsAlive()){
                        Thread.sleep(1000);
                    }
                    if(this.controller.getAlignedImages().size() > 0) {
                        new AlignmentOutputGUI(this.controller, this.bunwarpjGUI, this.pointController, this.mainGUI);
                        this.dispose();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
            pollingElastic.start();
        });
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {

                clearStack();
                controller.releaseImages();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });
    }

    public void releaseImages(){
        this.controller.releaseImages();
    }

    public LUT[] getOriginalLuts() {
        return this.originalLuts;
    }
}
