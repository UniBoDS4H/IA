package com.ds4h.view.mainGUI;
import com.ds4h.view.cornerSelectorGUI.CornerSelectorGUI;
import com.ds4h.view.aboutGUI.AboutGUI;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
public class MainMenuGUI extends JFrame implements StandardGUI {
    private final JButton manualAlignment, automaticAlignment;
    private final JMenuBar menuBar;
    private final JMenu menu;
    private final JMenuItem aboutItem, loadImages,settingsItem;
    private final JPanel leftPanel;
    private final CornerSelectorGUI rightPanel;

    private static final int MIN_IMAGES = 0, MAX_IMAGES = 3;

    /**
     * Constructor of the MainMenu GUI
     */
    public MainMenuGUI() {
        setTitle("DS4H Image Alignment");
        this.setFrameSize();
        //Init of the two buttons
        this.manualAlignment = new JButton("Manual Alignment");
        this.automaticAlignment = new JButton("Automatic Alignment");

        //Adding the Left Panel, where are stored the buttons for the transformations
        this.leftPanel = new JPanel();
        this.leftPanel.setLayout(new GridLayout(2, 1));
        this.leftPanel.add(manualAlignment);
        this.leftPanel.add(automaticAlignment);
        add(this.leftPanel, BorderLayout.WEST);

        //this.canvas = new ImageCanvas(new ImagePlus("my stack", this.stack));

        //Adding the Right Panel, where all the images are loaded
        this.rightPanel = new CornerSelectorGUI();
        add(this.rightPanel);

        //Init of the Menu Bar and all the Menu Items
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("Navigation");
        this.aboutItem = new JMenuItem("About");
        this.loadImages = new JMenuItem("Load Images");
        this.settingsItem = new JMenuItem("Settings");

        this.addComponents();
        this.addListeners();

        setVisible(true);
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

    /**
     * Add all the listeners to the components of the MainMenu
     */
    @Override
    public void addListeners() {
        // Add event listener to the menu items
        this.aboutItem.addActionListener(event -> {
            new AboutGUI();
        });

        this.loadImages.addActionListener(event ->{
            this.pickImages();
        });

        this.settingsItem.addActionListener(event ->{
            new BunwarpjGUI();
        });

        this.manualAlignment.addActionListener(event -> {
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
        this.rightPanel.loadImages(files);
    }

    /**
     * Method used to set the Min dimension of the Frame, based on the Users monitor dimension
     */
    private void setFrameSize(){
        // Get the screen size
        Dimension screenSize = DisplayInfo.getDisplaySize();
        int min_width = (int) (screenSize.width / 2);
        int min_height =(int) (screenSize.height / 2);
        // Set the size of the frame to be half of the screen width and height
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
    }
}