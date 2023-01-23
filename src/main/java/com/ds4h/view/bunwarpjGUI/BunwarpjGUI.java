package com.ds4h.view.bunwarpjGUI;

import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    private double parDivWeigth = MIN_ZERO,
        parCurlWeigth = MIN_ZERO,
        parLandmarkWeigth = MIN_ZERO,
        parImageWeigth = MIN_ONE,
        parConsistencyWeigth = MIN_TEN,
        parThreshold = MIN_ZERO_ONE;

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

        // Init the buttons
        this.buttonOk = new JButton("Save");
        this.buttonCancel = new JButton("Cancel");
        this.addComponents();
        this.addListeners();
        setVisible(true);
    }
    @Override
    public void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }

    /**
     * Add all the components for the GUI. This parameters will be used for BunwarpJ_
     */
    @Override
    public void addComponents() {

        this.addElement(new JLabel("Divergence Weight :"), this.divPanel, this.divWeight);
        this.addElement(new JLabel("Curl Weight :"), this.curlPanel, this.curlWeight);
        this.addElement(new JLabel("Landmark Weight :"), this.landmarkPanel, this.landmarkWeight);
        this.addElement(new JLabel("Image Weight :"), this.imagePanel, this.imageWeight);
        this.addElement(new JLabel("Consistency Weight :"), this.consistencyPanel, this.consistencyWeight);
        this.addElement(new JLabel("Stop Threshold :"), this.thresholdPanel, this.thresholdWeight);
        this.buttonPanel.add(this.buttonOk);
        this.buttonPanel.add(this.buttonCancel);
        this.constraints.gridy++;
        add(this.buttonPanel, this.constraints);

    }

    /**
     * Add a single element inside the GUI.
     * @param label : The label where we show which parameter are we setting
     * @param panel : The panel where we store the label and the field
     * @param field : The input field for the parameter
     */
    private void addElement(final JLabel label, final JPanel panel, final JFormattedTextField field){
        label.setPreferredSize(new Dimension(BunwarpjGUI.WIDTH, BunwarpjGUI.HEIGHT));
        field.setPreferredSize(new Dimension(BunwarpjGUI.WIDTH, BunwarpjGUI.HEIGHT));
        panel.add(label);
        panel.add(field);
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        add(panel, this.constraints);
    }
}
