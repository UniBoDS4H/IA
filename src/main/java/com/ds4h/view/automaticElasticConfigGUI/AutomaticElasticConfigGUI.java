package com.ds4h.view.automaticElasticConfigGUI;

import com.drew.lang.annotations.NotNull;
import com.ds4h.controller.elastic.ElasticController;
import com.ds4h.model.deformation.ElasticAlgorithms;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.Constants;

import javax.swing.*;
import java.awt.*;

public class AutomaticElasticConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<ElasticAlgorithms> algorithms;
    private final ElasticController controller;

    public AutomaticElasticConfigGUI(@NotNull final ElasticController controller) {
        this.setTitle("Automatic Elastic Config");
        this.controller = controller;
        this.algorithms = new JComboBox<>(this.controller.getAlgorithms());
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
    }

    @Override
    public void addComponents() {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        final JLabel algLbl = new JLabel(Constants.ELASTIC_ALGORITHM_LABEL);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(algLbl, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.algorithms, gbc);
    }
}
