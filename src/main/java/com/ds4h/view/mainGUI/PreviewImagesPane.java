package com.ds4h.view.mainGUI;

import com.ds4h.controller.pointController.PointController;
import ij.IJ;
import javax.swing.*;
import java.awt.*;

public class PreviewImagesPane extends JPanel {
    private final PointController controller;
    private final JScrollPane scrollPane;
    private final MainMenuGUI mainGUI;
    JPanel innerPanel;
    public PreviewImagesPane(final PointController controller, MainMenuGUI mainGUI){
        this.mainGUI = mainGUI;
        this.controller = controller;
        this.scrollPane = new JScrollPane();
        this.innerPanel = new JPanel();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setVisible(true);
    }

    public void showPreviewImages(){
        this.removeAll();
        this.revalidate();
        this.innerPanel.removeAll();
        try {
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
            IJ.log("[LOADING IMAGES] Start");
            long start = System.currentTimeMillis();
            this.controller.getCornerImagesImages().forEach(ImageCache::getScaledImage);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.gc();
            IJ.log("[LOADING IMAGES] TIme elapsed" + timeElapsed + "ms");

            this.controller.getCornerImagesImages().forEach(image -> {
                final PreviewListItem panel = new PreviewListItem(controller, image, this, this.controller.getCornerImagesImages().indexOf(image) + 1);
                panel.setPreferredSize(this.getPreferredSize());
                panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.setPreferredSize(new Dimension(0, this.getHeight() / 6)); // Imposta la dimensione preferita del pannello di anteprima
                innerPanel.add(panel);
            });
            scrollPane.setViewportView(innerPanel);
            this.add(scrollPane);
            this.revalidate();
            this.repaint();
        }catch (Exception e){
            throw new RuntimeException("The image(s) loaded are not correct.");
        }
    }

    public void clearPanels(){
        this.removeAll();
        this.repaint();
    }

    public void updateList(){
        this.showPreviewImages();
        this.repaint();
    }

    public MainMenuGUI getMainGUI() {
        return this.mainGUI;
    }
}
