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
        this.setTitle("The alignment algorithm is running");
        this.loadingLabel = new JLabel(this.loadingGif);
        this.loadingLabel.setSize(new Dimension(20, 20));
        this.setLayout(new GridLayout(1, 2));
        this.text = new JLabel();
        this.setSize(new Dimension(500, 300));
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
                IJ.showMessage("You can not interrupt the alignment algorithm");
            }
        });
    }

    @Override
    public void addComponents() {
        this.text.setVerticalAlignment(JLabel.TOP);
        this.text.setHorizontalAlignment(JLabel.LEFT);
        this.text.setText("<html>The alignment algorithm is running,<br/>" +
                " please wait until the end. If you aligned a lot of images It may take a bit of time, <br/>" +
                "please be patient until the alignment is done. </html>");
        this.add(this.loadingLabel, FlowLayout.LEFT);
        this.add(this.text);
    }
}
