package com.ds4h.view.pointSelectorGUI;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PointSelectorMenuGUI extends JPanel {
    private final PointController pointController;
    private final ImagePoints image;
    private final JButton deleteButton;
    private final JLabel copyToLabel;
    private final JButton copyButton;
    private final JButton cornerSetting;
    private final JButton improveMatrix;
    private final JComboBox<MenuItem> copyToCombo;
    private final PointSelectorGUI container;
    private final PointSelectorSettingsGUI settings;
    private final ImageIcon resizedImproveCR, resizedImproveBW;
    public PointSelectorMenuGUI(PointController controller, ImagePoints image, PointSelectorGUI container){
        this.container = container;
        this.image = image;
        this.pointController = controller;
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        final MenuItem[] options = this.pointController.getPointImages().stream()
                .filter(i -> !i.equals(this.image)).map(this.pointController::getMenuItem).toArray(MenuItem[]::new);
        this.copyToCombo = new JComboBox<>(options);
        this.copyToCombo.setEditable(false);
        if(pointController.getPointImages().size() > 1) {
            this.copyToCombo.setSelectedIndex(0);
        }
        this.copyToLabel = new JLabel("Copy to");
        this.deleteButton = new JButton("Delete");
        this.copyButton = new JButton("Copy");
        ImageIcon settingsIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/settings.png")));
        ImageIcon improveIconBW = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/magic_BW.png")));
        ImageIcon improveIconCR = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/magic_CR.png")));
        ImageIcon resized = new ImageIcon(settingsIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        resizedImproveBW = new ImageIcon(improveIconBW.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        resizedImproveCR = new ImageIcon(improveIconCR.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        this.cornerSetting = new JButton(resized);
        this.improveMatrix = new JButton(resizedImproveBW);

        this.setIconButtons(cornerSetting);
        this.setIconButtons(improveMatrix);

        this.settings = new PointSelectorSettingsGUI(container);

        this.addComponents();
        this.addListeners();
        this.updateView();
    }

    private void setIconButtons(final JButton button){
        button.setBorder(null);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    public void addListeners() {
        this.deleteButton.addActionListener(e->{
            container.getSelectedPoints().forEach(image::removePoint);
            //container.updatePointsForAlignment();
            container.clearSelectedPoints();
            container.repaintPoints();

        });
        this.copyButton.addActionListener(e->{
            final MenuItem item = (MenuItem) copyToCombo.getSelectedItem();
            assert item != null;
            if(!pointController.copyPoints(container.getSelectedPoints(), item.getImage())){
                JOptionPane.showMessageDialog(this, "Some of the points are out of the selected image, they have not been copied");
            }else{
                JOptionPane.showMessageDialog(this, "Successfully copied " + container.getSelectedPoints().size() + " points.");
                container.checkPointsForAlignment();
            }
        });
        this.cornerSetting.addActionListener(e->{
            this.settings.showDialog();
        });

        this.improveMatrix.addActionListener(event  -> {
            if (this.image.toImprove()) {
                this.image.useStock();
                this.improveMatrix.setBackground(Color.RED);
                this.improveMatrix.setIcon(this.resizedImproveBW);
            } else {
                this.image.improve();
                this.improveMatrix.setIcon(this.resizedImproveCR);
            }
        });
    }
    public void addComponents(){
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.copyToLabel);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.copyToCombo);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.copyButton);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.deleteButton);
        this.add(Box.createHorizontalGlue());
        this.add(this.improveMatrix);
        this.add(Box.createRigidArea(new Dimension(5, 0)));

        this.add(this.cornerSetting);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
    }
    public void updateView(){
        this.copyButton.setEnabled(this.container.getSelectedPoints().size()!=0);
        this.copyToCombo.setEnabled(this.container.getSelectedPoints().size()!=0);
        this.deleteButton.setEnabled(this.container.getSelectedPoints().size()!=0);

    }

    public void updateSettings() {
        this.settings.updateView();
    }
    public void disposeSettings(){
        this.settings.dispose();
    }
}
