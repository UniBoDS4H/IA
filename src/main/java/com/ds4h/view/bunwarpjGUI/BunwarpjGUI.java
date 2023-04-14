package com.ds4h.view.bunwarpjGUI;

import com.ds4h.controller.bunwarpJController.BunwarpJController;
import com.ds4h.model.deformation.scales.BunwarpJMaxScale;
import com.ds4h.model.deformation.scales.BunwarpJMinScale;
import com.ds4h.model.deformation.scales.BunwarpJMode;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BunwarpjGUI extends Frame implements StandardGUI {

    private final JSlider slider;
    private final JComboBox<BunwarpJMode> modeMenu;
    private final JComboBox<BunwarpJMinScale> initialDef;
    private final JComboBox<BunwarpJMaxScale>finalDef;
    private final JFormattedTextField divWeight,
            curlWeight,
            landmarkWeight,
            imageWeight,
            consistencyWeight,
            thresholdWeight;

    private BunwarpJMode modeInput;
    private BunwarpJMinScale minScale;
    private BunwarpJMaxScale maxScale;
    private int sampleFactor;
    public final static double MIN_ZERO = 0.0,
            MIN_ZERO_ONE = 0.01,
            MIN_ONE = 0.1,
            MIN_TEN = 10.0;

    private double parDivWeigth = MIN_ZERO,
            parCurlWeigth = MIN_ZERO,
            parLandmarkWeigth = MIN_ZERO,
            parImageWeigth = MIN_ONE,
            parConsistencyWeigth = MIN_TEN,
            parThreshold = MIN_ZERO_ONE;
    private final static int WIDTH = 200, HEIGHT = 30;
    private final BunwarpJController bunwarpJController;
    public BunwarpjGUI(final BunwarpJController bunwarpJController){
        super("bUnwaprJ settings");
        // Get the screens dimension
        this.bunwarpJController = bunwarpJController;
        this.setLayout(new GridBagLayout());

        this.modeInput = this.bunwarpJController.getModeInput();
        this.minScale = this.bunwarpJController.getMinScale();
        this.maxScale = this.bunwarpJController.getMaxScale();


        this.slider = new JSlider(0, 7);
        this.modeMenu = new JComboBox<>();
        this.initialDef = new JComboBox<>();
        this.finalDef = new JComboBox<>();
        // Init the input fields
        this.divWeight = new JFormattedTextField(String.valueOf(this.bunwarpJController.getMIN_ZERO()));
        this.curlWeight = new JFormattedTextField(String.valueOf(this.bunwarpJController.getMIN_ZERO()));
        this.landmarkWeight = new JFormattedTextField(String.valueOf(this.bunwarpJController.getMIN_ZERO()));
        this.imageWeight = new JFormattedTextField(String.valueOf(this.bunwarpJController.getMIN_ONE()));
        this.consistencyWeight = new JFormattedTextField(String.valueOf(this.bunwarpJController.getMIN_TEN()));
        this.thresholdWeight = new JFormattedTextField(String.valueOf(this.bunwarpJController.getMIN_ZERO_ONE()));

        this.slider.setMajorTickSpacing(7);
        this.slider.setMinorTickSpacing(1);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);

        // Init the buttons
        this.sampleFactor = this.slider.getValue();
        this.addComponents();
        this.addListeners();
        this.setResizable(false);
    }

    @Override
    public void showDialog() {
        this.modeMenu.setSelectedItem(this.bunwarpJController.getModeInput());
        this.initialDef.setSelectedItem(this.bunwarpJController.getMinScale());
        this.finalDef.setSelectedItem(this.bunwarpJController.getMaxScale());
        this.slider.setValue(this.bunwarpJController.getSampleFactor());
        this.divWeight.setValue(String.valueOf(this.bunwarpJController.getParDivWeight()));
        this.curlWeight.setValue(String.valueOf(this.bunwarpJController.getParCurlWeight()));
        this.landmarkWeight.setValue(String.valueOf(this.bunwarpJController.getParLandmarkWeight()));
        this.imageWeight.setValue(String.valueOf(this.bunwarpJController.getParImageWeight()));
        this.consistencyWeight.setValue(String.valueOf(this.bunwarpJController.getParConsistencyWeight()));
        this.thresholdWeight.setValue(String.valueOf(this.bunwarpJController.getParThreshold()));

        setVisible(true);
    }

    /**
     * Add all the listeners to the components.
     */
    @Override
    public void addListeners() {
        this.modeMenu.addActionListener(e -> {
            this.modeInput = this.modeMenu.getItemAt(this.modeMenu.getSelectedIndex());
            this.bunwarpJController.setModeInput(this.modeInput);
        });

        this.initialDef.addActionListener(e -> {
            this.minScale = this.initialDef.getItemAt(this.initialDef.getSelectedIndex());
            this.bunwarpJController.setMinScale(this.minScale);
        });
        this.finalDef.addActionListener(e -> {
            this.maxScale = this.finalDef.getItemAt(this.finalDef.getSelectedIndex());
            this.bunwarpJController.setMaxScale(this.maxScale);
        });
        this.slider.addChangeListener(e->{
            this.sampleFactor = this.slider.getValue();
            this.bunwarpJController.setSampleFactor(this.sampleFactor);
        });
        this.divWeight.addActionListener(e->{
            this.parDivWeigth = this.checkInput(this.divWeight) ? Double.parseDouble(this.divWeight.getText()) : BunwarpjGUI.MIN_ZERO;
            this.bunwarpJController.setParDivWeight(this.parDivWeigth);
        });
        this.curlWeight.addActionListener(e->{
            this.parCurlWeigth = this.checkInput(this.curlWeight) ? Double.parseDouble(this.curlWeight.getText()) : BunwarpjGUI.MIN_ZERO;
            this.bunwarpJController.setParCurlWeight(this.parCurlWeigth);
        });
        this.landmarkWeight.addActionListener(e->{
            this.parLandmarkWeigth = this.checkInput(this.landmarkWeight) ? Double.parseDouble(this.landmarkWeight.getText()) : BunwarpjGUI.MIN_ZERO;
            this.bunwarpJController.setParLandmarkWeight(this.parLandmarkWeigth);
        });
        this.imageWeight.addActionListener(e->{
            this.parImageWeigth = this.checkInput(this.imageWeight) ? Double.parseDouble(this.imageWeight.getText()) : BunwarpjGUI.MIN_ONE;
            this.bunwarpJController.setParImageWeight(this.parImageWeigth);
        });
        this.consistencyWeight.addActionListener(e->{
            this.parConsistencyWeigth = this.checkInput(this.consistencyWeight) ? Double.parseDouble(this.consistencyWeight.getText()) : BunwarpjGUI.MIN_TEN;
            this.bunwarpJController.setParConsistencyWeight(this.parConsistencyWeigth);
        });
        this.thresholdWeight.addActionListener(e->{
            this.parThreshold = this.checkInput(this.thresholdWeight) ? Double.parseDouble(this.thresholdWeight.getText()) : BunwarpjGUI.MIN_ZERO_ONE;
            this.bunwarpJController.setParThreshold(this.parThreshold);
        });
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
        this.fillCombo(BunwarpJMode.values());
        this.fillCombo(BunwarpJMinScale.values());
        this.fillCombo(BunwarpJMaxScale.values());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Increase vertical spacing
        gbc.anchor = GridBagConstraints.WEST; // Left align components
        gbc.weightx = 0.5; // Distribute extra horizontal space evenly
        gbc.gridx = 0;
        gbc.gridy = 0;

        this.add(new JLabel("Mode : "), gbc);
        gbc.gridx++;
        this.add(this.modeMenu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        this.add(new JLabel("Initial_Deformation : "), gbc);
        gbc.gridx++;
        add(this.initialDef, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        this.add(new JLabel("Final_Deformation : "), gbc);
        gbc.gridx++;
        this.add(this.finalDef, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        this.add(new JLabel(("Image Sample Factor : ")), gbc);
        gbc.gridx++;
        this.add(this.slider, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        this.add(new JLabel("Divergence Weight :"), gbc);
        gbc.gridx++;
        this.add(this.divWeight, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        this.add(new JLabel("Curl Weight :"), gbc);
        gbc.gridx++;
        this.add(this.curlWeight, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        this.add(new JLabel("Landmark Weight :"), gbc);
        gbc.gridx++;
        this.add(this.landmarkWeight, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        this.add(new JLabel("Image Weight :"), gbc);
        gbc.gridx++;
        this.add(this.imageWeight, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Consistency Weight :"), gbc);
        gbc.gridx = 1;
        add(this.consistencyWeight, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Stop Threshold :"), gbc);
        gbc.gridx = 1;
        add(this.thresholdWeight, gbc);

        this.pack();
    }

    private void fillCombo(final BunwarpJMode[] values){
        for(final BunwarpJMode mode : values){
            this.modeMenu.addItem(mode);
        }
    }

    private void fillCombo(final BunwarpJMinScale[] values){
        for(final BunwarpJMinScale mode : values){
            this.initialDef.addItem(mode);
        }
    }

    private void fillCombo(final BunwarpJMaxScale[] values){
        for(final BunwarpJMaxScale mode : values){
            this.finalDef.addItem(mode);
        }
    }

    private boolean checkInput(final JFormattedTextField field){
        try {
            if(!field.getText().isEmpty()){
                double val = Double.parseDouble(field.getText());
                return true;
            }
        }catch (Exception e){
                return false;
        }
        return false;
    }
}
