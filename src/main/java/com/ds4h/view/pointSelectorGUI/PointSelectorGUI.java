package com.ds4h.view.pointSelectorGUI;

import com.ds4h.controller.directoryManager.DirectoryManager;
import com.ds4h.controller.opencvController.OpencvController;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Toolbar;
import ij.plugin.tool.PlugInTool;
import org.opencv.core.Point;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PointSelectorGUI extends ImageWindow implements WindowListener {
    private final PointSelectorMenuGUI menu;
    private final ImagePoints image;
    private final PointController pointController;
    private final PointSelectorCanvas canvas;
    private final Panel panel;

    public PointSelectorGUI(ImagePoints image, final PointController controller) {
        super(image, new PointSelectorCanvas(image));
        this.panel = new Panel();
        this.setLayout(new BorderLayout());
        this.panel.setLayout(new BorderLayout());
        this.canvas = (PointSelectorCanvas) this.getCanvas();
        this.removeAll();
        Toolbar.addPlugInTool(new PlugInTool() {
            @Override
            public String getToolName() {
                return "Pointer";
            }

            @Override
            public String getToolIcon() {
                return getClass().getResource("/icons/settings.png").getPath();
            }
        });
        this.image = image;
        this.pointController = controller;
        this.menu = new PointSelectorMenuGUI(this.pointController, this.image, this);
        this.panel.add(this.canvas,BorderLayout.CENTER);
        this.panel.add(menu, BorderLayout.PAGE_END);
        this.add(this.panel, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                checkWindowSize();
            }

            private void checkWindowSize() {
                setPreferredSize(getSize());
            }
        });

        this.pack();
    }
    public List<Point> getSelectedPoints() {
        return this.canvas.getSelectedPoints();
    }

    public void clearSelectedPoints() {
        this.canvas.getSelectedPoints().clear();
    }

    public void repaintPoints() {
        this.canvas.drawPoints();
    }
    public ImagePoints getImage(){
        return this.image;
    }

    @Override
    public PointSelectorCanvas getCanvas() {
        return (PointSelectorCanvas) super.getCanvas();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.setVisible(false);
    }
    public void showWindow(){
        this.setVisible(true);
    }
}


