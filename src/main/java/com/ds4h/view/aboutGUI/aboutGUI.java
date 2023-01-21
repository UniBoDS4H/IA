package com.ds4h.view.aboutGUI;

import com.ds4h.view.standardGUI.StandardGUI;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class aboutGUI extends Frame implements StandardGUI {
    private Panel panel;
    private Label label;
    public aboutGUI(){
        setTitle("About DS4H Image Alignment");
        setLayout(new BorderLayout());
        this.label = new Label("This is My Program. It is a program that does something cool.");
        this.panel = new Panel();


        pack();
        setVisible(true);
    }

    @Override
    public void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

            }
        });
    }

    @Override
    public void addComponents() {
        panel.setLayout(new FlowLayout());
        this.panel.add(this.label);
        add(this.panel, BorderLayout.CENTER);
    }
}
