package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.ColorComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CornerSelectorSettingsGUI extends Frame implements StandardGUI {
    private final ColorComboBox pointerColor;
    private final ColorComboBox selectedPointerColor;
    private final ColorComboBox textColor;

    private final GridBagConstraints constraints;
    private final CornerSelectorGUI container;
    private final JSlider pointerDimension;
    private final JButton changeButton;
    private final JComboBox<Integer> indexFrom;
    private final JComboBox<Integer> indexTo;
    public CornerSelectorSettingsGUI(CornerSelectorGUI container){
        super("Settings");
        this.container = container;
        this.pointerColor = new ColorComboBox();
        this.changeButton = new JButton("Change");
        this.indexFrom = new JComboBox<>();
        this.indexTo = new JComboBox<>();
        this.selectedPointerColor = new ColorComboBox();
        this.textColor = new ColorComboBox();
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
            container.getImage().editPointIndex(from-1, to-1);
            container.repaint();
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
        this.addElement(new JLabel("Corner dimension: "), new JPanel(), this.pointerDimension);
        JPanel changeIndex = new JPanel();
        changeIndex.add(this.indexFrom);
        changeIndex.add(this.indexTo);
        changeIndex.add(changeButton);
        this.addElement(new JLabel("Change corner index: "), new JPanel(), changeIndex);
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
