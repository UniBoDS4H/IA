package com.ds4h.view.outputGUI;

import com.drew.lang.annotations.NotNull;
import com.ds4h.controller.elastic.ElasticOutputImageController;
import com.ds4h.controller.imageController.ImageController;
import com.ds4h.controller.pointController.ImageManagerController;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.reuseGUI.ReuseGUI;
import com.ds4h.view.saveImagesGUI.SaveImagesGUI;
import com.ds4h.view.standardGUI.StandardCanvas;
import com.ds4h.view.util.SaveAsEnum;
import ij.ImagePlus;
import ij.gui.StackWindow;

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
    private final Menu reuse;
    private final Menu save;
    private final Menu elastic;
    private final MenuItem elasticItem;
    private final MenuItem reuseItem;
    private final MenuItem saveItem;
    private final MenuItem saveAsTiff;
    private final MenuItem saveAsMosaic;
    private final MenuItem saveAsComposite;
    private final JPanel panel;
    private final StandardCanvas canvas;
    private final SaveImagesGUI saveGui;
    private final ImageController controller;
    private final ImageManagerController imageManagerController;
    private final MainMenuGUI mainGUI;
    private final boolean isOrderAscending;
    private final boolean isTargetImageForeground;
    private final Detectors selectedDetector;

    public AlignmentOutputGUI(final ImageController controller, final BunwarpjGUI settingsBunwarpj,
                              final ImageManagerController imageManagerController, final MainMenuGUI mainMenuGUI,
                              final boolean isAlignmentOrderAscending, final boolean isTargetImageForeground,
                              @NotNull Detectors selectedDetector) {
        super(image = controller.getAlignedImagesAsStack(), new StandardCanvas(image));
        this.selectedDetector = selectedDetector;
        this.canvas = (StandardCanvas)this.getCanvas();
        this.removeAll();
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.mainGUI = mainMenuGUI;
        this.imageManagerController = imageManagerController;
        this.bunwarpjGUI = settingsBunwarpj;
        this.saveGui = new SaveImagesGUI(controller, this.imageManagerController);
        this.panel = new JPanel();
        this.panel.removeAll();
        this.panel.revalidate();
        this.panel.repaint();
        this.panel.setLayout(new BorderLayout());
        this.menuBar = new MenuBar();
        this.elastic = new Menu("Elastic");
        this.reuse = new Menu("Reuse");
        this.save = new Menu("Save");
        this.elasticItem = new MenuItem("Elastic deformation");
        this.reuseItem = new MenuItem("Reuse as source");
        this.saveItem = new MenuItem("Save as project");
        this.saveAsTiff = new MenuItem("Save as Tiff");
        this.saveAsMosaic = new MenuItem("Save as Mosaic");
        this.saveAsComposite = new MenuItem("Save as Composite");
        this.isOrderAscending = isAlignmentOrderAscending;
        this.isTargetImageForeground = isTargetImageForeground;
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
        this.menuBar.add(this.save);
        this.menuBar.add(this.reuse);
        this.menuBar.add(this.elastic);
        this.save.add(this.saveItem);
        this.save.add(this.saveAsTiff);
        this.save.add(this.saveAsMosaic);
        this.save.add(this.saveAsComposite);
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

        this.saveAsTiff.addActionListener(event -> {
            this.saveGui.setSaveAsType(SaveAsEnum.SAVE_AS_TIFF);
            this.saveGui.showDialog();
        });

        this.saveItem.addActionListener(event -> {
            this.saveGui.setSaveAsType(SaveAsEnum.SAVE_AS_PROJECT);
            this.saveGui.showDialog();
        });

        this.saveAsMosaic.addActionListener(event -> {
            this.saveGui.setSaveAsType(SaveAsEnum.SAVE_AS_MOSAIC);
            this.saveGui.setOrderAscending(this.isOrderAscending);
            this.saveGui.setTargetImageForeground(this.isTargetImageForeground);
            this.saveGui.showDialog();
        });

        this.saveAsComposite.addActionListener(event -> {
            this.saveGui.setSaveAsType(SaveAsEnum.SAVE_AS_COMPOSITE);
            this.saveGui.showDialog();
        });

        this.saveItem.addActionListener(event -> this.saveGui.showDialog());

        this.reuseItem.addActionListener(event -> {
            final ReuseGUI reuseGUI = new ReuseGUI(this.imageManagerController, this.controller, this.mainGUI, this);
            reuseGUI.showDialog();
        });

        this.elasticItem.addActionListener(event -> {
            this.controller.elastic(imageManagerController.getImageManager(), this.selectedDetector)
                    .whenComplete((images, ex) -> {
                        final ElasticOutputImageController elasticOutputImageController = new ElasticOutputImageController(images);
                        final ImageController imageController = new ImageController(elasticOutputImageController, this.controller.getElasticController());
                        new AlignmentOutputGUI(imageController,
                                this.bunwarpjGUI,
                                this.imageManagerController,
                                this.mainGUI,
                                this.isOrderAscending,
                                this.isTargetImageForeground,
                                this.selectedDetector);
                        this.dispose();
                    });
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
}
