package com.ds4h.view.mainGUI;


import com.ds4h.controller.AlignmentController.AutomaticAlignmentController.*;
import com.ds4h.controller.bunwarpJController.BunwarpJController;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.view.cornerSelectorGUI.CornerSelectorGUI;
import com.ds4h.view.aboutGUI.AboutGUI;
import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenuGUI extends JFrame implements StandardGUI {
    private final JButton manualAlignment, automaticAlignment;
    private final JPanel previewImagesList, buttonsPanel;
    private final JScrollPane listScroller;
    private final JMenuBar menuBar;
    private final JMenu menu;
    private final JMenuItem aboutItem, loadImages,settingsItem;
    private final JPanel leftPanel;
    private final CornerSelectorGUI rightPanel;
    private final AboutGUI aboutGUI;
    private final BunwarpjGUI settingsBunwarpj;
    private final CornerController cornerControler;
    private final List<JLabel> imageLabels;


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
        this.leftPanel = new JPanel();
        this.leftPanel.setLayout(new GridLayout(2, 1));
        this.buttonsPanel = new JPanel();
        this.buttonsPanel.setLayout(new GridLayout(2, 1));
        this.buttonsPanel.add(manualAlignment);
        this.buttonsPanel.add(automaticAlignment);


        //Init of the previewList
        this.imageLabels = new ArrayList<>();
        this.previewImagesList = new JPanel();
        this.previewImagesList.setLayout(new BoxLayout(this.previewImagesList, BoxLayout.Y_AXIS));
        this.listScroller = new JScrollPane(this.previewImagesList);

        this.leftPanel.add(this.listScroller);
        this.leftPanel.add(this.buttonsPanel);

        add(this.leftPanel, BorderLayout.WEST);
        //this.canvas = new ImageCanvas(new ImagePlus("my stack", this.stack));

        //Adding the Right Panel, where all the images are loaded
        this.rightPanel = new CornerSelectorGUI();
        this.aboutGUI = new AboutGUI();
        this.settingsBunwarpj = new BunwarpjGUI();
        add(this.rightPanel);

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
        });
        this.automaticAlignment.addActionListener(event -> {
            //Mat m = new Mat();
            //bUnwarpJ_ b = new bUnwarpJ_();
            //new BunwarpJController().transformation(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.cornerControler.getCornerManager()).show();
            //new AutomaticAlignmentController().surfAlignment(this.cornerControler.getCornerManager());
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
        this.showPreviewImages();
    }
    /**
     * Method used to show the loaded images inside the list
     */
    private void showPreviewImages(){
        this.imageLabels.clear();
        this.previewImagesList.removeAll();
        this.previewImagesList.revalidate();
        for (ImageCorners image : this.cornerControler.getCornerImagesImages()) {
            JPanel panel = new JPanel();
            JButton targetButton = new JButton("Set as target");
            JLabel imageLabel = new JLabel(new ImageIcon(image.getImage().getBufferedImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            panel.setLayout(new GridLayout(1, 3));
            this.imageLabels.add(imageLabel);
            panel.add(imageLabel);
            panel.add(targetButton);
            JLabel textLabel = new JLabel("TARGET");
            textLabel.setForeground(Color.black);
            panel.add(textLabel);
            textLabel.setVisible(this.cornerControler.isSource(image));
            targetButton.addActionListener(event -> {
                this.cornerControler.changeTarget(image);
                this.showPreviewImages();
                this.previewImagesList.revalidate();
            });
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Set the clicked image as the current image
                    Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
                    imageLabels.forEach(i -> i.setBorder(BorderFactory.createLineBorder(Color.BLUE, 0)));
                    imageLabel.setBorder(border);
                    rightPanel.setCurrentImage(image.getImage());
                }
            });
            this.previewImagesList.add(panel);
        }
        this.previewImagesList.revalidate();

    }

    /**
     * Method used to set the Min dimension of the Frame, based on the Users monitor dimension
     */
    private void setFrameSize(){
        // Get the screen size
        Dimension screenSize = DisplayInfo.getDisplaySize(80);
        int min_width = (int) (screenSize.width);
        int min_height =(int) (screenSize.height);
        // Set the size of the frame to be half of the screen width and height
        // Set the size of the frame to be half of the screen width and height
        setSize(min_width, min_height);
        setMinimumSize(new Dimension(min_width,min_height));
    }
}