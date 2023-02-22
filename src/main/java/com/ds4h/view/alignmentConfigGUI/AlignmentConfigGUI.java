package com.ds4h.view.alignmentConfigGUI;

import com.ds4h.view.bunwarpjGUI.BunwarpjGUI;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;

public class AlignmentConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<AlignmentAlgorithm> algorithm;
    private final JButton saveButton;
    private final JLabel text;
    private final GridBagConstraints constraints;
    private AlignmentAlgorithm selectedValue;
    public AlignmentConfigGUI(){
        this.setTitle("Pick manual alignment algorithm");
        this.setLayout(new GridBagLayout());
        this.algorithm = new JComboBox<>();
        this.saveButton = new JButton("Save");
        this.text = new JLabel();
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;
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
        this.saveButton.addActionListener(event -> {
            this.selectedValue = (AlignmentAlgorithm) this.algorithm.getSelectedItem();
            this.dispose();
        });
    }

    public AlignmentAlgorithm getSelectedAlgorithm(){
        return this.selectedValue;
    }

    @Override
    public void addComponents() {
        this.populateCombo();
        this.setSize();
        this.addElement(new JLabel("Pick the algorithm: "), new JPanel(), this.algorithm);
        this.addElement(new JLabel("Info about: "), new JPanel(), this.text);
        this.constraints.gridy++;
        this.add(this.saveButton, this.constraints);
    }

    private void setSize(){
        this.setSize(new Dimension(400, 400));
    }

    private void addElement(final JLabel label, final JPanel panel, final JComponent component){
        label.setPreferredSize(new Dimension(200, 50));
        panel.add(label);
        panel.add(component);
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        this.add(panel, this.constraints);
    }

    private void populateCombo(){
        this.selectedValue = AlignmentAlgorithm.TRANSLATIVE;
        for(AlignmentAlgorithm algorithm : AlignmentAlgorithm.values()){
            this.algorithm.addItem(algorithm);
        }
    }
}
