package com.ds4h.view.configureImageGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.overlapImages.OverlapImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ConfigureImagesGUI extends JFrame implements StandardGUI {
    private final JButton reset;
    private final JComboBox<String> comboBox;
    private final JSlider opacitySlider, redSlider, greenSlider, blueSlider;
    private final JComboBox<JLabel> colorBox;
    private final AlignmentControllerInterface controller;
    private final JLabel labelCombo, labelSlider;
    private final GridBagConstraints constraints;

    private final static int WIDTH = 700, HEIGHT = 400, DEFAULT = 2;
    private final static float DIV = 10f;
    private final List<OverlapImagesGUI.ImagePanel> imagePanels;
    private final List<Color> colorList = new LinkedList<>();
    public ConfigureImagesGUI(final AlignmentControllerInterface controller){
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.controller = controller;
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;
        this.setLayout(new GridBagLayout());
        this.redSlider = new JSlider(0, 255);
        this.greenSlider = new JSlider(0, 255);
        this.blueSlider = new JSlider(0, 255);
        this.imagePanels = new LinkedList<>();
        this.colorBox = new JComboBox<>();
        this.reset = new JButton("Reset");
        this.labelCombo = new JLabel("Choose the Image");
        this.labelSlider = new JLabel("Choos the opacity of the image");
        this.comboBox = new JComboBox<>();
        this.populateList();
        this.opacitySlider = new JSlider(0,10);
        this.addComponents();
        this.addListeners();
    }

    private void populateList(){
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.YELLOW);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.CYAN);
        colorList.add(Color.PINK);
        colorList.add(Color.ORANGE);
        //this.populateColors();
    }

    private void populateColors(){
        int index = 0;
        for(Color color : colorList){
            final JLabel label = new JLabel();
            label.setBackground(color);
            this.colorBox.insertItemAt(label, index);
            index++;
        }
    }

    private void configureSlider(final JSlider slider){
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTrack(true);
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {

        this.redSlider.addChangeListener(event -> {
            final int value = redSlider.getValue();
            final int index = this.comboBox.getSelectedIndex();
            this.imagePanels.get(index).changeRedChannel(value);
        });
        this.greenSlider.addChangeListener(event -> {
            final int value = greenSlider.getValue();
            final int index = this.comboBox.getSelectedIndex();
            this.imagePanels.get(index).changeGreenChannel(value);
        });
        this.blueSlider.addChangeListener(event -> {
            final int value = blueSlider.getValue();
            final int index = this.comboBox.getSelectedIndex();
            this.imagePanels.get(index).changeBlueChannel(value);
        });
        this.comboBox.addActionListener(event -> {
            final int index = this.comboBox.getSelectedIndex();
            this.opacitySlider.setValue(Math.round(this.imagePanels.get(index).getOpacity()*DIV));
        });
        this.opacitySlider.addChangeListener(event -> {
            final float value = (this.opacitySlider.getValue() / DIV);
            final int index = this.comboBox.getSelectedIndex();
            this.imagePanels.get(index).setOpacity(value);
        });
        this.reset.addActionListener(evenet -> {
            this.imagePanels.forEach(imageP -> imageP.setOpacity(OverlapImagesGUI.ImagePanel.DEFAULT_OPACITY));
            this.opacitySlider.setValue(DEFAULT);
            //TODO: set default value
        });

    }

    public void setElements(final List<OverlapImagesGUI.ImagePanel> imagePanels){
        this.imagePanels.clear();
        this.imagePanels.addAll(imagePanels);
    }

    @Override
    public void addComponents() {
        this.addElement(this.labelCombo, new JPanel(), this.comboBox);
        this.addElement(new JLabel("Red channel : "), new JPanel(), this.redSlider);
        this.addElement(new JLabel("Green channel : "), new JPanel(), this.greenSlider);
        this.addElement(new JLabel("Blue channel : "), new JPanel(), this.blueSlider);
        this.addElement(new JLabel("Pick a color : "), new JPanel(), this.colorBox);
        this.configureSlider(this.redSlider);
        this.configureSlider(this.greenSlider);
        this.configureSlider(this.blueSlider);
        this.addElement(this.labelSlider, new JPanel(), this.opacitySlider);
        this.opacitySlider.setMajorTickSpacing(5);
        this.opacitySlider.setMinorTickSpacing(1);
        this.opacitySlider.setPaintTicks(true);
        this.opacitySlider.setPaintLabels(true);
        this.populateCombo();
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(reset);
        this.constraints.gridy++;
        this.add(buttonPanel, this.constraints);
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
        this.constraints.gridx = 0;
        this.constraints.gridy++;
        add(panel, this.constraints);
    }
}
