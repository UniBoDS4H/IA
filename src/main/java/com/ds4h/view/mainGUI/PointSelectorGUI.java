package com.ds4h.view.mainGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.view.cornerSelectorGUI.CornerSelectorMenuGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Toolbar;
import ij.plugin.tool.PlugInTool;

import java.awt.*;
import java.awt.event.*;

public class PointSelectorGUI extends ImageWindow implements WindowListener {
    private final CornerSelectorMenuGUI menu;
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
        this.menu = new CornerSelectorMenuGUI(this.pointController, this.image, this);
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


}


