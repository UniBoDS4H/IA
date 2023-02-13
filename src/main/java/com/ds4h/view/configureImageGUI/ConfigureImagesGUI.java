package com.ds4h.view.configureImageGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.imagingConversion.ImagingController;
import com.ds4h.model.util.Pair;
import com.ds4h.view.overlapImages.OverlapImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.LinkedList;
import java.util.List;

public class ConfigureImagesGUI extends JFrame implements StandardGUI {
    private final JButton reset;
    private final JComboBox<String> comboBox;
    private final JSlider redSlider, greenSlider, blueSlider;
    private final JSlider slider;
    private final JLabel labelCombo, labelSlider;
    private Color color = Color.RED;
    private final GridBagConstraints constraints;

    private final static int WIDTH = 700, HEIGHT = 400, DEFAULT = 2;
    private final static float DIV = 10f;
    private final List<Pair<ImagePlus, Color>> defaultColors;
    private final List<OverlapImagesGUI.ImagePanel> imagePanels;
    public ConfigureImagesGUI(final AlignmentControllerInterface controller){
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;
        this.setLayout(new GridBagLayout());
        this.defaultColors = new LinkedList<>();
        this.redSlider = new JSlider(0, 255);
        this.greenSlider = new JSlider(0, 255);
        this.blueSlider = new JSlider(0, 255);
        this.imagePanels = new LinkedList<>();
        controller.getAlignedImages()
                .forEach(image -> defaultColors.add(new Pair<>(image.getAlignedImage(),
                        image.getAlignedImage().getImage().getGraphics().getColor())));
        this.reset = new JButton("Reset");
        this.labelCombo = new JLabel("Choose the Image");
        this.labelSlider = new JLabel("Choos the opacity of the image");
        this.comboBox = new JComboBox<>();
        this.slider = new JSlider(0,10);
        this.addComponents();
        this.addListeners();
    }

    private void configureSlider(final JSlider slider){
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
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
        this.comboBox.addActionListener(event -> {
            final int index = this.comboBox.getSelectedIndex();
            this.slider.setValue(Math.round(this.imagePanels.get(index).getOpacity()*DIV));
        });
        this.slider.addChangeListener(event -> {
            final float value = (this.slider.getValue() / DIV);
            final int index = this.comboBox.getSelectedIndex();
            this.imagePanels.get(index).setOpacity(value);
        });
        this.reset.addActionListener(evenet -> {
            this.imagePanels.forEach(imageP -> imageP.setOpacity(OverlapImagesGUI.ImagePanel.DEFAULT_OPACITY));
            this.slider.setValue(DEFAULT);
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
        this.configureSlider(this.redSlider);
        this.configureSlider(this.greenSlider);
        this.configureSlider(this.blueSlider);
        this.addElement(this.labelSlider, new JPanel(), this.slider);
        this.slider.setMajorTickSpacing(5);
        this.slider.setMinorTickSpacing(1);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);
        this.populateCombo();
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(reset);
        this.constraints.gridy++;
        this.add(buttonPanel, this.constraints);
    }

    private void populateCombo(){
        int index = 0;
        for(Pair<ImagePlus, Color> image : this.defaultColors){
            this.comboBox.addItem(image.getFirst().getTitle() + ":" + index);
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
