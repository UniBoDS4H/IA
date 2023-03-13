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
    private final JPanel modmenuPanel,
            sliderPanel,
            initialPanel,
            finalPanel,
            divPanel,
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

    private final JButton buttonSave, buttonCancel;
    private final GridBagConstraints constraints;

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
        super("BunwaprJ settings");
        // Get the screens dimension
        this.bunwarpJController = bunwarpJController;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        this.setLayout(new GridBagLayout());

        this.modeInput = this.bunwarpJController.getModeInput();
        this.minScale = this.bunwarpJController.getMinScale();
        this.maxScale = this.bunwarpJController.getMaxScale();

        // Set the Frame size
        int width = (int) (screenSize.width * 0.25);
        int height = (int) (screenSize.height * 0.50);
        setSize(width, height);

        // Input field Panel
        this.modmenuPanel = new JPanel();
        this.sliderPanel = new JPanel();
        this.initialPanel = new JPanel();
        this.finalPanel = new JPanel();
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

        // Init the buttons
        this.sampleFactor = this.slider.getValue();
        this.buttonSave = new JButton("Save");
        this.buttonCancel = new JButton("Cancel");
        this.addComponents();
        this.addListeners();
    }

    @Override
    public void showDialog() {
        this.modeMenu.setSelectedItem(this.bunwarpJController.getModeInput());
        this.initialDef.setSelectedItem(this.bunwarpJController.getMinScale());
        this.finalDef.setSelectedItem(this.bunwarpJController.getMaxScale());
        this.slider.setValue(this.bunwarpJController.getSampleFactor());
        this.divWeight.setValue(String.valueOf(this.bunwarpJController.getParDivWeigth()));
        this.curlWeight.setValue(String.valueOf(this.bunwarpJController.getParCurlWeigth()));
        this.landmarkWeight.setValue(String.valueOf(this.bunwarpJController.getParLandmarkWeigth()));
        this.imageWeight.setValue(String.valueOf(this.bunwarpJController.getParImageWeigth()));
        this.consistencyWeight.setValue(String.valueOf(this.bunwarpJController.getParConsistencyWeigth()));
        this.thresholdWeight.setValue(String.valueOf(this.bunwarpJController.getParThreshold()));

        setVisible(true);
    }

    /**
     * Add all the listeners to the components.
     */
    @Override
    public void addListeners() {

        this.buttonSave.addActionListener(event -> {
            try {
                this.modeInput = this.modeMenu.getItemAt(this.modeMenu.getSelectedIndex());
                this.bunwarpJController.setModeInput(this.modeInput);
                this.minScale = this.initialDef.getItemAt(this.initialDef.getSelectedIndex());
                this.bunwarpJController.setMinScale(this.minScale);
                this.maxScale = this.finalDef.getItemAt(this.finalDef.getSelectedIndex());
                this.bunwarpJController.setMaxScale(this.maxScale);
                this.sampleFactor = this.slider.getValue();
                this.bunwarpJController.setSampleFactor(this.sampleFactor);
                this.parDivWeigth = this.checkInput(this.divWeight) ? Double.parseDouble(this.divWeight.getText()) : BunwarpjGUI.MIN_ZERO;
                this.bunwarpJController.setParDivWeigth(this.parDivWeigth);
                this.parCurlWeigth = this.checkInput(this.curlWeight) ? Double.parseDouble(this.curlWeight.getText()) : BunwarpjGUI.MIN_ZERO;
                this.bunwarpJController.setParCurlWeigth(this.parCurlWeigth);
                this.parLandmarkWeigth = this.checkInput(this.landmarkWeight) ? Double.parseDouble(this.landmarkWeight.getText()) : BunwarpjGUI.MIN_ZERO;
                this.bunwarpJController.setParLandmarkWeigth(this.parLandmarkWeigth);
                this.parImageWeigth = this.checkInput(this.imageWeight) ? Double.parseDouble(this.imageWeight.getText()) : BunwarpjGUI.MIN_ONE;
                this.bunwarpJController.setParImageWeigth(this.parImageWeigth);
                this.parConsistencyWeigth = this.checkInput(this.consistencyWeight) ? Double.parseDouble(this.consistencyWeight.getText()) : BunwarpjGUI.MIN_TEN;
                this.bunwarpJController.setParConsistencyWeigth(this.parConsistencyWeigth);
                this.parThreshold = this.checkInput(this.thresholdWeight) ? Double.parseDouble(this.thresholdWeight.getText()) : BunwarpjGUI.MIN_ZERO_ONE;
                this.bunwarpJController.setParThreshold(this.parThreshold);
                dispose();
            }catch(Exception e){

            }
        });

        this.buttonCancel.addActionListener(evenet -> {
            this.dispose();
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
        this.addElement(new JLabel("Mode : "), this.modmenuPanel, this.modeMenu);
        this.addElement(new JLabel("Initial_Deformation : "), this.initialPanel, this.initialDef);
        this.addElement(new JLabel("Final_Deformation : "), this.finalPanel, this.finalDef);
        this.addElement(new JLabel(("Image Sample Factor : ")), this.sliderPanel, this.slider);
        this.addElement(new JLabel("Divergence Weight :"), this.divPanel, this.divWeight);
        this.addElement(new JLabel("Curl Weight :"), this.curlPanel, this.curlWeight);
        this.addElement(new JLabel("Landmark Weight :"), this.landmarkPanel, this.landmarkWeight);
        this.addElement(new JLabel("Image Weight :"), this.imagePanel, this.imageWeight);
        this.addElement(new JLabel("Consistency Weight :"), this.consistencyPanel, this.consistencyWeight);
        this.addElement(new JLabel("Stop Threshold :"), this.thresholdPanel, this.thresholdWeight);
        this.buttonPanel.add(this.buttonSave);
        this.buttonPanel.add(this.buttonCancel);
        this.constraints.gridy++;
        add(this.buttonPanel, this.constraints);

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
    private void addElement(final JLabel label, final JPanel panel, final JSlider slider){
        label.setPreferredSize(new Dimension(BunwarpjGUI.WIDTH, BunwarpjGUI.HEIGHT));
        slider.setMajorTickSpacing(7);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(label);
        panel.add(slider);
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        add(panel, this.constraints);
    }

    /**
     * Add a single element inside the GUI. In particular the element is a JComboBox
     * @param label : The label where we show which parameter are we setting
     * @param panel : The panel where we store the label and the field
     * @param combo : Combobox where a stored the infos for BunwarpJ settings (Mode, MinScaleDeformation and MaxScaleDeformation)
     */
    private void addElement(final JLabel label, final JPanel panel, final JComboBox<? extends Enum> combo){
        label.setPreferredSize(new Dimension(BunwarpjGUI.WIDTH, BunwarpjGUI.HEIGHT));
        combo.setPreferredSize(new Dimension(BunwarpjGUI.WIDTH, BunwarpjGUI.HEIGHT));
        panel.add(label);
        panel.add(combo);
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        add(panel, this.constraints);
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
