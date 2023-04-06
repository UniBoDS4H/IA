package com.ds4h.view.manualAlignmentConfigGUI;

import com.ds4h.controller.alignmentController.ManualAlignmentController.ManualAlignmentController;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithmEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.PointOverloadEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.TranslationalAlignment;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import javax.swing.*;
import java.awt.*;
import static com.ds4h.model.util.AlignmentUtil.getEnumFromAlgorithm;

public class ManualAlignmentConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<AlignmentAlgorithmEnum> algorithm;
    private final JTextArea text;
    private final JCheckBox translationCheckbox;
    private final JCheckBox rotationCheckbox;
    private final JCheckBox scalingCheckbox;
    private final ManualAlignmentController controller;
    private final MainMenuGUI container;
    private final JComboBox pointOverloadComboBox;
    public ManualAlignmentConfigGUI(MainMenuGUI container, ManualAlignmentController manualAlignmentController){
        this.setTitle("Manual alignment algorithm");
        this.controller = manualAlignmentController;
        this.container = container;
        this.getContentPane().setLayout(new GridBagLayout());
        this.algorithm = new JComboBox<>(AlignmentAlgorithmEnum.values());
        pointOverloadComboBox = new JComboBox(PointOverloadEnum.values());
        this.text = new JTextArea();
        this.translationCheckbox = new JCheckBox("Translation");
        this.rotationCheckbox = new JCheckBox("Rotation");
        this.scalingCheckbox = new JCheckBox("Scaling");
        this.addComponents();
        this.addListeners();
        this.updateCheckBoxes();
    }

    private void updateCheckBoxes() {
        if(this.controller.getAlgorithm() instanceof TranslationalAlignment){
            TranslationalAlignment translational = (TranslationalAlignment) this.controller.getAlgorithm();
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
        this.algorithm.setSelectedItem(getEnumFromAlgorithm(this.controller.getAlgorithm()));
        this.pointOverloadComboBox.setSelectedItem(this.controller.getPointOverload());
    }


    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.algorithm.addActionListener(event -> {
            AlignmentAlgorithmEnum selected = (AlignmentAlgorithmEnum) this.algorithm.getSelectedItem();
            assert selected != null;
            this.text.setText(selected.getDocumentation());
            this.controller.setAlgorithm(this.controller.getAlgorithmFromEnum(selected));

            this.pointOverloadComboBox.setSelectedItem(this.controller.getPointOverload());
            this.updateCheckBoxes();
            this.container.checkPointsForAlignment();
        });
        this.rotationCheckbox.addActionListener(e->{
            if(this.controller.getAlgorithm() instanceof TranslationalAlignment){
                TranslationalAlignment alg = ((TranslationalAlignment) this.controller.getAlgorithm());
                alg.setTransformation(alg.getTranslate(),this.rotationCheckbox.isSelected(),alg.getScale());
            }
            this.container.checkPointsForAlignment();
        });
        this.scalingCheckbox.addActionListener(e->{
            if(this.controller.getAlgorithm() instanceof TranslationalAlignment){
                TranslationalAlignment alg = ((TranslationalAlignment) this.controller.getAlgorithm());
                alg.setTransformation(alg.getTranslate(),alg.getRotate(),this.scalingCheckbox.isSelected());
                if(this.scalingCheckbox.isSelected()) {
                    JOptionPane.showMessageDialog(this,
                            "Warning: \n" +
                                    "using the \"scaling\" can lead to data loss!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
            this.container.checkPointsForAlignment();
        });
        this.translationCheckbox.addActionListener(e->{
            if(this.controller.getAlgorithm() instanceof TranslationalAlignment){
                TranslationalAlignment alg = ((TranslationalAlignment) this.controller.getAlgorithm());
                alg.setTransformation(this.translationCheckbox.isSelected(),alg.getRotate(),alg.getScale());
            }
            this.container.checkPointsForAlignment();
        });
        this.pointOverloadComboBox.addActionListener(e->{
            PointOverloadEnum selected = (PointOverloadEnum) this.pointOverloadComboBox.getSelectedItem();
            assert selected!= null;
            this.controller.setPointOverload(selected);
        });
    }

    @Override
    public void addComponents() {
        JLabel algLbl = new JLabel("Algorithm: ");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(algLbl, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(this.algorithm, gbc);

        final JLabel infoLbl = new JLabel("Info: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(infoLbl, gbc);

        this.text.setLineWrap(true);
        this.text.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(this.text);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(250, 80));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        this.add(scrollPane, gbc);

        // Add point overload label and combobox
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        JLabel pointLbl = new JLabel("Point Overload:");
        this.add(pointLbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        this.add(pointOverloadComboBox, gbc);

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
        this.setResizable(false);
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
}
