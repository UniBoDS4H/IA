package com.ds4h.view.aboutGUI;

import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AboutGUI extends Frame implements StandardGUI {
    private final Panel panel;
    private final JTextArea area;

    private static final String INFORMATIONS = "DS4H Image Alignment Tool version 1.1\n" +
            "Head of the Project:\n" +
            "Prof.ssa Antonella Carbonaro - antonella.carbonaro@unibo.it\n" +
            "Prof. Filippo Piccinini - f.piccinini@unibo.it\n" +
            "Made By:\n" +
            "Matteo Iorio: matteo.iorio01@gmail.com\n" +
            "Fabio Vincenzi: fabio.vincenzi2001@gmail.com\n" +
            "Lorenzo Rigoni: rigoni.lorenzo.21@gmail.com\n" +
            "Contributors:\n" +
            "Matteo Belletti\n" +
            "Stefano Belli\n" +
            "Marco Edoardo Duma\n" +
            "Copyright (©) 2019 Data Science for Health (DS4H) Group.\n" +
            "University of Bologna. All rights reserved\n" +
            "License: GNU General Public License version 3.";
    private static final Font FONT = new Font("Arial", Font.PLAIN, DisplayInfo.getTextSize(6));
    public AboutGUI(){
        setTitle("About DS4H Image Alignment");
        setLayout(new BorderLayout());
        this.area = new JTextArea(AboutGUI.INFORMATIONS);
        this.area.setFont(AboutGUI.FONT);
        this.area.setEnabled(false);
        this.area.setDisabledTextColor(Color.BLACK);
        this.panel = new Panel();
        this.addComponents();
        this.addListeners();

        pack();

    }

    @Override
    public void showDialog() {
        setVisible(true);
    }

    @Override
    public void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void addComponents() {
        panel.setLayout(new BorderLayout());
        this.panel.add(this.area);
        add(this.panel, BorderLayout.CENTER);
    }
}
