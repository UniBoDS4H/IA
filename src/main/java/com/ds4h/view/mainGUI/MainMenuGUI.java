package com.ds4h.view.mainGUI;
import com.ds4h.controller.alignmentController.AutomaticAlignmentController.AutomaticAlignmentController;
import com.ds4h.controller.alignmentController.ManualAlignmentController.ManualAlignmentController;
import com.ds4h.controller.alignmentController.semiAutomaticController.SemiAutomaticController;
import com.ds4h.controller.bunwarpJController.BunwarpJController;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.controller.directoryManager.DirectoryManager;
import com.ds4h.controller.exportController.ExportController;
import com.ds4h.controller.importController.ImportController;
import com.ds4h.controller.opencvController.OpencvController;
import com.ds4h.model.alignment.manual.AffineAlignment;
import com.ds4h.model.alignment.manual.PerspectiveAlignment;
import com.ds4h.model.alignment.manual.RansacAlignment;
import com.ds4h.model.alignment.manual.TranslationAlignment;
import com.ds4h.view.aboutGUI.AboutGUI;
import com.ds4h.view.alignmentConfigGUI.AlignmentConfigGUI;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.carouselGUI.CarouselGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.loadingGUI.LoadingGUI;
import com.ds4h.view.overlapImages.OverlapImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;


public class MainMenuGUI extends JFrame implements StandardGUI {
    private final JButton manualAlignment, automaticAlignment, semiAutomaticAlignment;
    private final JMenuBar menuBar;
    private final JMenu menu, project;
    private final JMenuItem aboutItem, loadImages,settingsItem, exportItem, importItem, alignmentItem;
    private final JPanel panel;
    private final AboutGUI aboutGUI;
    private final JFileChooser fileChooser;
    private final BunwarpjGUI settingsBunwarpj;
    private final CornerController cornerControler;
    private final PreviewImagesPane imagesPreview;
    private final AlignmentConfigGUI alignmentConfigGUI;
    private final BunwarpJController bunwarpJController;
    private final AutomaticAlignmentController automaticAlignmentController = new AutomaticAlignmentController();
    private final ManualAlignmentController manualAlignmentController = new ManualAlignmentController();
    private final SemiAutomaticController semiAutomaticController = new SemiAutomaticController();

    private static final int MIN_IMAGES = 0, MAX_IMAGES = 3;

    /**
     * Constructor of the MainMenu GUI
     */
    public MainMenuGUI() {
        setTitle("DS4H Image Alignment");
        this.setFrameSize();
        this.bunwarpJController = new BunwarpJController();
        this.fileChooser = new JFileChooser();
        this.cornerControler = new CornerController();
        //Init of the two buttons
        this.manualAlignment = new JButton("Manual Alignment");
        this.automaticAlignment = new JButton("Automatic Alignment");
        this.semiAutomaticAlignment = new JButton("SemiAutomatic Alignment");

        //Adding the Left Panel, where are stored the buttons for the transformations
        this.panel = new JPanel();
        this.panel.setLayout(new GridBagLayout());

        //Init of the previewList
        this.imagesPreview = new PreviewImagesPane(this.cornerControler);

        GridBagConstraints gbcPanel = new GridBagConstraints();
        gbcPanel.gridx = 0;
        gbcPanel.gridy = 0;
        gbcPanel.gridwidth = GridBagConstraints.REMAINDER;
        gbcPanel.gridheight = 10;
        gbcPanel.fill = GridBagConstraints.BOTH;
        gbcPanel.weightx = 1;
        gbcPanel.weighty = 1;
        this.panel.add(new JScrollPane(this.imagesPreview), gbcPanel);

        GridBagConstraints gbcAuto = new GridBagConstraints();
        gbcAuto.gridx = 0;
        gbcAuto.gridy = 10;
        gbcAuto.gridwidth = 1;
        gbcAuto.gridheight = 1;
        gbcAuto.fill = GridBagConstraints.BOTH;
        gbcAuto.weightx = 1;
        gbcAuto.weighty = 0;
        this.panel.add(this.automaticAlignment, gbcAuto);
        
        GridBagConstraints gbcManual = new GridBagConstraints();
        gbcManual.gridx = 0;
        gbcManual.gridy = 11;
        gbcManual.gridwidth = 1;
        gbcManual.gridheight = 1;
        gbcManual.fill = GridBagConstraints.BOTH;
        gbcManual.weightx = 1;
        gbcManual.weighty = 0;
        this.panel.add(this.manualAlignment, gbcManual); // aggiungo il secondo bottone al JFrame con il GridBagLayout

        GridBagConstraints gbcSemi = new GridBagConstraints();
        gbcManual.gridx = 0;
        gbcManual.gridy = 12;
        gbcManual.gridwidth = 1;
        gbcManual.gridheight = 1;
        gbcManual.fill = GridBagConstraints.BOTH;
        gbcManual.weightx = 1;
        gbcManual.weighty = 0;
        this.panel.add(this.semiAutomaticAlignment, gbcManual); // aggiungo il terzo bottone al JFrame con il GridBagLayout



        add(this.panel);
        //this.canvas = new ImageCanvas(new ImagePlus("my stack", this.stack));

        this.aboutGUI = new AboutGUI();
        this.settingsBunwarpj = new BunwarpjGUI(this.bunwarpJController);
        this.alignmentConfigGUI = new AlignmentConfigGUI(this);
        //Init of the Menu Bar and all the Menu Items
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("Menu");
        this.project = new JMenu("Project");
        this.aboutItem = new JMenuItem("About");
        this.loadImages = new JMenuItem("Load Images");
        this.settingsItem = new JMenuItem("Settings");
        this.exportItem = new JMenuItem("Export");
        this.importItem = new JMenuItem("Import");
        this.alignmentItem = new JMenuItem("Alignment algorithm");

        this.addComponents();
        this.addListeners();
        this.checkPointsForAlignment();
        this.showDialog();
    }

