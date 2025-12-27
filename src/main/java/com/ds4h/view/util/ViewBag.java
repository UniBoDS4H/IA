package com.ds4h.view.util;

import com.ds4h.model.image.imagePoints.ImagePoints;
import com.ds4h.view.pointSelectorGUI.PointSelectorCanvas;

import java.util.HashMap;
import java.util.Map;

public class ViewBag {
    public static Map<ImagePoints, PointSelectorCanvas> references = new HashMap<>();
    private ViewBag() {}
}
