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

public class ExportZipGUI {
    private final JFrame frame;
    /*
    private final AlignmentControllerInterface controller;
    private final JFileChooser fileChooser;
    private final JButton button;
    private final JList<ExportImage> elements;

     */


    public ExportZipGUI(){
        List<JPanel> panels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JCheckBox checkBox = new JCheckBox("Checkbox " + (i + 1));
            JTextField textField = new JTextField("Textfield " + (i + 1), 20);
            panel.add(checkBox);
            panel.add(textField);
            panels.add(panel);
        }

        JList<JPanel> panelList = new JList<>(panels.toArray(new JPanel[panels.size()]));
        panelList.setCellRenderer(new PanelListCellRenderer());

        this.frame = new JFrame("JList of JPanels Example");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(panelList), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    static class PanelListCellRenderer implements ListCellRenderer<JPanel> {
        @Override
        public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel panel, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            return panel;
        }
    }
    /*
    private void setFrameSize(){
        final Dimension newDimension = DisplayInfo.getDisplaySize(50);
        this.setSize((int)newDimension.getWidth(), (int)newDimension.getHeight());
        this.setMinimumSize(newDimension);
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.button.addActionListener(event -> {
            final int result = fileChooser.showDialog(this, "Select a Directory");
            if(result == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                try {
                    ExportController.exportAsZip(this.controller.getAlignedImages(), path);
                } catch (IOException e) {
                    IJ.showMessage(e.getMessage());
                }
            }
        });
    }

    @Override
    public void addComponents() {
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 200));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (int i = 0; i < 50; i++) {
            JPanel innerPanel = new JPanel();
            innerPanel.add(new JCheckBox("Checkbox " + i));
            innerPanel.add(new JTextField("Textfield " + i));
            innerPanel.setPreferredSize(new Dimension(100, 100));
            innerPanel.setVisible(true);
            this.elements.add(innerPanel);

        }
        this.elements.setPreferredSize(new Dimension(100, 200));
        this.elements.setVisible(true);
        JScrollPane scrollPane = new JScrollPane(this.elements);
        scrollPane.setPreferredSize(new Dimension(200, 10));
        this.pack();
        this.add(scrollPane, BorderLayout.CENTER);

    }

    */
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
            this.addComponents();
        }

        public void addComponents() {
            this.add(this.fileName);
            this.add(this.exclude);
        }
    }
}