    /**
     * Add all the components of the MainMenu
     */
    @Override
    public void addComponents(){
        // Create menu bar and add it to the frame
        setJMenuBar(this.menuBar);

        // Create menu and add it to the menu bar
        this.menuBar.add(this.menu);
        this.menuBar.add(this.project);
        // Create menu items and add them to the menu
        this.menu.add(this.aboutItem);
        this.menu.add(this.loadImages);
        this.menu.add(this.settingsItem);
        this.menu.add(this.alignmentItem);
        this.project.add(this.exportItem);
        this.project.add(this.importItem);
    }
    public void checkPointsForAlignment(){
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        int nPoints;
        if(!this.cornerControler.getCornerImagesImages().isEmpty()){
            nPoints = this.cornerControler.getCornerImagesImages().get(0).getCorners().length;
            this.cornerControler.getCornerImagesImages().forEach(i->{
                switch (this.alignmentConfigGUI.getSelectedValue()){
                    case AFFINE:
                        if(i.getCorners().length != AffineAlignment.REQUIRED_POINTS){
                            this.manualAlignment.setEnabled(false);
                            this.manualAlignment.setToolTipText("<html>"
                                    + "The number of points inside the images is not correct."
                                    +"<br>"
                                    + "In order to use the Affine alignment you must use " + AffineAlignment.REQUIRED_POINTS + " points in each image."
                                    + "</html>");
                        }else{
                            this.manualAlignment.setEnabled(true);
                            this.manualAlignment.setToolTipText("");
                        }
                        break;
                    case RANSAC:
                        if(i.getCorners().length < RansacAlignment.LOWER_BOUND) {
                            this.manualAlignment.setEnabled(false);
                            this.manualAlignment.setToolTipText("<html>"
                                    + "The number of points inside the images is not correct."
                                    +"<br>"
                                    + "In order to use the RANSAC alignment you must use at least " + RansacAlignment.LOWER_BOUND + " points in each image."
                                    + "</html>");
                        }else{
                            if(i.getCorners().length != nPoints){
                                this.manualAlignment.setEnabled(false);
                                this.manualAlignment.setToolTipText("The number of points inside the images is not the same in all of them.");
                            }
                            this.manualAlignment.setEnabled(true);
                            this.manualAlignment.setToolTipText("");
                        }
                        break;
                    case PERSPECTIVE:
                        if(i.getCorners().length < PerspectiveAlignment.LOWER_BOUND) {
                            this.manualAlignment.setEnabled(false);
                            this.manualAlignment.setToolTipText("<html>"
                                    + "The number of points inside the images is not correct."
                                    +"<br>"
                                    + "In order to use the Perspective alignment you must use at least " + PerspectiveAlignment.LOWER_BOUND + " points in each image."
                                    + "</html>");
                        }else{
                            if(i.getCorners().length != nPoints){
                                this.manualAlignment.setEnabled(false);
                                this.manualAlignment.setToolTipText("The number of points inside the images is not the same in all of them.");
                            }
                            this.manualAlignment.setEnabled(true);
                            this.manualAlignment.setToolTipText("");
                        }
                        break;
                    case TRANSLATIVE:
                        if(i.getCorners().length < TranslationAlignment.LOWER_BOUND) {
                            this.manualAlignment.setEnabled(false);
                            this.manualAlignment.setToolTipText("<html>"
                                    + "The number of points inside the images is not correct."
                                    +"<br>"
                                    + "In order to use the Translation alignment you must use at least " + TranslationAlignment.LOWER_BOUND + " points in each image."
                                    + "</html>");
                        }else{
                            if(i.getCorners().length != nPoints){
                                this.manualAlignment.setEnabled(false);
                                this.manualAlignment.setToolTipText("The number of points inside the images is not the same in all of them.");
                            }
                            this.manualAlignment.setEnabled(true);
                            this.manualAlignment.setToolTipText("");
                        }
                        break;
                }
            });
        }
    }

