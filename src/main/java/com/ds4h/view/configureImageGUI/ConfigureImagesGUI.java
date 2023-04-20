package com.ds4h.view.configureImageGUI;

import com.ds4h.view.outputGUI.AlignmentOutputGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.ColorComboBox;
import ij.CompositeImage;
import ij.process.LUT;
import javax.swing.*;
import java.awt.*;

public class ConfigureImagesGUI extends JFrame implements StandardGUI {
    private final JButton reset;
    private final JComboBox<String> comboBox;
    private final ColorComboBox colorComboBox;
    private final JLabel labelCombo;
    private final AlignmentOutputGUI outputGUI;

    public ConfigureImagesGUI(AlignmentOutputGUI alignmentOutputGUI){
        this.setTitle("Configure Images");
        this.outputGUI = alignmentOutputGUI;
        this.setLayout(new GridBagLayout());
        this.colorComboBox = new ColorComboBox();
        this.reset = new JButton("Reset");
        this.labelCombo = new JLabel("Image:");
        this.comboBox = new JComboBox<>();
        this.addComponents();
        this.addListeners();
        this.setResizable(false);
    }


    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.colorComboBox.addActionListener(event -> {
            final int index = this.comboBox.getSelectedIndex();
            final Color color = this.colorComboBox.getSelectedColor();
            LUT[]luts = this.outputGUI.getImagePlus().getLuts();
            luts[index] = LUT.createLutFromColor(color);

            ((CompositeImage)this.outputGUI.getImagePlus()).setLuts(luts);
            this.outputGUI.repaint();
            this.outputGUI.getImagePlus().setSlice(index+1);
        });

        this.comboBox.addActionListener(event -> {
            //final int index = this.comboBox.getSelectedIndex();
        });
        this.reset.addActionListener(event -> {
            ((CompositeImage)this.outputGUI.getImagePlus()).setLuts(this.outputGUI.getOriginalLuts());
            this.outputGUI.getImagePlus().setSlice(1);
        });

    }

    @Override
    public void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(this.labelCombo, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.comboBox, gbc);

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("Color:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.colorComboBox, gbc);

        // Row 4
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        this.add(reset, gbc);

        // Configure the combo boxes
        this.populateCombo();
        this.pack();
    }

    private void populateCombo(){
        for(int i = 0; i < this.outputGUI.getImagePlus().getStack().getSize(); i++){
            this.comboBox.addItem(this.outputGUI.getImagePlus().getStack().getSliceLabels()[i]);
        }

    }
}
