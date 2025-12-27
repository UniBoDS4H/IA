package com.ds4h.view.automaticElasticConfigGUI;

import com.drew.lang.annotations.NotNull;
import com.ds4h.controller.elastic.ElasticController;
import com.ds4h.model.deformation.ElasticAlgorithms;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.Constants;
import ij.IJ;

import javax.swing.*;
import java.awt.*;

public class AutomaticElasticConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<ElasticAlgorithms> algorithms;
    private final JTextField field;
    private final ElasticController controller;

    public AutomaticElasticConfigGUI(@NotNull final ElasticController controller) {
        this.setTitle("Automatic Elastic Config");
        this.controller = controller;
        this.algorithms = new JComboBox<>(this.controller.getAlgorithms());
        this.field = new JTextField(String.valueOf(10));
        this.setSize(new Dimension(300, 80));
        this.getContentPane().setLayout(new GridBagLayout());
        this.addComponents();
        this.addListeners();
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.algorithms.addActionListener(event -> {
            final ElasticAlgorithms selected = (ElasticAlgorithms) this.algorithms.getSelectedItem();
            this.controller.setAlgorithm(selected);
        });

        this.field.addActionListener(event -> {
            final String text = this.field.getText();
            try {
                final int value = Integer.parseInt(text);
                this.controller.setNumberOfLandmarks(value);
            } catch (Exception e) {
                IJ.log("Error setting number of landmarks is not a valid integer.");
            }
        });
    }

    @Override
    public void addComponents() {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // --- First row: Label and ComboBox ---
        final JLabel algLbl = new JLabel(Constants.ELASTIC_ALGORITHM_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(algLbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.algorithms, gbc);

        // --- Second row: Label and Slider ---
        final JLabel sliderLabel = new JLabel(Constants.ELASTIC_SLIDER_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(sliderLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.field, gbc);

        // Set default selection
        this.algorithms.setSelectedItem(ElasticAlgorithms.BIGWARP);

    }

}
