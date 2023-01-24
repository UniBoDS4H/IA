package com.ds4h.view.aboutGUI;

import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AboutGUI extends Frame implements StandardGUI {
    private final Panel panel;
    private final JTextArea area;

    private static final String INFORMATIONS = "DS4H Image Alignment\n" +
            "Head of the Project : Prof.ssa Carbonaro Antonella - antonella.carbonaro@unibo.it\n" +
            "Prof. Piccinini Filippo  - f.piccinini@unibo.it\n" +
            "Made By\n" +
            "Iorio Matteo : matteo.iorio01@gmail.com\n" +
            "Vincenzi Fabio : fabio.vincenzi2001@gmail.com\n" +
            "Copyright (©) 2019 Data Science for Health (DS4H) Group. All rights reserved\n" +
            "License: GNU General Public License version 3.";
    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);
    public AboutGUI(){
        System.out.println(AboutGUI.INFORMATIONS);
        setTitle("About DS4H Image Alignment");
        setLayout(new BorderLayout());
        this.area = new JTextArea(AboutGUI.INFORMATIONS);
        this.area.setFont(AboutGUI.FONT);
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
