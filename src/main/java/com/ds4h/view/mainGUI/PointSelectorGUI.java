package com.ds4h.view.mainGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.view.cornerSelectorGUI.CornerSelectorMenuGUI;
import ij.gui.ImageWindow;
public class PointSelectorGUI extends ImageWindow {
    private final CornerSelectorMenuGUI menu;
    private final ImagePoints image;
    private final PointController pointController;

    public PointSelectorGUI(ImagePoints image, final PointController controller) {
        super(image, new PointSelectorCanvas(image));
        this.image = image;
        this.pointController = controller;
        this.menu = new CornerSelectorMenuGUI(this.pointController, this.image, this);
    }
}
