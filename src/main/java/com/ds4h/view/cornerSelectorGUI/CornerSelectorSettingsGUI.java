package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CornerSelectorSettingsGUI extends Frame implements StandardGUI {
    private final ColorComboBox pointerColor;
    private final ColorComboBox selectedPointerColor;
    private final ColorComboBox textColor;

    private final GridBagConstraints constraints;
    private final CornerSelectorGUI container;
    private final JSlider pointerDimension;

    public CornerSelectorSettingsGUI(CornerSelectorGUI container){
        this.container = container;
        this.pointerColor = new ColorComboBox();
        this.selectedPointerColor = new ColorComboBox();
        this.textColor = new ColorComboBox();
        this.setLayout(new GridBagLayout());
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;
        this.pointerDimension = new JSlider(1,10);

        this.setActualPointerStyles();

        this.addListeners();
        this.addComponents();
        this.setFrameSize();
    }

    private void setActualPointerStyles() {
        this.pointerColor.setSelectedItem(this.container.getCornerPanel().getPointerColor());
        this.selectedPointerColor.setSelectedItem(this.container.getCornerPanel().getSelectedPointerColor());
        this.textColor.setSelectedItem(this.container.getCornerPanel().getTextColor());
        this.pointerDimension.setValue(this.container.getCornerPanel().getPointerDimension());
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.pointerColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = pointerColor.getSelectedColor();
                container.setPointerColor(selectedColor);
            }
        });
        this.selectedPointerColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = selectedPointerColor.getSelectedColor();
                container.setSelectedPointerColor(selectedColor);
            }
        });
        this.textColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = textColor.getSelectedColor();
                container.setTextColor(selectedColor);
            }
        });
        this.pointerDimension.addChangeListener(e->{
            this.container.getCornerPanel().setPointerDimension(pointerDimension.getValue());
        });
    }

    @Override
    public void addComponents() {
        this.addElement(new JLabel("Pointer color: "), new JPanel(), this.pointerColor);
        this.addElement(new JLabel("Selected Pointer color: "), new JPanel(), this.selectedPointerColor);
        this.addElement(new JLabel("Pointer Index color: "), new JPanel(), this.textColor);
        this.pointerDimension.setMajorTickSpacing(4);
        this.pointerDimension.setMinorTickSpacing(1);
        this.pointerDimension.setPaintTicks(true);
        this.pointerDimension.setPaintLabels(true);
        this.addElement(new JLabel("Pointer dimension: "), new JPanel(), this.pointerDimension);
        this.constraints.gridy++;
    }
    private void setFrameSize(){
        final Dimension newDimension = DisplayInfo.getDisplaySize(30);
        setSize((int)newDimension.getWidth(), (int)newDimension.getHeight());
        setMinimumSize(newDimension);
    }
    private void addElement(final JLabel label, final JPanel panel, final JComponent component){
        panel.add(label);
        panel.add(component);
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        add(panel, this.constraints);
    }
}
