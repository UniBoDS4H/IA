package com.ds4h.view.saveImagesGUI;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.view.displayInfo.DisplayInfo;
import com.ds4h.view.standardGUI.StandardGUI;
import com.ds4h.view.util.ImageCache;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class SaveListItem extends JPanel implements StandardGUI {
    private final AlignedImage image;
    private final JButton reuseButton;
    private final JLabel idLabel;
    private final JLabel nameLabel;
    private final JLabel imageLabel;
    private final JPanel centerPanel;
    private boolean save;

    public SaveListItem(final AlignedImage image, final int id){
        this.centerPanel = new JPanel();
        this.image = image;
        this.save = true;
        this.idLabel = new JLabel(Integer.toString(id));
        this.idLabel.setFont(new Font("Serif", Font.BOLD, DisplayInfo.getTextSize(5)));
        this.reuseButton = new JButton("INCLUDE");
        this.nameLabel = new JLabel(this.image.getAlignedImage().getTitle());
        this.imageLabel = new JLabel(new ImageIcon(ImageCache.getScaledImage(this.image)));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.X_AXIS));
        this.addComponents();
        this.addListeners();
        this.updateButtons();
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void addListeners() {
        this.reuseButton.addActionListener(event -> {
            this.save = !this.save;
            this.updateButtons();
        });
    }

    private void updateButtons(){
        if(this.toSave()){
            this.reuseButton.setBackground(new Color(0,153,0));
            this.reuseButton.setText("INCLUDED");
            this.reuseButton.setForeground(Color.BLACK);
        }else{
            this.reuseButton.setBackground(new Color(153,0,0));
            this.reuseButton.setText("EXCLUDED");
            this.reuseButton.setForeground(Color.WHITE);
        }
    }

    public boolean toSave(){
        return this.save;
    }

    public AlignedImage getImage(){
        return this.image;
    }

    @Override
    public void addComponents() {
        this.centerPanel.add(this.idLabel);
        this.centerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.centerPanel.add(this.imageLabel);
        this.centerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.centerPanel.add(this.reuseButton);
        this.setBorder( new MatteBorder(0, 0, 1, 0, Color.gray));

        nameLabel.setFont(new Font("Serif", Font.PLAIN, DisplayInfo.getTextSize(3)));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(this.leftJustify(nameLabel));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(this.leftJustify(this.centerPanel));
        this.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    private Component leftJustify( Component c )  {
        Box  b = Box.createHorizontalBox();
        b.add(Box.createRigidArea(new Dimension(5, 0)));
        b.add(c);
        b.add( Box.createHorizontalGlue() );
        return b;
    }
}
