package com.ds4h.view.mainGUI;

import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.view.cornerSelectorGUI.CornerSelectorGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;

public class PreviewListItem extends JPanel {
    private final JButton targetButton;
    private final JLabel imageLabel;
    private final JLabel targetLabel;
    private final CornerController controller;
    private final ImageCorners image;
    private final PreviewImagesPane container;
    PreviewListItem(CornerController controller, ImageCorners image, PreviewImagesPane container){
        this.container = container;
        this.controller = controller;
        this.image = image;
        this.targetButton = new JButton("Set");
        this.targetLabel = new JLabel("TARGET");
        this.imageLabel = new JLabel(new ImageIcon(this.image.getImage().getBufferedImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //we set the Target label visible only if this is the taret image
        this.setVisibilityTargetLabel();
        this.setSelectedPanel();
        this.add(this.imageLabel);
        this.add(this.targetButton);
        this.add(this.targetLabel);

        this.targetButton.addActionListener(event -> {
            this.controller.changeTarget(image);
            this.container.updateList();
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Set the clicked image as the current image
                container.setCurrentPanel(PreviewListItem.this);
                setSelectedPanel();
                new CornerSelectorGUI(image).showDialog();
            }
        });
    }
    private void setVisibilityTargetLabel(){
        this.targetLabel.setVisible(this.controller.isSource(this.image));
    }
    private void setSelectedPanel(){
        if(this.equals(this.container.getCurrentPanel())){
            Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
            Arrays.stream(this.container.getComponents()).map(i->(JPanel)i).forEach(i -> i.setBorder(null));
            this.setBorder(border);
        }

        container.repaint();

    }
}
