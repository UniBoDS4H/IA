package com.ds4h.view.mainGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.pointSelectorGUI.PointSelectorGUI;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

//TODO: Implement the StandardGUI interface

public class PreviewListItem extends JPanel {
    private final JButton targetButton;
    private final PointController controller;
    private final ImagePoints image;
    private final PreviewImagesPane container;
    private PointSelectorGUI pointSelector;
    private boolean firstOpening = true;
    PreviewListItem(final PointController controller, final ImagePoints image, final PreviewImagesPane container, final int id){
        this.container = container;
        this.controller = controller;
        final JPanel centerPanel = new JPanel();
        this.image = image;
        final JLabel nameLabel = new JLabel(this.image.toString());
        final JLabel idLabel = new JLabel(Integer.toString(id));
        idLabel.setFont(new Font("Serif", Font.BOLD, DisplayInfo.getTextSize(5)));
        this.targetButton = new JButton("TARGET");
        final ImageIcon deleteIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/remove.png")));
        final ImageIcon resized = new ImageIcon(deleteIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton deleteButton = new JButton(resized);
        deleteButton.setBorder(null);
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setOpaque(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel imageLabel = new JLabel(new ImageIcon(this.image.getProcessor().resize(40, 40).getBufferedImage()));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        //we set the Target label visible only if this is the taret image
        this.updateTargetView();

        centerPanel.add(idLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        centerPanel.add(this.targetButton);
        centerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        centerPanel.add(deleteButton);
        this.setBorder( new MatteBorder(0, 0, 1, 0, Color.gray));



        nameLabel.setFont(new Font("Serif", Font.PLAIN, DisplayInfo.getTextSize(3)));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(this.leftJustify(nameLabel));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(this.leftJustify(centerPanel));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        deleteButton.addActionListener(event -> {
            if(!this.controller.isTarget(image)) {
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
            this.container.updateList();
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(firstOpening){
                    pointSelector = new PointSelectorGUI(image, controller);
                    pointSelector.setMainGUI(container.getMainGUI());
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
        this.targetButton.setBackground(this.controller.isTarget(this.image)?new Color(0,153,0):Color.GRAY);
    }
}
