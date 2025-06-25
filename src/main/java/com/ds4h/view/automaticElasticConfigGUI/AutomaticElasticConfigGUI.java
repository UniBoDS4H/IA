package com.ds4h.view.automaticElasticConfigGUI;

import com.drew.lang.annotations.NotNull;
import com.ds4h.controller.elastic.ElasticController;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.model.deformation.ElasticAlgorithms;
import com.ds4h.view.automaticAlignmentConfigGUI.AutomaticAlignmentConfigGUI;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;

public class AutomaticElasticConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<ElasticAlgorithms> algorithms;
    private final ElasticController controller;

    public AutomaticElasticConfigGUI(@NotNull final ElasticController controller) {
        this.controller = controller;
        this.algorithms = new JComboBox<>(this.controller.getAlgorithms());
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void addListeners() {

    }

    @Override
    public void addComponents() {

    }
}
