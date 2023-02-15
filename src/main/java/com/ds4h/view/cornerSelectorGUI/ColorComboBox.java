package com.ds4h.view.cornerSelectorGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class ColorComboBox extends JComboBox<Color> {
    private final List<Color> colorList = new LinkedList<>();

    public ColorComboBox() {
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.YELLOW);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.CYAN);
        colorList.add(Color.WHITE);
        colorList.add(Color.BLACK);
        DefaultComboBoxModel<Color> colorModel = new DefaultComboBoxModel<Color>();
        for(Color color : colorList){
            colorModel.addElement(color);
        }
        this.setModel(colorModel);

        this.setRenderer(new ListCellRenderer<Color>() {
            private final JTextField renderer = new JTextField();
            @Override
            public Component getListCellRendererComponent(JList<? extends Color> list, Color value, int index, boolean isSelected, boolean cellHasFocus) {
                renderer.setText(" ");
                renderer.setEditable(false);
                renderer.setBackground(value);
                renderer.setPreferredSize(new Dimension(50, 20));
                return renderer;
            }
        });
        this.setEditor(new ComboBoxEditor() {
            private final JTextField editor = new JTextField();
            private Color color;

            @Override
            public Component getEditorComponent() {
                return editor;
            }

            @Override
            public void setItem(Object anObject) {
                if (anObject instanceof Color) {
                    color = (Color) anObject;
                    editor.setText("");
                    editor.setOpaque(true);
                    editor.setEditable(false);
                    editor.setBackground(color);
                }
            }

            @Override
            public Object getItem() {
                return color;
            }

            @Override
            public void selectAll() {

            }

            @Override
            public void addActionListener(ActionListener l) {

            }

            @Override
            public void removeActionListener(ActionListener l) {

            }
        });
        this.setEnabled(true);
        this.setEditable(true);
    }

    public Color getSelectedColor() {
        final int index = this.getSelectedIndex();
        return this.getItemAt(index);
    }
}