    @Override
    public void showDialog() {
        setVisible(true);
    }

    /**
     * Add all the listeners to the components of the MainMenu
     */
    @Override
    public void addListeners() {
        // Add event listener to the menu items
        this.aboutItem.addActionListener(event -> {
            this.aboutGUI.showDialog();
        });

        this.loadImages.addActionListener(event ->{
            this.pickImages();
        });

        this.settingsItem.addActionListener(event ->{
            this.settingsBunwarpj.showDialog();
        });

        this.manualAlignment.addActionListener(event -> {
            //ManualAlignmentController m = new ManualAlignmentController();
            if(!manualAlignmentController.isAlive()) {
                final Thread th = new Thread(() -> {
                    manualAlignmentController.alignImages(this.alignmentConfigGUI.getSelectedValue(), this.cornerControler);
                    while (manualAlignmentController.isAlive()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    new CarouselGUI(this.settingsBunwarpj, manualAlignmentController, this.cornerControler, this.imagesPreview);
                });
                th.start();
            }

        });

        this.semiAutomaticAlignment.addActionListener(event -> {
            if(!semiAutomaticController.isAlive()) {
                final Thread th = new Thread(() -> {
                    semiAutomaticController.align(this.cornerControler);
                    while (semiAutomaticController.isAlive()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    new CarouselGUI(this.settingsBunwarpj, semiAutomaticController, this.cornerControler, this.imagesPreview);
                });
                th.start();
            }
        });

        this.alignmentItem.addActionListener(event -> {
            this.alignmentConfigGUI.showDialog();
        });

        this.exportItem.addActionListener(event -> {
            this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            final int result = this.fileChooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION){
                final File file = this.fileChooser.getSelectedFile();
                try {
                    ExportController.exportProject(this.cornerControler.getCornerManager(), file.getPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        this.importItem.addActionListener(event -> {
            this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            final int result = this.fileChooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION){
                final File file = this.fileChooser.getSelectedFile();
                try {
                    ImportController.importProject(file, this.cornerControler.getCornerManager());
                    this.imagesPreview.showPreviewImages();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        this.automaticAlignment.addActionListener(event -> {
            if(!automaticAlignmentController.isAlive()) {
                automaticAlignmentController.surfAlignment(this.cornerControler);
                final Thread th = new Thread(() -> {
                    final LoadingGUI loadingGUI = new LoadingGUI();
                    while (automaticAlignmentController.isAlive()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (automaticAlignmentController.getAlignedImages().size() > 0) {
                        new OverlapImagesGUI(this.settingsBunwarpj, automaticAlignmentController, this.cornerControler, this.imagesPreview);
                        loadingGUI.close();
                    }
                });
                th.start();
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                DirectoryManager.deleteTMPDirectories();
                OpencvController.deleteLibrary();
                dispose();
            }
        });

    }

    /**
     * Open a File dialog in order to choose all the images for our tool
     */
    private void pickImages(){
        FileDialog fd = new FileDialog(new Frame(), "Choose files", FileDialog.LOAD);
        fd.setMultipleMode(true);
        fd.setVisible(true);
        File[] files = fd.getFiles();//Get all the files
        this.cornerControler.loadImages(Arrays.stream(files).map(File::getPath).collect(Collectors.toList()));
        this.imagesPreview.showPreviewImages();
    }

    /**
     * Method used to set the Min dimension of the Frame, based on the Users monitor dimension
     */
    private void setFrameSize(){
        // Get the screen size
        Dimension screenSize = DisplayInfo.getDisplaySize(80);
        int min_width = (int) (screenSize.width/5);
        int min_height =(int) (screenSize.height);
        // Set the size of the frame to be half of the screen width and height
        // Set the size of the frame to be half of the screen width and height
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
    }

    public void reloadImages(){
        this.imagesPreview.showPreviewImages();
    }
}