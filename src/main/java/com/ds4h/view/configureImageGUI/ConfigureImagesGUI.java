package com.ds4h.view.configureImageGUI;

import com.ds4h.model.util.Pair;
import com.ds4h.view.overlapImages.OverlapImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ConfigureImagesGUI extends JFrame implements StandardGUI {
    private final JButton reset;
    private final JButton colorButton;
    private final JComboBox<String> comboBox;
    private final JSlider slider;
    private final JLabel labelButton, labelCombo, labelSlider;
    private Color color = Color.RED;
    private final GridBagConstraints constraints;

    private final static int WIDTH = 700, HEIGHT = 400, DEFAULT = 2;
    private final static float DIV = 10f;
    private final List<Pair<ImagePlus, Color>> defaultColors;
    private final List<OverlapImagesGUI.ImagePanel> imagePanels;
    public ConfigureImagesGUI(final List<ImagePlus> images){
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;
        this.setLayout(new GridBagLayout());
        this.defaultColors = new LinkedList<>();
        this.imagePanels = new LinkedList<>();
        images.forEach(image -> defaultColors.add(new Pair<>(image, image.getImage().getGraphics().getColor())));
        this.reset = new JButton("Reset");
        this.labelButton = new JLabel("Choose a color for the image");
        this.labelCombo = new JLabel("Choose the Image");
        this.labelSlider = new JLabel("Choos the opacity of the image");
        this.comboBox = new JComboBox<>();
        this.colorButton = new JButton("Choose color");
        this.slider = new JSlider(0,10);
        this.addComponents();
        this.addListeners();
    }

    @Override
    public void showDialog() {
        this.setVisible(true);
    }

    @Override
    public void addListeners() {

        this.colorButton.addActionListener(event -> {
            color = JColorChooser.showDialog(this, "Choose color", color);
            //TODO:SELECT THE INDEXED IMAGE AND CHANGE HIS COLOR
            //TODO:UNDERSTAND HOW CAN I CHANGE THE BACKGROUND COLOR OF THE IMAGE WITHOUT DESTROY THE IMAGE ITSELF
            //TODO:TRY WITH THE IMAGEPROCESSOR, CONFIGURING THE IMAGE CHANNEL

        });
        this.comboBox.addActionListener(evenet -> {
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
        this.addElement(this.labelButton, new JPanel(), this.colorButton);
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
