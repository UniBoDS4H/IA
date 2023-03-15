package com.ds4h.view.alignmentConfigGUI;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithmEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.TranslationalAlignment;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import javax.swing.*;
import java.awt.*;

import static com.ds4h.model.util.AlignmentUtil.getAlgorithmFromEnum;
public class AlignmentConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<AlignmentAlgorithmEnum> algorithm;
    private final JTextArea text;
    private final JCheckBox translationCheckbox;
    private final JCheckBox rotationCheckbox;
    private final JCheckBox scalingCheckbox;
    private AlignmentAlgorithmEnum selectedValue;
    private final MainMenuGUI container;
    public AlignmentConfigGUI(MainMenuGUI container){
        this.setTitle("Manual alignment algorithm");
        this.container = container;
        this.getContentPane().setLayout(new GridBagLayout());
        this.algorithm = new JComboBox<>();
        this.text = new JTextArea();
        this.translationCheckbox = new JCheckBox("Translation");
        this.rotationCheckbox = new JCheckBox("Rotation");
        this.scalingCheckbox = new JCheckBox("Scaling");
        this.addComponents();
        this.addListeners();
        this.setSize(DisplayInfo.getDisplaySize(25));
        this.updateCheckBoxes();
    }

    private void updateCheckBoxes() {
        if(this.getSelectedValue() == AlignmentAlgorithmEnum.TRANSLATIONAL){
            TranslationalAlignment translational = (TranslationalAlignment) getAlgorithmFromEnum(AlignmentAlgorithmEnum.TRANSLATIONAL);
            this.scalingCheckbox.setEnabled(true);
            this.translationCheckbox.setEnabled(true);
            this.rotationCheckbox.setEnabled(true);
            this.scalingCheckbox.setSelected(translational.getScale());
            this.translationCheckbox.setSelected(translational.getTranslate());
            this.rotationCheckbox.setSelected(translational.getRotate());
        }else{
            this.scalingCheckbox.setSelected(true);
            this.translationCheckbox.setSelected(true);
            this.rotationCheckbox.setSelected(true);
            this.scalingCheckbox.setEnabled(false);
            this.translationCheckbox.setEnabled(false);
            this.rotationCheckbox.setEnabled(false);
        }

    }

    @Override
    public void showDialog() {
        this.setVisible(true);
        this.algorithm.setSelectedItem(this.selectedValue);
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
            this.text.setText(this.selectedValue.getDocumentation());
            this.container.checkPointsForAlignment();
            this.updateCheckBoxes();
            this.selectedValue = (AlignmentAlgorithmEnum) this.algorithm.getSelectedItem();
        });
    }

    @Override
    public void addComponents() {
        this.populateCombo();
        this.setSize();
        JLabel algLbl = new JLabel("Algorithm: ");
        JLabel infoLbl = new JLabel("Info: ");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Aggiunta dei componenti al layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(algLbl, gbc);
        gbc.gridx = 1;
        add(this.algorithm, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(infoLbl, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(this.text, gbc);

        // Add translation checkbox
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        add(translationCheckbox, gbc);

        // Add scaling checkbox
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        add(scalingCheckbox, gbc);

        // Add rotation checkbox
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        add(rotationCheckbox, gbc);
        this.pack();
    }
    public boolean getScaling(){
        return this.scalingCheckbox.isSelected();
    }
    public boolean getRotation(){
        return this.rotationCheckbox.isSelected();
    }
    public boolean getTranslation(){
        return this.translationCheckbox.isSelected();
    }

    private void setSize(){
        this.setSize(DisplayInfo.getDisplaySize(30));
    }

    private void populateCombo(){
        this.selectedValue = AlignmentAlgorithmEnum.TRANSLATIONAL;
        for(AlignmentAlgorithmEnum algorithm : AlignmentAlgorithmEnum.values()){
            this.algorithm.addItem(algorithm);
        }
    }
}
