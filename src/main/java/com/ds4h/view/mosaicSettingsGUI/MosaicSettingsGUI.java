package com.ds4h.view.mosaicSettingsGUI;

import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;

public class MosaicSettingsGUI extends JFrame implements StandardGUI {

    private boolean isOrderAscending;
    private boolean isTargetImageForeground;
    private final JRadioButton rbDescendantOrder;
    private final JRadioButton rbAscendantOrder;
    private final JCheckBox cbTargetImage;

    public MosaicSettingsGUI() {
        this.setTitle("Mosaic Settings");
        this.isOrderAscending = true;
        this.isTargetImageForeground = true;
        this.rbDescendantOrder = new JRadioButton("Align the non-target images from the highest ID to the lowest");
        this.rbAscendantOrder = new JRadioButton("Align the non-target images from the lowest ID to the highest");
        this.cbTargetImage = new JCheckBox("Put the target image foreground on the non-target images");
        this.getContentPane().setLayout(new GridBagLayout());
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.rbDescendantOrder);
        buttonGroup.add(this.rbAscendantOrder);
        this.addComponents();
        this.addListeners();
    }

    @Override
    public void showDialog() {
        if (this.isOrderAscending)
            this.rbAscendantOrder.setSelected(true);
        else
            this.rbDescendantOrder.setSelected(true);

        this.cbTargetImage.setSelected(this.isTargetImageForeground);
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.rbAscendantOrder.addActionListener(event ->
            this.isOrderAscending = true
        );

        this.rbDescendantOrder.addActionListener(event ->
                this.isOrderAscending = false
        );

        this.cbTargetImage.addActionListener(event ->
                this.isTargetImageForeground = !this.isTargetImageForeground
        );
    }

    @Override
    public void addComponents() {
        final JLabel question = new JLabel("How do you prefer to align non-target images?");
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
        this.add(this.rbAscendantOrder, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.rbDescendantOrder, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.cbTargetImage, gbc);

        this.pack();
        this.setResizable(false);
    }

    public boolean isAlignmentOrderAscending() {
        return this.isOrderAscending;
    }

    public boolean isTargetImageForeground() {
        return this.isTargetImageForeground;
    }
}
