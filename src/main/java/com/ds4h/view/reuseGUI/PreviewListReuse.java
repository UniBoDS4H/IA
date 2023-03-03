package com.ds4h.view.reuseGUI;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.imageController.ImageController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.standardGUI.StandardGUI;

import javax.swing.*;
import java.awt.*;

public class PreviewListReuse extends JPanel implements StandardGUI {
    private final JButton removeButton;
    private final JLabel imageLabel;
    private final JLabel removeLabel;
    private final JTextField textField;
    private final ImageController controller;
    private final AlignedImage image;
    private final ReuseImagesPanel container;
    private boolean reuse;

    public PreviewListReuse(final ImageController controller, final AlignedImage image, final ReuseImagesPanel container){
        this.container = container;
        this.controller = controller;
        this.image = image;
        this.reuse = true;
        this.removeButton = new JButton("INCLUDE");
        this.textField = new JTextField(this.image.getAlignedImage().getTitle());
        this.removeLabel = new JLabel("INCLUDED IN THE REUSE");
        this.imageLabel = new JLabel(new ImageIcon(this.image.getAlignedImage().getBufferedImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //we set the Target label visible only if this is the taret image
        this.setVisibilityTargetLabel();
        this.setSelectedPanel();
        this.addComponents();
        this.addListeners();

    }
    private void setVisibilityTargetLabel(){

    }
    private void setSelectedPanel(){

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void addListeners() {
        this.removeButton.addActionListener(event -> {
            if(this.removeButton.getBackground() == Color.RED) {
                this.removeLabel.setText("INCLUDED IN THE REUSE");
                this.reuse = true;
                this.removeButton.setBackground(Color.GREEN);
            }else{
                this.reuse = false;
                this.removeLabel.setText("EXCLUDED FROM REUSE");
                this.removeButton.setBackground(Color.RED);
            }
        });
    }

    public boolean toReuse(){
        return this.reuse;
    }

    public AlignedImage getImage(){
        return this.image;
    }

    @Override
    public void addComponents() {
        this.removeButton.setBackground(Color.GREEN);
        //this.textField.setSize(new Dimension(200,50));
        this.add(this.imageLabel);
        //this.add(this.textField);
        this.add(this.removeButton);
        this.add(this.removeLabel);
    }
}
