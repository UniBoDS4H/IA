package com.ds4h.view.alignmentConfigGUI;

import com.ds4h.model.alignment.AlignmentAlgorithmEnum;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;


public class AlignmentConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<AlignmentAlgorithmEnum> algorithm;
    private final GroupLayout layout;
    private final JButton saveButton;
    private final JTextArea text;
    private AlignmentAlgorithmEnum selectedValue;
    private final MainMenuGUI container;
    public AlignmentConfigGUI(MainMenuGUI container){
        this.setTitle("Pick manual alignment algorithm");
        this.container = container;
        this.layout = new GroupLayout(this.getContentPane());
        this.layout.setAutoCreateGaps(true);
        this.layout.setAutoCreateContainerGaps(true);
        this.getContentPane().setLayout(this.layout);
        this.algorithm = new JComboBox<>();
        this.saveButton = new JButton("Save");
        this.text = new JTextArea();
        this.addComponents();
        this.addListeners();
    }
    @Override
    public void showDialog() {
        this.setVisible(true);
        //this.algorithm.setSelectedItem(this.selectedValue);
    }

    public AlignmentAlgorithmEnum getSelectedValue(){
        return this.selectedValue;
    }

    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.algorithm.addActionListener(event -> {
            this.selectedValue = (AlignmentAlgorithmEnum) this.algorithm.getSelectedItem();
            assert this.selectedValue != null;
            System.out.println(this.selectedValue.getDocumentation());
            this.text.setText(this.selectedValue.getDocumentation());
            this.container.checkPointsForAlignment();
        });
        this.saveButton.addActionListener(event -> {
            this.selectedValue = (AlignmentAlgorithmEnum) this.algorithm.getSelectedItem();
            this.dispose();
        });
    }

    public AlignmentAlgorithmEnum getSelectedAlgorithm(){
        return this.selectedValue;
    }

    @Override
    public void addComponents() {
        this.populateCombo();
        this.setSize();
        JLabel algLbl = new JLabel("Pick the algorithm: ");
        JLabel infoLbl = new JLabel("Info about: ");
        this.layout.setVerticalGroup(this.layout.createSequentialGroup()
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(algLbl)
                .addComponent(this.algorithm))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(infoLbl)
                .addComponent(this.text))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.saveButton)));
        this.layout.setHorizontalGroup(this.layout.createSequentialGroup()
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(algLbl)
                        .addComponent(infoLbl)
                        .addComponent(this.saveButton))
                .addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(this.algorithm)
                        .addComponent(this.text)));
        this.pack();
    }

    private void setSize(){
        this.setSize(DisplayInfo.getDisplaySize(30));
    }

    private void addElement(final JLabel label, final JComponent component){
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(component);
        this.add(panel);
    }

    private void populateCombo(){
        this.selectedValue = AlignmentAlgorithmEnum.TRANSLATION;
        for(AlignmentAlgorithmEnum algorithm : AlignmentAlgorithmEnum.values()){
            System.out.println(algorithm);
            this.algorithm.addItem(algorithm);
        }
    }
}
