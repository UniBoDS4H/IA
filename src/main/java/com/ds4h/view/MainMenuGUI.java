package com.ds4h.view;
import java.awt.*;
import java.awt.event.*;

public class MainMenuGUI extends Frame {

    private Button button1, button2;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem aboutItem, settingsItem;

    public MainMenuGUI() {
        setTitle("My GUI");

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Set the size of the frame to be half of the screen width and height
        setSize((int) (screenSize.width / 2), (int) (screenSize.height / 2));

        // Create button1
        button1 = new Button("Button 1");
        // Create button2
        button2 = new Button("Button 2");

        // Create a panel to hold the buttons
        // Set the layout of the panel to be a vertical box layout
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.add(button1);
        buttonPanel.add(button2);


        // Add the button panel to the left side of the frame
        add(buttonPanel, BorderLayout.WEST);

        // Create menu bar and add it to the frame
        menuBar = new MenuBar();
        setMenuBar(menuBar);

        // Create menu and add it to the menu bar
        menu = new Menu("Navigation");
        menuBar.add(menu);

        // Create menu items and add them to the menu
        aboutItem = new MenuItem("About");
        menu.add(aboutItem);
        settingsItem = new MenuItem("Settings");
        menu.add(settingsItem);

        // Add event listener to the menu items
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle "About" menu item event
            }
        });
        settingsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle "Settings" menu item event
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }
}