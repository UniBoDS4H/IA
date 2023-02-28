package com.ds4h.view.loadingGUI;

import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;

public class LoadingGUI extends JFrame implements StandardGUI {

    private final Icon loadingGif = new ImageIcon(LoadingGUI.class.getResource("/icons/loading.gif"));
    private final JLabel loadingLabel, text;

    public LoadingGUI(){
        this.loadingLabel = new JLabel(this.loadingGif);
        this.loadingLabel.setSize(new Dimension(20, 20));
        this.setLayout(new GridLayout(1, 2));
        this.text = new JLabel();
        this.text.setText("The alignment algorithm is running,\n" +
                " please wait until the end.");
        this.add(this.loadingLabel, FlowLayout.LEFT);
        this.add(this.text);
        this.setSize(new Dimension(200, 100));
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

    }

    @Override
    public void addComponents() {

    }
}
