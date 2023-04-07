package com.ds4h.view.automaticAlignmentConfigGUI;

import com.ds4h.controller.alignmentController.AutomaticAlignmentController.AutomaticAlignmentController;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithmEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.TranslationalAlignment;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.standardGUI.StandardGUI;
import javax.swing.*;
import java.awt.*;
import static com.ds4h.model.util.AlignmentUtil.getEnumFromAlgorithm;

public class AutomaticAlignmentConfigGUI extends JFrame implements StandardGUI {
    private final JComboBox<Detectors> detectors;
    private final JSlider slider, sliderFactor;
    private final GridBagLayout layout;
    private final JCheckBox translationCheckbox;
    private final JCheckBox rotationCheckbox;
    private final JCheckBox scalingCheckbox;
    private final JComboBox<AlignmentAlgorithmEnum> algorithm;
    private final JTextArea text;
    private final AutomaticAlignmentController controller;
    private Detectors selectedDetector;
    private final MainMenuGUI container;

    public AutomaticAlignmentConfigGUI(MainMenuGUI container, AutomaticAlignmentController automaticAlignmentController){
        this.setTitle("Automatic alignment algorithm");
        this.controller = automaticAlignmentController;
        this.container = container;
        this.layout = new GridBagLayout();
        this.getContentPane().setLayout(this.layout);
        this.detectors = new JComboBox<>(Detectors.values());
        this.selectedDetector = Detectors.SURF;
        this.slider = new JSlider(0, 20);
        this.sliderFactor = new JSlider(Detectors.LOWER_BOUND, Detectors.UPPER_BOUND);

        this.sliderFactor.setMajorTickSpacing(1);
        this.sliderFactor.setMinorTickSpacing(1);
        this.sliderFactor.setPaintTicks(true);
        this.sliderFactor.setPaintLabels(true);
        this.sliderFactor.setValue(4);
        this.selectedDetector.setScaling(this.sliderFactor.getValue());

        this.slider.setMajorTickSpacing(5);
        this.slider.setMinorTickSpacing(1);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);


        this.slider.setValue((int)(this.selectedDetector.getFactor()*10));
        this.translationCheckbox = new JCheckBox("Translation");
        this.rotationCheckbox = new JCheckBox("Rotation");
        this.scalingCheckbox = new JCheckBox("Scaling");
        this.algorithm = new JComboBox<>(AlignmentAlgorithmEnum.values());
        this.text = new JTextArea();
        this.addComponents();
        this.addListeners();
        this.updateCheckBoxes();
    }
    @Override
    public void showDialog() {
        this.setVisible(true);
        this.detectors.setSelectedItem(this.selectedDetector);
        this.algorithm.setSelectedItem(getEnumFromAlgorithm(this.controller.getAlgorithm()));
        this.slider.setValue((int)(this.selectedDetector.getFactor()*10));
    }
    @Override
    public void addListeners() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.algorithm.addActionListener(event -> {
            AlignmentAlgorithmEnum selected = (AlignmentAlgorithmEnum) this.algorithm.getSelectedItem();
            assert selected != null;
            this.text.setText(selected.getDocumentation());
            this.controller.setAlgorithm(this.controller.getAlgorithmFromEnum(selected));
            this.container.checkPointsForAlignment();
            this.updateCheckBoxes();
        });
        this.detectors.addActionListener(event -> {
            this.selectedDetector = (Detectors) this.detectors.getSelectedItem();
            assert this.selectedDetector != null;
            this.slider.setValue((int)(this.selectedDetector.getFactor()*10));
        });
        this.slider.addChangeListener(event -> {
            this.selectedDetector.setFactor(this.slider.getValue());
        });
        this.rotationCheckbox.addActionListener(e->{
            if(this.controller.getAlgorithm() instanceof TranslationalAlignment){
                TranslationalAlignment alg = ((TranslationalAlignment) this.controller.getAlgorithm());
                alg.setTransformation(alg.getTranslate(),this.rotationCheckbox.isSelected(),alg.getScale());
            }
        });
        this.scalingCheckbox.addActionListener(e->{
            if(this.controller.getAlgorithm() instanceof TranslationalAlignment){
                TranslationalAlignment alg = ((TranslationalAlignment) this.controller.getAlgorithm());
                alg.setTransformation(alg.getTranslate(),alg.getRotate(),this.scalingCheckbox.isSelected());
            }
        });
        this.translationCheckbox.addActionListener(e->{
            if(this.controller.getAlgorithm() instanceof TranslationalAlignment){
                TranslationalAlignment alg = ((TranslationalAlignment) this.controller.getAlgorithm());
                alg.setTransformation(this.translationCheckbox.isSelected(),alg.getRotate(),alg.getScale());
            }
        });
        this.sliderFactor.addChangeListener(event -> {
            this.selectedDetector.setScaling(this.sliderFactor.getValue());
        });
    }

    private void updateCheckBoxes() {
        if(this.controller.getAlgorithm() instanceof TranslationalAlignment){
            TranslationalAlignment translational = (TranslationalAlignment) this.controller.getAlgorithm();
            this.scalingCheckbox.setEnabled(true);
            this.translationCheckbox.setEnabled(true);
            this.rotationCheckbox.setEnabled(true);
            this.scalingCheckbox.setSelected(translational.getScale());
            this.translationCheckbox.setSelected(translational.getTranslate());
            this.rotationCheckbox.setSelected(translational.getRotate());
        }else{
            this.scalingCheckbox.setSelected(true);
            this.translationCheckbox.setSelected(true);
            this.rotationCheckbox.setSelected(true);
            this.scalingCheckbox.setEnabled(false);
            this.translationCheckbox.setEnabled(false);
            this.rotationCheckbox.setEnabled(false);
        }

    }


    public Detectors getSelectedDetector(){
        return this.selectedDetector;
    }

    @Override
    public void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);


        final JLabel algLbl = new JLabel("Detector: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(algLbl, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.detectors, gbc);

        final JLabel slidLbl = new JLabel("Threshold factor: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        this.add(slidLbl, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.slider, gbc);


        final JLabel scalingLbl = new JLabel("Scaling factor: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        this.add(scalingLbl, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.sliderFactor, gbc);



        final JLabel algTypeLbl = new JLabel("Algorithm: ");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        this.add(algTypeLbl, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(this.algorithm, gbc);

        final JLabel infoLbl = new JLabel("Algorithm info: ");
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(infoLbl, gbc);

        this.text.setLineWrap(true);
        this.text.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(this.text);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(250, 80));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        this.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        this.add(this.translationCheckbox, gbc);

        gbc.gridx = 1;
        this.add(this.scalingCheckbox, gbc);

        gbc.gridx = 2;
        this.add(this.rotationCheckbox, gbc);

        this.pack();
        this.setResizable(false);
    }
    public boolean getScaling(){
        return this.scalingCheckbox.isSelected();
    }
    public boolean getRotation(){
        return this.rotationCheckbox.isSelected();
    }
    public boolean getTranslation(){
        return this.translationCheckbox.isSelected();
    }
}
