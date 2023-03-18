package com.ds4h.view.mainGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.view.cornerSelectorGUI.CornerSelectorMenuGUI;
import com.ds4h.view.displayInfo.DisplayInfo;
import ij.gui.ImageWindow;
import ij.gui.Toolbar;
import ij.plugin.tool.PlugInTool;

import java.awt.*;

public class PointSelectorGUI extends ImageWindow {
    private final CornerSelectorMenuGUI menu;
    private final ImagePoints image;
    private final PointController pointController;

    public PointSelectorGUI(ImagePoints image, final PointController controller) {
        super(image, new PointSelectorCanvas(image));
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
        this.add(this.menu);

        Dimension s = DisplayInfo.getScaledImageDimension(new Dimension(this.image.getWidth(), this.image.getHeight()), DisplayInfo.getDisplaySize(80));
        this.setSize((int)s.getWidth(), (int)s.getHeight());

        this.setMinimumSize(s);
        this.setMaximumSize(s);
        this.pack();
    }

}
