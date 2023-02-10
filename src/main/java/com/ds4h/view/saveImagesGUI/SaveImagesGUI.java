package com.ds4h.view.saveImagesGUI;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SaveImagesGUI extends JFrame {
    private final DefaultListModel<JPanel> ps = new DefaultListModel<>();
    public SaveImagesGUI() {
        initUI();
    }

    private void initUI() {
        setTitle("List Box Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        JScrollPane scrollPane = new JScrollPane();
        for(int i = 0; i < 10; i++){
            final JPanel p = new JPanel();
            p.setVisible(true);
            p.setSize(new Dimension(400,50));
            p.add(new JTextField("test"));
            final JCheckBox c = new JCheckBox("test");
            c.addActionListener(evenet -> {
                c.setSelected(!c.isSelected());
            });
            p.add(c);
            p.setBackground(Color.GREEN);
            scrollPane.setView(p, i);
        }
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 100));
        panel.add(scrollPane);

        pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
