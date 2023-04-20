package com.ds4h.view.loadingGUI;

import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.IJ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class LoadingGUI extends JFrame implements StandardGUI {

    private final static String GIF_PATH = "/icons/loading.gif";
    private final JLabel loadingLabel, text;

    private final LoadingType loadingType;
    private final JProgressBar progressBar;

    public LoadingGUI(final LoadingType loadingType){
        this.loadingType = loadingType;
        this.setTitle(loadingType.getTitle());
        Icon loadingGif = new ImageIcon(Objects.requireNonNull(LoadingGUI.class.getResource(GIF_PATH)));
        this.loadingLabel = new JLabel(loadingGif);
        this.setLayout(new GridBagLayout());
        this.text = new JLabel();
        this.progressBar = new JProgressBar();
        this.progressBar.setValue(0);
        this.progressBar.setStringPainted(true);
        Dimension dspSize = DisplayInfo.getDisplaySize();
        this.loadingLabel.setPreferredSize(new Dimension((int)dspSize.getWidth()/2,(int)dspSize.getHeight()));
        this.setSize((int)dspSize.getWidth()/4, (int)dspSize.getHeight()/4);
        this.addComponents();
        this.addListeners();
        this.setResizable(false);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        this.text.setVerticalAlignment(JLabel.CENTER);
        this.text.setHorizontalAlignment(JLabel.CENTER);
        this.text.setText(this.loadingType.getDescription());
        IJ.log("[LOADING GUI] text: " + this.text.getText());
        if(this.loadingType == LoadingType.ALGORITHM){
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(this.progressBar, gbc);
            gbc.gridy = 1;
            this.add(this.text,gbc);
        }else{
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(this.loadingLabel, gbc);
            gbc.gridx = 1;
            this.add(this.text,gbc);
        }
    }

    public void updateProgress(int val) {
        this.progressBar.setValue(val);
    }
}
