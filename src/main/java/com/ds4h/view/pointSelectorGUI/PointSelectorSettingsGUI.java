package com.ds4h.view.pointSelectorGUI;

import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.ColorComboBox;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class PointSelectorSettingsGUI extends Frame implements StandardGUI {
    private final ColorComboBox pointerColor;
    private final ColorComboBox selectedPointerColor;
    private final ColorComboBox textColor;

    private final PointSelectorGUI container;
    private final JSlider pointerDimension;
    private final JButton changeButton;
    private final JComboBox<Integer> indexFrom;
    private final JComboBox<Integer> indexTo;
    public PointSelectorSettingsGUI(final PointSelectorGUI container){
        super("Settings");
        this.container = container;
        this.pointerColor = new ColorComboBox();
        this.changeButton = new JButton("Apply");
        this.indexFrom = new JComboBox<>();
        this.indexTo = new JComboBox<>();
        this.selectedPointerColor = new ColorComboBox();
        this.textColor = new ColorComboBox();

        this.setLayout(new GridBagLayout());
        this.pointerDimension = new JSlider(1,10);

        this.setActualPointerStyles();
        this.setCornerComboBox();
        this.addListeners();
        this.addComponents();
        this.updateChangeButton();
    }
    private void updateChangeButton(){
        this.changeButton.setEnabled(indexFrom.getSelectedItem() != null && indexTo.getSelectedItem() != null
                && (int) indexTo.getSelectedItem() != (int) indexFrom.getSelectedItem());
    }
    private void setCornerComboBox(){
        this.indexFrom.removeAllItems();
        this.indexTo.removeAllItems();

        for(int i = 1; i <=this.container.getImage().totalPoints(); i++){
            this.indexFrom.addItem(i);
            this.indexTo.addItem(i);
        }
    }

    private void setActualPointerStyles() {
        this.pointerColor.setSelectedItem(this.container.getCanvas().getPointerColor());
        this.selectedPointerColor.setSelectedItem(this.container.getCanvas().getSelectedPointerColor());
        this.textColor.setSelectedItem(this.container.getCanvas().getTextColor());
        this.pointerDimension.setValue(this.container.getCanvas().getPointerDimension());
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
        this.setCornerComboBox();
    }

    @Override
    public void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.pointerColor.addActionListener(e -> {
            Color selectedColor = pointerColor.getSelectedColor();
            this.container.getCanvas().setPointerColor(selectedColor);
        });
        this.selectedPointerColor.addActionListener(e -> {
            Color selectedColor = selectedPointerColor.getSelectedColor();
            this.container.getCanvas().setSelectedPointerColor(selectedColor);
        });
        this.textColor.addActionListener(e -> {
            Color selectedColor = textColor.getSelectedColor();
            this.container.getCanvas().setTextColor(selectedColor);
        });
        this.pointerDimension.addChangeListener(event ->
            this.container.getCanvas().setPointerDimension(pointerDimension.getValue())
        );
        this.changeButton.addActionListener(e -> {
            if(Objects.nonNull(indexFrom.getSelectedItem()) && Objects.nonNull(indexTo.getSelectedItem())) {
                int from = (int) indexFrom.getSelectedItem();
                int to = (int) indexTo.getSelectedItem();
                this.container.getImage().editPointIndex(from - 1, to - 1);
                this.container.updatePoints();
            }
        });
        this.indexFrom.addActionListener(event ->
            this.updateChangeButton()
        );
        this.indexTo.addActionListener(event ->
            this.updateChangeButton()
        );
    }

    @Override
    public void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Corner color:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        pointerColor.setPreferredSize(new Dimension(150, 30));
        add(pointerColor, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Selected corner color:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        selectedPointerColor.setPreferredSize(new Dimension(150, 30));
        add(selectedPointerColor, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Corner index color:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        textColor.setPreferredSize(new Dimension(150, 30));
        add(textColor, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Corner dimension:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        pointerDimension.setPreferredSize(new Dimension(200, 50));
        add(pointerDimension, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Change corner index:"), gbc);
        gbc.gridx = 1;
        indexFrom.setPreferredSize(new Dimension(50, 30));
        add(indexFrom, gbc);
        gbc.gridx = 2;
        add(new JLabel("to"), gbc);
        gbc.gridx = 3;
        indexTo.setPreferredSize(new Dimension(50, 30));
        add(indexTo, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        changeButton.setPreferredSize(new Dimension(80, 30));
        add(changeButton, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        this.pack();
        this.setResizable(false);
    }

    public void updateView() {
        this.setCornerComboBox();
    }
}
