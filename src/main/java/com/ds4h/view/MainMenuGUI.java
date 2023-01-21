package com.ds4h.view;
import com.ds4h.view.StandardGUI.StandardGUI;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageCanvas;
import ij.process.ImageProcessor;

import java.awt.*;
import java.awt.event.*;

public class MainMenuGUI extends Frame implements StandardGUI {

    private ImageStack stack;
    private ImageCanvas canvas;
    private Button manualAlignment, automaticAlignment;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem aboutItem, settingsItem;
    private Panel leftPanel, rightPanel;

    public MainMenuGUI() {
        setTitle("My GUI");

        //this.stack = new ImageStack();
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Set the size of the frame to be half of the screen width and height
        setSize((int) (screenSize.width / 2), (int) (screenSize.height / 2));
        leftPanel = new Panel();
        leftPanel.setLayout(new GridLayout(2, 1));

        this.manualAlignment = new Button("Manual ALignment");
        this.automaticAlignment = new Button("Automatic Alignment");

        leftPanel.add(manualAlignment);
        leftPanel.add(automaticAlignment);

        add(this.leftPanel);

        //this.canvas = new ImageCanvas(new ImagePlus("my stack", this.stack));

        this.rightPanel = new Panel();
        this.rightPanel.setLayout(new BorderLayout());
        this.rightPanel.add(new Button("test image"));
        add(this.rightPanel);

        this.menuBar = new MenuBar();
        this.menu = new Menu("Navigation");
        this.aboutItem = new MenuItem("About");
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
        this.menu.add(this.settingsItem);
    }

    @Override
    public void addListeners() {
        // Add event listener to the menu items
        this.aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle "About" menu item event
            }
        });
        this.settingsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle "Settings" menu item event
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
}