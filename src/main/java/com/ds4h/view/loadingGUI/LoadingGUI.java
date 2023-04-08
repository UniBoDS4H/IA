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

    private final LoadingType loadingType;

    public LoadingGUI(final LoadingType loadingType){
        this.loadingType = loadingType;
        this.setTitle(loadingType.getTitle());
        this.loadingLabel = new JLabel(this.loadingGif);
        this.loadingLabel.setSize(new Dimension(20, 20));
        this.setLayout(new GridLayout(1, 2));
        this.text = new JLabel();
        this.setSize(new Dimension(300, 200));
        this.addComponents();
        this.addListeners();
    }

    public void close(){
        this.setVisible(false);
        this.dispose();
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            }
        });
    }


    @Override
    public void addComponents() {
        this.text.setVerticalAlignment(JLabel.CENTER);
        this.text.setHorizontalAlignment(JLabel.CENTER);
        this.text.setText(this.loadingType.getDescription());
        IJ.log("[LOADING GUI] text: " + this.text.getText());
        this.add(this.loadingLabel, FlowLayout.LEFT);
        this.add(this.text);
    }
}
