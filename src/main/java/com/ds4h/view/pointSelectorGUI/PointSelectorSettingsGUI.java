package com.ds4h.view.pointSelectorGUI;

import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.ColorComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PointSelectorSettingsGUI extends Frame implements StandardGUI {
    private final ColorComboBox pointerColor;
    private final ColorComboBox selectedPointerColor;
    private final ColorComboBox textColor;

    private final GridBagConstraints constraints;
    private final PointSelectorGUI container;
    private final JSlider pointerDimension, contrastSlider;
    private final JButton changeButton, invertButton;
    private final JComboBox<Integer> indexFrom;
    private final JComboBox<Integer> indexTo;
    private float contrast = 0.0f;
    public PointSelectorSettingsGUI(final PointSelectorGUI container){
        super("Settings");
        this.container = container;
        this.pointerColor = new ColorComboBox();
        this.changeButton = new JButton("Change");
        this.invertButton = new JButton("Invert");
        this.indexFrom = new JComboBox<>();
        this.indexTo = new JComboBox<>();
        this.selectedPointerColor = new ColorComboBox();
        this.textColor = new ColorComboBox();
        this.contrastSlider = new JSlider(0, 10);

        this.setLayout(new GridBagLayout());
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;
        this.pointerDimension = new JSlider(1,10);

        this.setActualPointerStyles();
        this.setCornerComboBox();
        this.addListeners();
        this.addComponents();
        this.updateChangeButton();
        this.pack();
    }
    private void updateChangeButton(){
        if(indexFrom.getSelectedItem() != null && indexTo.getSelectedItem() != null
                && (int)indexTo.getSelectedItem() != (int)indexFrom.getSelectedItem()){
            this.changeButton.setEnabled(true);
        }else{
            this.changeButton.setEnabled(false);
        }

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
        this.pointerDimension.addChangeListener(e->{
            this.container.getCanvas().setPointerDimension(pointerDimension.getValue());
        });
        this.changeButton.addActionListener(e -> {
            int from = (int)indexFrom.getSelectedItem();
            int to = (int)indexTo.getSelectedItem();
            this.container.getImage().editPointIndex(from-1, to-1);
            this.container.updatePoints();
        });

        this.contrastSlider.addChangeListener(event->{
            this.contrast  = this.contrastSlider.getValue()/10.0f;
            //this.container.setImage(ChangeColorController.changeContrast(this.container.getImage().getOriginalImage(), this.contrast));
            this.container.repaint();
        });

        this.invertButton.addActionListener(event -> {
            //this.container.setImage(ChangeColorController.invert(this.container.getImage().getImage()));
            this.container.repaint();
        });
        this.indexFrom.addActionListener(e->{
            this.updateChangeButton();
        });
        this.indexTo.addActionListener(e->{
            this.updateChangeButton();
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
        this.contrastSlider.setMaximum(10);
        this.contrastSlider.setMinimum(0);
        this.contrastSlider.setMajorTickSpacing(5);
        this.contrastSlider.setMinorTickSpacing(1);
        this.contrastSlider.setPaintLabels(true);
        this.contrastSlider.setPaintTicks(true);
        this.addElement(new JLabel("Corner dimension: "), new JPanel(), this.pointerDimension);
        this.addElement(new JLabel("Contrast: "), new JPanel(), this.contrastSlider);
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
