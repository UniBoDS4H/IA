package com.ds4h.view.loadingGUI;

import com.ds4h.controller.directoryManager.DirectoryManager;
import com.ds4h.controller.opencvController.OpencvController;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.IJ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;

public class LoadingGUI extends JFrame implements StandardGUI {

    private final static String GIF_PATH = "/icons/loading.gif";
    private final Icon loadingGif = new ImageIcon(Objects.requireNonNull(LoadingGUI.class.getResource(GIF_PATH)));
    private final JLabel loadingLabel, text;

    public LoadingGUI(){
        this.setTitle("The algorithm is running");
        this.loadingLabel = new JLabel(this.loadingGif);
        this.loadingLabel.setSize(new Dimension(20, 20));
        this.setLayout(new GridLayout(1, 2));
        this.text = new JLabel();
        this.setSize(new Dimension(300, 200));
        this.addComponents();
        this.addListeners();
        this.setVisible(true);
    }

    public void close(){
        this.setVisible(false);
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void addListeners() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                IJ.showMessage("You can not interrupt the algorithm");
            }
        });
    }

    @Override
    public void addComponents() {
        this.text.setVerticalAlignment(JLabel.CENTER);
        this.text.setHorizontalAlignment(JLabel.CENTER);
        this.text.setText("<html>The algorithm is running, please wait.<br/> </html>");
        this.add(this.loadingLabel, FlowLayout.LEFT);
        this.add(this.text);
    }
}
