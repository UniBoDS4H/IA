package com.ds4h.view.overlapImages;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.overlapImages.OverlapImagesGUI.ImagePanel;
import com.ds4h.view.util.ColorComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class ConfigPanel extends JPanel implements StandardGUI {
    private final JButton reset;
    private final JComboBox<String> comboBox;
    private final JSlider opacitySlider;
    private final ColorComboBox colorBox;
    private final AlignmentControllerInterface controller;
    private final JLabel labelCombo, labelSlider;

    private final static int WIDTH = 700, HEIGHT = 400, DEFAULT = (int)(OverlapImagesGUI.ImagePanel.DEFAULT_OPACITY * 10);
    private final static float DIV = 10f;
    private final List<OverlapImagesGUI.ImagePanel> imagePanels;
    private final List<Color> colorList = new LinkedList<>();
    public ConfigPanel(final AlignmentControllerInterface controller){
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.controller = controller;
        this.setLayout(new FlowLayout());
        this.imagePanels = new LinkedList<>();
        this.colorBox = new ColorComboBox();
        this.reset = new JButton("Reset");
        this.labelCombo = new JLabel("Image: ");
        this.labelSlider = new JLabel("Opacity: ");
        this.comboBox = new JComboBox<>();
        this.opacitySlider = new JSlider(0,10);
        this.addComponents();
        this.addListeners();
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {
        this.colorBox.addActionListener(event -> {
            System.out.println("ao");
            final int index = this.colorBox.getSelectedIndex();
            final int indexImage = this.comboBox.getSelectedIndex();
            final Color color = this.colorBox.getItemAt(index);
            this.imagePanels.get(indexImage).changeColor(color);
        });

        this.comboBox.addActionListener(event -> {
            final int index = this.comboBox.getSelectedIndex();
            this.opacitySlider.setValue(Math.round(this.imagePanels.get(index).getOpacity()*DIV));
            //this.colorBox.setSelectedItem(this.imagePanels.get(index).getColor());
        });
        this.opacitySlider.addChangeListener(event -> {
            final float value = (this.opacitySlider.getValue() / DIV);
            final int index = this.comboBox.getSelectedIndex();
            this.imagePanels.get(index).setOpacity(value);
        });
        this.reset.addActionListener(evenet -> {
            this.imagePanels.forEach(imageP -> {
                imageP.setOpacity(OverlapImagesGUI.ImagePanel.DEFAULT_OPACITY);
                imageP.resetImage();
                imageP.setDefaultColor();
            });
            this.opacitySlider.setValue(DEFAULT);

        });


    }

    public void setElements(final List<OverlapImagesGUI.ImagePanel> imagePanels){
        this.imagePanels.clear();
        this.imagePanels.addAll(imagePanels);
    }

    @Override
    public void addComponents() {
        this.addElement(this.labelCombo, new JPanel(), this.comboBox);
        this.addElement(new JLabel("Color: "), new JPanel(), this.colorBox);
        this.addElement(this.labelSlider, new JPanel(), this.opacitySlider);
        this.opacitySlider.setMajorTickSpacing(5);
        this.opacitySlider.setMinorTickSpacing(1);
        this.opacitySlider.setPaintTicks(true);
        this.opacitySlider.setPaintLabels(true);
        this.populateCombo();
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(reset);
        this.add(buttonPanel);
    }

    private void populateCombo(){
        int index = 0;
        for(AlignedImage image : this.controller.getAlignedImages()){
            this.comboBox.addItem(image.getAlignedImage().getTitle() + ":" + index);
            index++;
        }
    }
    private void addElement(final JLabel label, final JPanel panel, final JComponent component){
        panel.add(label);
        panel.add(component);
        add(panel, FlowLayout.LEFT);
    }
}
