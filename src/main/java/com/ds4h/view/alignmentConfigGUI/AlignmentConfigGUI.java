package com.ds4h.view.alignmentConfigGUI;

import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;

public class AlignmentConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<AlignmentAlgorithm> algorithm;
    private AlignmentAlgorithm selectedValue;
    public AlignmentConfigGUI(){
        this.setTitle("Pick manual alignment algorithm");
        this.algorithm = new JComboBox<>();
        this.addComponents();
        this.addListeners();
    }
    @Override
    public void showDialog() {
        this.setVisible(true);
        this.algorithm.setSelectedItem(this.selectedValue);
    }

    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.algorithm.addActionListener(event -> {
            this.selectedValue = (AlignmentAlgorithm) this.algorithm.getSelectedItem();
        });
    }

    public AlignmentAlgorithm getSelectedAlgorithm(){
        return this.selectedValue;
    }

    @Override
    public void addComponents() {
        this.populateCombo();
        this.add(this.algorithm);
    }
    private void populateCombo(){
        this.selectedValue = AlignmentAlgorithm.TRANSLATIVE;
        for(AlignmentAlgorithm algorithm : AlignmentAlgorithm.values()){
            this.algorithm.addItem(algorithm);
        }
    }
}
