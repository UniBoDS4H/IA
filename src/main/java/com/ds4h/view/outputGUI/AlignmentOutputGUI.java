package com.ds4h.view.outputGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.alignmentController.ManualAlignmentController.ManualAlignmentController;
import com.ds4h.controller.bunwarpJController.BunwarpJController;
import com.ds4h.controller.imageController.ImageController;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.configureImageGUI.ConfigureImagesGUI;
import com.ds4h.view.loadingGUI.LoadingGUI;
import com.ds4h.view.loadingGUI.LoadingType;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.reuseGUI.ReuseGUI;
import com.ds4h.view.saveImagesGUI.SaveImagesGUI;
import com.ds4h.view.standardGUI.StandardCanvas;
import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.StackWindow;
import ij.process.LUT;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class AlignmentOutputGUI extends StackWindow {
    private static ImagePlus image;
    private final String algorithm;
    private final BunwarpjGUI bunwarpjGUI;
    private final MenuBar menuBar;
    private final Menu settings;
    private final Menu reuse;
    private final Menu save;
    private final Menu elastic;
    private final MenuItem elasticItem;
    private final MenuItem reuseItem;
    private final MenuItem overlappedItem;
    private final MenuItem saveItem;
    private final MenuItem settingsImages;
    private final MenuItem carouselItem;
    private final JPanel panel;
    private final StandardCanvas canvas;
    private final SaveImagesGUI saveGui;
    private final ImageController controller;
    private final ConfigureImagesGUI configureImagesGUI;
    private final BunwarpJController bunwarpJController;
    private final AlignmentControllerInterface alignmentControllerInterface;
    private final PointController pointController;
    private final MainMenuGUI mainGUI;
    private final LUT[] originalLuts;

    public AlignmentOutputGUI(final AlignmentControllerInterface alignmentController, final BunwarpjGUI settingsBunwarpj, final BunwarpJController bunwarpJController, final PointController pointController, final MainMenuGUI mainMenuGUI) {
        super(image = alignmentController.getAlignedImagesAsStack(), new StandardCanvas(image));
        this.canvas = (StandardCanvas)this.getCanvas();
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.originalLuts = this.getImagePlus().getLuts();
        this.mainGUI = mainMenuGUI;
        this.bunwarpJController = bunwarpJController;
        this.alignmentControllerInterface = alignmentController;
        this.pointController = pointController;
        this.controller = new ImageController(alignmentController, bunwarpJController);
        this.configureImagesGUI = new ConfigureImagesGUI(this);
        this.algorithm = alignmentController.name();
        this.bunwarpjGUI = settingsBunwarpj;
        this.saveGui = new SaveImagesGUI(this.controller);
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        this.menuBar = new MenuBar();
        this.settingsImages = new MenuItem("Configure images");
        this.settings = new Menu("Settings");
        this.elastic = new Menu("Elastic");
        this.reuse = new Menu("Reuse");
        this.save = new Menu("Save");
        this.elasticItem = new MenuItem("Elastic deformation");
        this.reuseItem = new MenuItem("Reuse as source");
        this.overlappedItem = new MenuItem("View overlapped");
        this.carouselItem = new MenuItem("View carousel");
        this.saveItem = new MenuItem("Save project");
        this.addComponents();
        this.addListeners();
        this.createOutputIcons();
    }

    public AlignmentOutputGUI(final ImagePlus stack, final AlignmentControllerInterface alignmentController,  final BunwarpjGUI settingsBunwarpj, final BunwarpJController bunwarpJController, final PointController pointController, final MainMenuGUI mainMenuGUI) {
        super(image = stack, new StandardCanvas(image));
        this.canvas = (StandardCanvas)this.getCanvas();
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.originalLuts = this.getImagePlus().getLuts();
        this.mainGUI = mainMenuGUI;
        this.bunwarpJController = bunwarpJController;
        this.alignmentControllerInterface = alignmentController;
        this.pointController = pointController;
        this.controller = new ImageController(alignmentController, bunwarpJController);
        this.configureImagesGUI = new ConfigureImagesGUI(this);
        this.algorithm = alignmentController.name();
        this.bunwarpjGUI = settingsBunwarpj;
        this.saveGui = new SaveImagesGUI(this.controller);
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        this.menuBar = new MenuBar();
        this.settingsImages = new MenuItem("Configure images");
        this.settings = new Menu("Settings");
        this.elastic = new Menu("Elastic");
        this.reuse = new Menu("Reuse");
        this.save = new Menu("Save");
        this.elasticItem = new MenuItem("Elastic deformation");
        this.reuseItem = new MenuItem("Reuse as source");
        this.overlappedItem = new MenuItem("View overlapped");
        this.carouselItem = new MenuItem("View carousel");
        this.saveItem = new MenuItem("Save project");
        this.addComponents();
        this.addListeners();
        this.createOutputIcons();
    }

    public void clearStack(){
        image = null;
        System.gc();
    }

    private void createOutputIcons() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.gc();
                reuseItem.setEnabled(true);
            }
        });
        t1.start();
    }

    public void addComponents() {
        this.menuBar.add(this.settings);
        this.menuBar.add(this.save);
        this.menuBar.add(this.reuse);
        this.menuBar.add(this.elastic);
        this.settings.add(this.settingsImages);
        this.settings.add(this.overlappedItem);
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

        this.settingsImages.addActionListener(event -> {
            this.configureImagesGUI.showDialog();

        });

        this.pack();
        /*this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        */
        this.overlappedItem.addActionListener(event -> {
            this.getImagePlus().setDisplayMode(CompositeImage.COMPOSITE);
            this.settings.remove(this.overlappedItem);
            this.settings.add(this.carouselItem);
        });

        this.carouselItem.addActionListener(event -> {
            this.getImagePlus().setDisplayMode(2);
            this.settings.remove(this.carouselItem);
            this.settings.add(this.overlappedItem);
        });

        this.saveItem.addActionListener(event -> {
            this.saveGui.showDialog();
        });

        this.reuseItem.addActionListener(event -> {
            final ReuseGUI reuseGUI = new ReuseGUI(this.pointController, this.controller, this.mainGUI, this);
            reuseGUI.showDialog();
        });

        this.elasticItem.addActionListener(event -> {
            this.controller.elastic(this.alignmentControllerInterface.getAlignedImages());
            final Thread pollingElastic = new Thread(() -> {
                try {
                    while (this.controller.deformationIsAlive()){
                        Thread.sleep(1000);
                    }
                    if(this.controller.getAlignedImages().size() > 0) {
                        new AlignmentOutputGUI(this.controller.getAlignedImagesAsStack(), this.alignmentControllerInterface, this.bunwarpjGUI, this.bunwarpJController, this.pointController, this.mainGUI);
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
    }

    public LUT[] getOriginalLuts() {
        return this.originalLuts;
    }
}
