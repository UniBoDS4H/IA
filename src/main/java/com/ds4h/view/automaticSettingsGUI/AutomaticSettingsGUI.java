package com.ds4h.view.automaticSettingsGUI;

import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;

public class AutomaticSettingsGUI extends JFrame implements StandardGUI {
    private final JComboBox<Detectors> detectors;
    private final JSlider slider;
    private final GroupLayout layout;
    private final JButton saveButton;
    private Detectors selectedValue;
    private final MainMenuGUI container;
    public AutomaticSettingsGUI(MainMenuGUI container){
        this.setTitle("Automatic alignment algorithm");
        this.container = container;
        this.layout = new GroupLayout(this.getContentPane());
        this.layout.setAutoCreateGaps(true);
        this.layout.setAutoCreateContainerGaps(true);
        this.getContentPane().setLayout(this.layout);
        this.detectors = new JComboBox<>();
        this.slider = new JSlider(0, 20);
        this.slider.setMajorTickSpacing(5);
        this.slider.setMinorTickSpacing(1);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);
        this.slider.setValue(0);
        this.saveButton = new JButton("Save");
        this.addComponents();
        this.addListeners();
        this.setSize(DisplayInfo.getDisplaySize(25));
    }
    @Override
    public void showDialog() {
        this.setVisible(true);
        this.detectors.setSelectedItem(this.selectedValue);
        this.slider.setValue((int)(this.selectedValue.getFactor()*10));
    }

    public Detectors getSelectedValue(){
        return this.selectedValue;
    }

    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.detectors.addActionListener(event -> {
            this.selectedValue = (Detectors) this.detectors.getSelectedItem();
            assert this.selectedValue != null;
            this.slider.setValue((int)(this.selectedValue.getFactor()*10));
            this.container.checkPointsForAlignment();
        });
        this.saveButton.addActionListener(event -> {
            this.selectedValue = (Detectors) this.detectors.getSelectedItem();
            assert this.selectedValue != null;
            this.selectedValue.setFactor(this.slider.getValue());
            this.dispose();
        });
    }

    public Detectors getSelectedDetector(){
        return this.selectedValue;
    }

    @Override
    public void addComponents() {
        this.populateCombo();
        this.setSize();
        final JLabel slidLbl = new JLabel("Threshold factor: ");
        final JLabel algLbl = new JLabel("Detector: ");
        this.layout.setVerticalGroup(this.layout.createSequentialGroup()
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(algLbl)
                        .addComponent(this.detectors))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(slidLbl)
                        .addComponent(this.slider))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.saveButton)));
        this.layout.setHorizontalGroup(this.layout.createSequentialGroup()
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(algLbl)
                        .addComponent(slidLbl)
                        .addComponent(this.saveButton))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(this.detectors))
                        .addComponent(this.slider));
        this.pack();
    }

    private void setSize(){
        this.setSize(DisplayInfo.getDisplaySize(30));
    }
    private void populateCombo(){
        this.selectedValue = Detectors.SURF;
        for(final Detectors detector : Detectors.values()){
            this.detectors.addItem(detector);
        }
    }
}
