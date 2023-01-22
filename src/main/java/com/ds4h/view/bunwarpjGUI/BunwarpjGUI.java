package com.ds4h.view.bunwarpjGUI;

import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BunwarpjGUI extends Frame implements StandardGUI {

    private final JPanel divPanel,
            curlPanel,
            landmarkPanel,
            imagePanel,
            consistencyPanel,
            thresholdPanel,
            buttonPanel;
    private final JFormattedTextField divWeight,
            curlWeight,
            landmarkWeight,
            imageWeight,
            consistencyWeight,
            thresholdWeight;
    private final JButton buttonOk, buttonCancel;
    private final GridBagConstraints constraints;

    private final static double MIN_ZERO = 0.0,
            MIN_ZERO_ONE = 0.01,
            MIN_ONE = 0.1,
            MIN_TEN = 10.0,
            MIN_SCREEN_MULT = 0.25,
            MAX_SCREEN_MULT = 0.35;
    private final static int WIDTH = 200, HEIGHT = 30;

    public BunwarpjGUI(){
        super("BunwaprJ settings");
        // Get the screens dimension
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLayout(new GridBagLayout());

        // Set the Frame size
        int width = (int) (screenSize.width * BunwarpjGUI.MIN_SCREEN_MULT);
        int height = (int) (screenSize.height * BunwarpjGUI.MAX_SCREEN_MULT);
        setSize(width, height);

        // Input field Panel
        this.divPanel = new JPanel();
        this.curlPanel = new JPanel();
        this.landmarkPanel = new JPanel();
        this.imagePanel = new JPanel();
        this.consistencyPanel = new JPanel();
        this.thresholdPanel = new JPanel();
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new FlowLayout());
        // Setting the layout
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;


        // Init the input fields
        this.divWeight = new JFormattedTextField(BunwarpjGUI.MIN_ZERO);
        this.curlWeight = new JFormattedTextField(BunwarpjGUI.MIN_ZERO);
        this.landmarkWeight = new JFormattedTextField(BunwarpjGUI.MIN_ZERO);
        this.imageWeight = new JFormattedTextField(BunwarpjGUI.MIN_ONE);
        this.consistencyWeight = new JFormattedTextField(BunwarpjGUI.MIN_TEN);
        this.thresholdWeight = new JFormattedTextField(BunwarpjGUI.MIN_ZERO_ONE);

        //
        this.buttonOk = new JButton("Save");
        this.buttonCancel = new JButton("Cancel");
        this.addComponents();
        this.addListeners();
        setVisible(true);
    }
    @Override
    public void addListeners() {

    }

    @Override
    public void addComponents() {

        // Add input filed for the divWeight
        this.addElement(new JLabel("Divergence Weight :"), this.divPanel, this.divWeight);
        // Add input filed for the curlWeight
        this.addElement(new JLabel("Curl Weight :"), this.curlPanel, this.curlWeight);
        // Add input field for the landmarkWeight
        this.addElement(new JLabel("Landmark Weight :"), this.landmarkPanel, this.landmarkWeight);
        this.addElement(new JLabel("Image Weight :"), this.imagePanel, this.imageWeight);
        this.addElement(new JLabel("Consistency Weight :"), this.consistencyPanel, this.consistencyWeight);
        this.addElement(new JLabel("Stop Threshold :"), this.thresholdPanel, this.thresholdWeight);

    }

    private void addElement(final JLabel label, final JPanel panel, final JFormattedTextField field){
        field.setPreferredSize(new Dimension(BunwarpjGUI.WIDTH, BunwarpjGUI.HEIGHT));
        panel.add(label);
        panel.add(field);
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        add(panel, this.constraints);
    }
}
