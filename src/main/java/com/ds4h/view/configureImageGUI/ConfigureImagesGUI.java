package com.ds4h.view.configureImageGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.imageController.ImageController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.carouselGUI.AlignmentOutputGUI;
import com.ds4h.view.overlapImages.OverlapImagesGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.ColorComboBox;
import ij.CompositeImage;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.LUT;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ConfigureImagesGUI extends JFrame implements StandardGUI {
    private final JButton reset;
    private final JComboBox<String> comboBox;
    private final JSlider opacitySlider;
    private final ColorComboBox colorComboBox;
    private final JLabel labelCombo, labelSlider;
    private final GridBagConstraints constraints;

    private final static int WIDTH = 700, HEIGHT = 400, DEFAULT = (int)(OverlapImagesGUI.ImagePanel.DEFAULT_OPACITY * 10);
    private final static float DIV = 10f;
    private final List<OverlapImagesGUI.ImagePanel> imagePanels;
    private final List<Color> colorList = new LinkedList<>();
    private final AlignmentOutputGUI outputGUI;

    public ConfigureImagesGUI(AlignmentOutputGUI alignmentOutputGUI){
        this.outputGUI = alignmentOutputGUI;
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(0, 0, 5, 5);
        this.constraints.anchor = GridBagConstraints.WEST;
        this.setLayout(new GridBagLayout());
        this.imagePanels = new LinkedList<>();
        this.colorComboBox = new ColorComboBox();
        this.reset = new JButton("Reset");
        this.labelCombo = new JLabel("Choose the Image");
        this.labelSlider = new JLabel("Choose the opacity of the image");
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
        this.colorComboBox.addActionListener(event -> {
            final int index = this.comboBox.getSelectedIndex();
            final Color color = this.colorComboBox.getSelectedColor();
            LUT[]luts = this.outputGUI.getImagePlus().getLuts();
            luts[index] = LUT.createLutFromColor(color);

            ((CompositeImage)this.outputGUI.getImagePlus()).setLuts(luts);
            this.outputGUI.repaint();
            this.outputGUI.getImagePlus().setSlice(index+1);
        });

        this.comboBox.addActionListener(event -> {
            final int index = this.comboBox.getSelectedIndex();
        });
        this.opacitySlider.addChangeListener(event -> {
           /* final float value = (this.opacitySlider.getValue() / DIV);
            final int index = this.comboBox.getSelectedIndex();
            this.imagePanels.get(index).setOpacity(value);

            */
        });
        this.reset.addActionListener(evenet -> {
            ((CompositeImage)this.outputGUI.getImagePlus()).setLuts(this.outputGUI.getOriginalLuts());
            this.outputGUI.getImagePlus().setSlice(1);
        });

    }

    public void setElements(final List<OverlapImagesGUI.ImagePanel> imagePanels){
        this.imagePanels.clear();
        this.imagePanels.addAll(imagePanels);
    }

    @Override
    public void addComponents() {
        this.addElement(this.labelCombo, new JPanel(), this.comboBox);
        this.addElement(new JLabel("Pick a color : "), new JPanel(), this.colorComboBox);
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
        for(int i = 0; i < this.outputGUI.getImagePlus().getStack().getSize(); i++){
            this.comboBox.addItem(this.outputGUI.getImagePlus().getStack().getSliceLabels()[i]);
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
