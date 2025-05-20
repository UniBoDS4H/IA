package com.ds4h.view.pointSelectorGUI;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.view.mainGUI.MainMenuGUI;
import com.ds4h.view.util.ViewBag;
import ij.gui.ImageWindow;
import ij.gui.Toolbar;
import ij.plugin.tool.PlugInTool;
import org.opencv.core.Point;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Objects;

public class PointSelectorGUI extends ImageWindow implements WindowListener {
    private final PointSelectorMenuGUI menu;
    private final ImagePoints image;
    private final PointSelectorCanvas canvas;
    private MainMenuGUI mainGUI;

    public PointSelectorGUI(ImagePoints image, final PointController controller) {
        super(image, new PointSelectorCanvas(image));
        Panel panel = new Panel();
        this.setLayout(new BorderLayout());
        panel.setLayout(new BorderLayout());
        this.canvas = (PointSelectorCanvas) this.getCanvas();
        this.canvas.setContainer(this);
        ViewBag.references.put(image, this.canvas);
        this.removeAll();
        Toolbar.addPlugInTool(new PlugInTool() {
            @Override
            public String getToolName() {
                return "Pointer";
            }

            @Override
            public String getToolIcon() {
                return Objects.requireNonNull(getClass().getResource("/icons/settings.png")).getPath();
            }
        });
        this.image = image;
        this.menu = new PointSelectorMenuGUI(controller, this.image, this);
        panel.add(this.canvas,BorderLayout.CENTER);
        panel.add(menu, BorderLayout.PAGE_END);
        this.add(panel, BorderLayout.CENTER);
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
        if (this.image.getListPoints().isEmpty()) {
            this.canvas.getOverlay().clear();
            this.canvas.repaint();
            return;
        }
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
        this.menu.disposeSettings();
    }
    public void showWindow(){
        this.setVisible(true);
    }

    public void updatePoints() {
        this.canvas.drawPoints();
    }

    public void updateMenu() {
        this.menu.updateView();
    }
    public void updateSettings(){
        this.menu.updateSettings();
    }
    public void checkPointsForAlignment(){
        assert this.mainGUI != null;
        this.mainGUI.checkPointsForAlignment();
    }
    public void setMainGUI(MainMenuGUI mainGUI){this.mainGUI = mainGUI;}
}


