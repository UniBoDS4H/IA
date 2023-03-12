package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.controller.changeColorController.ChangeColorController;
import com.ds4h.controller.imagingConversion.ImagingController;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.ColorComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class CornerSelectorSettingsGUI extends Frame implements StandardGUI {
    private final ColorComboBox pointerColor;
    private final ColorComboBox selectedPointerColor;
    private final ColorComboBox textColor;

    private final GridBagConstraints constraints;
    private final CornerSelectorGUI container;
    private final JSlider pointerDimension, contrastSlider;
    private final JButton changeButton, applyButton, invertButton;
    private final JComboBox<Integer> indexFrom;
    private final JComboBox<Integer> indexTo;
    private float contrast = 0.0f;
    public CornerSelectorSettingsGUI(final CornerSelectorGUI container){
        super("Settings");
        this.container = container;
        this.pointerColor = new ColorComboBox();
        this.changeButton = new JButton("Change");
        this.invertButton = new JButton("Invert");
        this.applyButton = new JButton("Apply");
        this.indexFrom = new JComboBox<>();
        this.indexTo = new JComboBox<>();
        this.selectedPointerColor = new ColorComboBox();
        this.textColor = new ColorComboBox();
        this.contrastSlider = new JSlider(0, 20);

        this.setLayout(new GridBagLayout());
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;
        this.pointerDimension = new JSlider(1,10);

        this.setActualPointerStyles();
        this.setCornerComboBox();

        this.addListeners();
        this.addComponents();
        this.setFrameSize();
    }
    private void setCornerComboBox(){
        this.indexFrom.removeAllItems();
        this.indexTo.removeAllItems();
        for(int i = 1; i <=this.container.getImage().getPoints().length; i++){
            this.indexFrom.addItem(i);
            this.indexTo.addItem(i);
        }
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
        this.pointerColor.addActionListener(e -> {
            Color selectedColor = pointerColor.getSelectedColor();
            container.setPointerColor(selectedColor);
        });
        this.selectedPointerColor.addActionListener(e -> {
            Color selectedColor = selectedPointerColor.getSelectedColor();
            container.setSelectedPointerColor(selectedColor);
        });
        this.textColor.addActionListener(e -> {
            Color selectedColor = textColor.getSelectedColor();
            container.setTextColor(selectedColor);
        });
        this.pointerDimension.addChangeListener(e->{
            this.container.getCornerPanel().setPointerDimension(pointerDimension.getValue());
        });
        this.changeButton.addActionListener(e -> {
            int from = (int)indexFrom.getSelectedItem();
            int to = (int)indexTo.getSelectedItem();
            this.container.getImage().editPointIndex(from-1, to-1);
            //ChangeColorController.changeContrast(container.getImage().getImage(), this.contrast);
            this.container.repaint();
        });

        this.applyButton.addActionListener(event -> {
            this.contrast  = this.contrastSlider.getValue()/10.0f;
            this.container.setImage(ChangeColorController.changeContrast(this.container.getImage().getImage(), this.contrast));
            this.container.repaint();
        });

        this.invertButton.addActionListener(event -> {
            this.container.setImage(ChangeColorController.invert(this.container.getImage().getImage()));
            this.container.repaint();
        });
    }

    @Override
    public void addComponents() {
        this.addElement(new JLabel("Corner color: "), new JPanel(), this.pointerColor);
        this.addElement(new JLabel("Selected corner color: "), new JPanel(), this.selectedPointerColor);
        this.addElement(new JLabel("Corner index color: "), new JPanel(), this.textColor);
        this.pointerDimension.setMajorTickSpacing(9);
        this.pointerDimension.setMinorTickSpacing(1);
        this.pointerDimension.setPaintTicks(true);
        this.pointerDimension.setPaintLabels(true);
        this.contrastSlider.setMaximum(20);
        this.contrastSlider.setMinimum(0);
        this.contrastSlider.setMajorTickSpacing(5);
        this.contrastSlider.setMinorTickSpacing(1);
        this.contrastSlider.setPaintLabels(true);
        this.contrastSlider.setPaintTicks(true);
        this.addElement(new JLabel("Corner dimension: "), new JPanel(), this.pointerDimension);
        this.addElement(new JLabel("Contrast: "), new JPanel(), this.contrastSlider);
        this.addElement(new JLabel("Apply contrast"), new JPanel(), this.applyButton);
        JPanel changeIndex = new JPanel();
        changeIndex.add(new JLabel("From"));
        changeIndex.add(this.indexFrom);
        changeIndex.add(new JLabel("To"));
        changeIndex.add(this.indexTo);
        changeIndex.add(changeButton);
        this.addElement(new JLabel("Change corner index: "), new JPanel(), changeIndex);
        this.addElement(new JLabel("Change contrast: "), new JPanel(), this.invertButton);
        this.constraints.gridy++;
    }
    private void setFrameSize(){
        final Dimension newDimension = DisplayInfo.getDisplaySize(40);
        setSize((int)newDimension.getWidth(), (int)newDimension.getHeight());
        setMinimumSize(newDimension);
    }
    private void addElement(final JLabel label, final JPanel panel, final JComponent component){
        panel.add(label);
        panel.add(component);
        component.setBackground(this.getBackground());
        panel.setBackground(this.getBackground());
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        add(panel, this.constraints);
    }

    public void updateView() {
        this.setCornerComboBox();
    }
}
