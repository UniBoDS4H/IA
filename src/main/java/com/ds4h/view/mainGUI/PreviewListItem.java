package com.ds4h.view.mainGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.view.pointSelectorGUI.PointSelectorCanvas;
import com.ds4h.view.pointSelectorGUI.PointSelectorGUI;
import com.ds4h.view.displayInfo.DisplayInfo;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//TODO: Implement the StandardGUI interface

public class PreviewListItem extends JPanel {
    private final JButton targetButton, deleteButton;
    private final JLabel idLabel;
    private final JLabel imageLabel;
    private final PointController controller;
    private final ImagePoints image;
    private final PreviewImagesPane container;
    private PointSelectorGUI pointSelector;
    private final JPanel centerPanel;
    private boolean firstOpening = true;
    PreviewListItem(final PointController controller, final ImagePoints image, final PreviewImagesPane container, final int id){
        this.container = container;
        this.controller = controller;
        this.centerPanel = new JPanel();
        this.image = image;
        this.idLabel = new JLabel(Integer.toString(id));
        this.idLabel.setFont(new Font("Serif", Font.BOLD, DisplayInfo.getTextSize(5)));
        this.targetButton = new JButton("TARGET");
        ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/icons/remove.png"));
        ImageIcon resized = new ImageIcon(deleteIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        this.deleteButton = new JButton(resized);
        this.deleteButton.setBorder(null);
        this.deleteButton.setBorderPainted(false);
        this.deleteButton.setContentAreaFilled(false);
        this.deleteButton.setOpaque(false);
        this.deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.imageLabel = new JLabel(new ImageIcon(this.image.resize(40,40,"bilinear").getBufferedImage()));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.X_AXIS));
        //we set the Target label visible only if this is the taret image
        this.updateTargetView();

        this.centerPanel.add(this.idLabel);
        this.centerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.centerPanel.add(this.imageLabel);
        this.centerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.centerPanel.add(this.targetButton);
        this.centerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.centerPanel.add(this.deleteButton);
        this.setBorder( new MatteBorder(0, 0, 1, 0, Color.gray));


        JLabel nameLabel = new JLabel(this.image.toString());
        nameLabel.setFont(new Font("Serif", Font.PLAIN, DisplayInfo.getTextSize(3)));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(this.leftJustify(nameLabel));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(this.leftJustify(this.centerPanel));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
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
                if(firstOpening){
                    pointSelector = new PointSelectorGUI(image, controller);
                    firstOpening = false;
                }else{
                    pointSelector.showWindow();
                }
            }
        });
    }
    private Component leftJustify( Component c )  {
        Box  b = Box.createHorizontalBox();
        b.add(Box.createRigidArea(new Dimension(5, 0)));
        b.add(c);
        b.add( Box.createHorizontalGlue() );
        return b;
    }
    private void updateTargetView(){
        this.targetButton.setBackground(this.controller.isSource(this.image)?new Color(0,153,0):Color.GRAY);
    }
}
