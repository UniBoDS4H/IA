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

//TODO: Implement the StandardGUI interface

public class PreviewListItem extends JPanel {
    private final JButton targetButton, deleteButton;
    private final JLabel idLabel;
    private final JLabel imageLabel;
    private final JLabel targetLabel;
    private final CornerController controller;
    private final ImageCorners image;
    private final PreviewImagesPane container;
    private final CornerSelectorGUI cornerSelector;
    PreviewListItem(CornerController controller, ImageCorners image, PreviewImagesPane container, int id){
        this.container = container;
        this.controller = controller;
        this.image = image;
        this.cornerSelector = new CornerSelectorGUI(this.image, this.controller);
        this.idLabel = new JLabel(Integer.toString(id));
        this.idLabel.setFont(new Font("Serif", Font.BOLD, 16));
        this.targetButton = new JButton("Set");
        this.targetLabel = new JLabel("TARGET");
        Icon deleteIcon = new ImageIcon(getClass().getResource("/icons/remove.png"));
        this.deleteButton = new JButton(deleteIcon);
        this.deleteButton.setBorder(null);
        this.deleteButton.setBorderPainted(false);
        this.deleteButton.setContentAreaFilled(false);
        this.deleteButton.setOpaque(false);
        this.deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.imageLabel = new JLabel(new ImageIcon(this.image.getBufferedImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //we set the Target label visible only if this is the taret image
        this.setVisibilityTargetLabel();
        this.setSelectedPanel();

        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.idLabel);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.imageLabel);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.targetButton);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.targetLabel);
        this.add(this.deleteButton);

        this.deleteButton.addActionListener(event -> {
            if(!this.controller.isTarget(image)) {
                //TODO: Launch a message dialog in order to confirm the deletion
                final int result = JOptionPane.showConfirmDialog(this,
                        "Are you sure to delete the selected image ?",
                        "Confirm operation",
                        JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    this.controller.removeImage(image);
                    this.container.updateList();
                }
            }
        });

        this.targetButton.addActionListener(event -> {
            this.controller.changeTarget(image);
            System.out.println("qua");
            this.container.updateList();
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Set the clicked image as the current image
                container.setCurrentPanel(PreviewListItem.this);
                setSelectedPanel();
                cornerSelector.showDialog();
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
        this.container.repaint();

    }
}
