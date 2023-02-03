package com.ds4h.view.mainGUI;


import com.ds4h.controller.alignmentController.AutomaticAlignmentController.*;
import com.ds4h.controller.alignmentController.ManualAlignmentController.ManualAlignmentController;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.view.aboutGUI.AboutGUI;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.carouselGUI.CarouselGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.overlapImages.OverlapImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;


public class MainMenuGUI extends JFrame implements StandardGUI {
    private final JButton manualAlignment, automaticAlignment;
    private final JPanel buttonsPanel;
    private final JMenuBar menuBar;
    private final JMenu menu;
    private final JMenuItem aboutItem, loadImages,settingsItem;
    private final JPanel panel;
    private final AboutGUI aboutGUI;
    private final BunwarpjGUI settingsBunwarpj;
    private final CornerController cornerControler;
    private final PreviewImagesPane imagesPreview;


    private static final int MIN_IMAGES = 0, MAX_IMAGES = 3;

    /**
     * Constructor of the MainMenu GUI
     */
    public MainMenuGUI() {
        setTitle("DS4H Image Alignment");
        this.setFrameSize();
        this.cornerControler = new CornerController();
        //Init of the two buttons
        this.manualAlignment = new JButton("Manual Alignment");
        this.automaticAlignment = new JButton("Automatic Alignment");


        //Adding the Left Panel, where are stored the buttons for the transformations
        this.panel = new JPanel();
        this.panel.setLayout(new GridLayout(2, 1));
        this.buttonsPanel = new JPanel();
        this.buttonsPanel.setLayout(new GridLayout(2, 1));
        this.buttonsPanel.add(manualAlignment);
        this.buttonsPanel.add(automaticAlignment);


        //Init of the previewList
        this.imagesPreview = new PreviewImagesPane(this.cornerControler);
        this.panel.add(add(new JScrollPane(this.imagesPreview)));
        this.panel.add(this.buttonsPanel);

        add(this.panel);
        //this.canvas = new ImageCanvas(new ImagePlus("my stack", this.stack));

        this.aboutGUI = new AboutGUI();
        this.settingsBunwarpj = new BunwarpjGUI();

        //Init of the Menu Bar and all the Menu Items
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("Navigation");
        this.aboutItem = new JMenuItem("About");
        this.loadImages = new JMenuItem("Load Images");
        this.settingsItem = new JMenuItem("Settings");

        this.addComponents();
        this.addListeners();
        this.showDialog();
    }

    /**
     * Add all the components of the MainMenu
     */
    @Override
    public void addComponents(){

        // Create a panel to hold the buttons
        // Set the layout of the panel to be a vertical box layout


        // Create menu bar and add it to the frame
        setJMenuBar(this.menuBar);

        // Create menu and add it to the menu bar
        this.menuBar.add(this.menu);

        // Create menu items and add them to the menu
        this.menu.add(this.aboutItem);
        this.menu.add(this.loadImages);
        this.menu.add(this.settingsItem);
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
            ManualAlignmentController m = new ManualAlignmentController();
            m.homographyAlignment(this.cornerControler.getCornerManager());
            new CarouselGUI(m);

        });
        this.automaticAlignment.addActionListener(event -> {
            //Mat m = new Mat();
            //bUnwarpJ_ b = new bUnwarpJ_();
            //new BunwarpJController().transformation(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.cornerControler.getCornerManager()).show();
            AutomaticAlignmentController a = new AutomaticAlignmentController();
            a.surfAlignment(this.cornerControler.getCornerManager());
            //new AutomaticAlignmentController().surfAlignment(this.cornerControler.getCornerManager()).forEach(ImagePlus::show);
            //new CarouselGUI(a.getAlignedImages());
            new OverlapImagesGUI(a);
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
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
        int min_width = (int) (screenSize.width/6);
        int min_height =(int) (screenSize.height);
        // Set the size of the frame to be half of the screen width and height
        // Set the size of the frame to be half of the screen width and height
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
    }
}