package com.ds4h.view.lutSettingsGUI;

import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.ConvertLutImageEnum;

import javax.swing.*;
import java.awt.*;

public class LutSettingsGUI extends JFrame implements StandardGUI {
    private ConvertLutImageEnum convertType;
    private final JRadioButton rbRGBConvert;
    private final JRadioButton rbEightBitConvert;

    public LutSettingsGUI() {
        this.setTitle("LUT Settings");
        this.convertType = ConvertLutImageEnum.CONVERT_TO_RGB;
        this.rbRGBConvert = new JRadioButton("Convert to a RGB image");
        this.rbEightBitConvert = new JRadioButton("Convert to an 8-bit image");
        this.getContentPane().setLayout(new GridBagLayout());
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.rbRGBConvert);
        buttonGroup.add(this.rbEightBitConvert);
        this.addComponents();
        this.addListeners();
    }

    @Override
    public void showDialog() {
        if (this.convertType == ConvertLutImageEnum.CONVERT_TO_RGB)
            this.rbRGBConvert.setSelected(true);
        else
            this.rbEightBitConvert.setSelected(true);
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.rbRGBConvert.addActionListener(event ->
                this.convertType = ConvertLutImageEnum.CONVERT_TO_RGB
        );

        this.rbEightBitConvert.addActionListener(event ->
                this.convertType = ConvertLutImageEnum.CONVERT_TO_EIGHT_BIT
        );
    }

    @Override
    public void addComponents() {
        final JLabel question = new JLabel("How do you prefer to convert 8-bit images with LUT applied?");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(question, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.rbRGBConvert, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.rbEightBitConvert, gbc);

        this.pack();
        this.setResizable(false);
    }

    public ConvertLutImageEnum getConvertType() {
        return this.convertType;
    }
}
