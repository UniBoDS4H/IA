package com.ds4h.view;
import com.ds4h.view.StandardGUI.StandardGUI;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageCanvas;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MainMenuGUI extends Frame implements StandardGUI {
    private Button manualAlignment, automaticAlignment;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem aboutItem, loadImages,settingsItem;
    private Panel leftPanel, rightPanel;

    private static int MIN_IMAGES = 0, MAX_IMAGES = 3;

    /**
     * Constructor of the MainMenu GUI
     */
    public MainMenuGUI() {
        setTitle("DS4H Image Alignment");
        this.setFrameSize();
        //Init of the two buttons
        this.manualAlignment = new Button("Manual Alignment");
        this.automaticAlignment = new Button("Automatic Alignment");

        //Adding the Left Panel, where are stored the buttons for the transformations
        this.leftPanel = new Panel();
        this.leftPanel.setLayout(new GridLayout(2, 1));
        this.leftPanel.add(manualAlignment);
        this.leftPanel.add(automaticAlignment);
        add(this.leftPanel);

        //this.canvas = new ImageCanvas(new ImagePlus("my stack", this.stack));

        //Adding the Right Panel, where all the images are loaded
        this.rightPanel = new Panel();
        this.rightPanel.setLayout(new BorderLayout());
        add(this.rightPanel);

        //Init of the Menu Bar and all the Menu Items
        this.menuBar = new MenuBar();
        this.menu = new Menu("Navigation");
        this.aboutItem = new MenuItem("About");
        this.loadImages = new MenuItem("Load Images");
        this.settingsItem = new MenuItem("Settings");

        this.addComponents();
        this.addListeners();

        setVisible(true);
    }


    @Override
    public void addComponents(){

        // Create a panel to hold the buttons
        // Set the layout of the panel to be a vertical box layout
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.add(manualAlignment);
        buttonPanel.add(automaticAlignment);


        // Add the button panel to the left side of the frame
        add(buttonPanel, BorderLayout.WEST);

        // Create menu bar and add it to the frame
        setMenuBar(this.menuBar);

        // Create menu and add it to the menu bar
        this.menuBar.add(this.menu);

        // Create menu items and add them to the menu
        this.menu.add(this.aboutItem);
        this.menu.add(this.loadImages);
        this.menu.add(this.settingsItem);
    }

    @Override
    public void addListeners() {
        // Add event listener to the menu items
        this.aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.loadImages.addActionListener(event ->{
            this.pickImages();
        });
        this.settingsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.manualAlignment.addActionListener(event -> {
            System.out.println("Ciao");
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void pickImages(){
        FileDialog fd = new FileDialog(new Frame(), "Choose a file", FileDialog.LOAD);
        fd.setMultipleMode(true);
        fd.setVisible(true);
        File[] files = fd.getFiles();

    }

    private void setFrameSize(){
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int min_width = (int) (screenSize.width / 2);
        int min_heigth =(int) (screenSize.height / 2);
        // Set the size of the frame to be half of the screen width and height
        setSize(min_width, min_heigth);
        setMinimumSize(new Dimension(min_width,min_heigth));
    }
}