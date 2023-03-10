package com.ds4h.view.automaticSettingsGUI;

import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;

public class AutomaticSettingsGUI extends JFrame implements StandardGUI {
    private final JComboBox<Detectors> detectors;
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
        this.saveButton = new JButton("Save");
        this.addComponents();
        this.addListeners();
    }
    @Override
    public void showDialog() {
        this.setVisible(true);
        //this.algorithm.setSelectedItem(this.selectedValue);
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
            this.container.checkPointsForAlignment();
        });
        this.saveButton.addActionListener(event -> {
            this.selectedValue = (Detectors) this.detectors.getSelectedItem();
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
        final JLabel algLbl = new JLabel("Detector: ");
        this.layout.setVerticalGroup(this.layout.createSequentialGroup()
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(algLbl)
                        .addComponent(this.detectors))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.LEADING))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.saveButton)));
        this.layout.setHorizontalGroup(this.layout.createSequentialGroup()
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(algLbl)
                        .addComponent(this.saveButton))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(this.detectors)));
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
