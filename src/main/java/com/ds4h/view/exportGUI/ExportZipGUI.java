package com.ds4h.view.exportGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.exportController.ExportController;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.IJ;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExportZipGUI implements StandardGUI{
    private final JFrame frame;
    private final JButton button;
    private final AlignmentControllerInterface controllerInterface;
    private final List<JPanel> panels;
    private final JList<JPanel> panelList;
    private final JFileChooser fileChooser;


    public ExportZipGUI(final AlignmentControllerInterface controller){
        this.button = new JButton("Save");
        this.controllerInterface = controller;
        this.panels = new ArrayList<>();
        this.controllerInterface.getAlignedImages().forEach(image -> {
            final ExportImage panel = new ExportImage(image.getAlignedImage().getTitle());
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.setVisible(true);
            panel.setFocusable(true);
            panels.add(panel);
        });
        this.panelList  = new JList<>(panels.toArray(new JPanel[0]));
        this.panelList.setVisible(true);
        this.panelList.setFocusable(true);
        this.panelList.addListSelectionListener(event -> {
            System.out.println("dio porco");
        });
        this.panelList.setCellRenderer(new PanelListCellRenderer());
        this.fileChooser = new JFileChooser();

        this.frame = new JFrame("JList of JPanels Example");
        this.addComponents();
        this.addListeners();

    }
    static class PanelListCellRenderer implements ListCellRenderer<JPanel> {
        @Override
        public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel panel, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            return panel;
        }
    }

    private void setFrameSize(){
        final Dimension newDimension = DisplayInfo.getDisplaySize(50);
        this.frame.setSize((int)newDimension.getWidth(), (int)newDimension.getHeight());
        this.frame.setMinimumSize(newDimension);
    }

    @Override
    public void showDialog() {
        this.frame.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.button.addActionListener(event -> {
            final int result = fileChooser.showDialog(this.frame, "Select a Directory");
            if(result == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                try {
                    ExportController.exportAsZip(this.controllerInterface.getAlignedImages(), path);
                } catch (IOException e) {
                    IJ.showMessage(e.getMessage());
                }
            }
        });
    }

    @Override
    public void addComponents() {
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.frame.getContentPane().add(new JScrollPane(panelList), BorderLayout.CENTER);
        this.frame.getContentPane().add(this.button, BorderLayout.SOUTH);
        this.frame.pack();
    }

    private static class ExportImage extends JPanel{
        private final JCheckBox exclude;
        private final JTextField fileName;

        public  ExportImage(final String name){
            this.setSize(new Dimension(1000, 10));
            this.setLayout(new BorderLayout());
            this.exclude = new JCheckBox();
            this.fileName = new JTextField();
            this.fileName.setText(name);
            this.exclude.setText("Exclude from saving");
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.addComponents();
        }

        public String getFileName(){
            return this.fileName.getText();
        }

        public void addComponents() {
            this.setVisible(true);
            this.setFocusable(true);
            this.add(this.fileName);
            this.add(this.exclude);
        }
    }
}
