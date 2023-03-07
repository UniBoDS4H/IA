package com.ds4h.view.cornerSelectorGUI;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.CoordinateConverter;
import com.ds4h.view.displayInfo.DisplayInfo;
import org.opencv.core.Point;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class CornerSelectorPanelGUI extends JPanel implements MouseWheelListener {
    private ImagePoints currentImage;
    private Point referencePoint;
    private final CornerSelectorGUI container;
    private final int POINT_DIAMETER = 6;
    private Color pointerColor;
    private Color selectedPointerColor;
    private Color textColor;
    private int pointerDimension;

    private double zoomFactor;
    private double prevZoomFactor;
    private boolean zoomer;
    private boolean dragger;
    private boolean released;
    private double xOffset;
    private double yOffset;
    private int xDiff;
    private int yDiff;
    private java.awt.Point startPoint;

    public CornerSelectorPanelGUI(CornerSelectorGUI container) {
        this.container = container;
        this.setDefaultPointerStyles();
        this.addMouseWheelListener(this);
        this.setFocusable(true);
        this.initZoom();
        setOpaque(true);
        initComponent();

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(referencePoint != null) {
                    Point newPoint = getMatIndexFromPoint(getScaledPoint(e));
                    moveAllSelected(referencePoint, newPoint);
                    referencePoint = newPoint;
                    repaint();
                }else{
                    java.awt.Point curPoint = e.getLocationOnScreen();
                    xDiff = curPoint.x - startPoint.x;
                    yDiff = curPoint.y - startPoint.y;

                    dragger = true;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = getMatIndexFromPoint(getScaledPoint(e));
                if(imageContains(point)){//point already present in the image
                    Point actualPoint = getActualPoint(point); //getting the exact pressed point
                    if(!e.isControlDown()){
                        container.clearSelectedPoints();
                        container.addSelectedPoint(actualPoint);
                    }
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                    Point point = getMatIndexFromPoint(getScaledPoint(e));
                    if(imageContains(point)){//point already present in the image
                        Point actualPoint = getActualPoint(point); //getting the exact pressed point
                        referencePoint = actualPoint;
                        if(e.isControlDown()) {
                            if (container.getSelectedPoints().contains(actualPoint)) {//if the point is already selected
                                container.removeSelectedPoint(actualPoint);
                            } else {
                                container.addSelectedPoint(actualPoint);
                            }
                        }else {

                            if (!container.getSelectedPoints().contains(actualPoint)) {
                                container.clearSelectedPoints();
                                container.addSelectedPoint(actualPoint);
                            }
                        }
                    }else{
                        //ADD new point
                        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                            //first i add the new point then i call the updatePointsForAlignment for checking if we can enable the manual alignment button
                            currentImage.addPoint(getMatIndexFromPoint(getScaledPoint(e)));
                            container.updatePointsForAlignment();
                            repaint();
                        }else{
                            //if I single click a place where there is no point I clear the selection
                            container.clearSelectedPoints();
                            repaint();
                            released = false;
                            startPoint = MouseInfo.getPointerInfo().getLocation();
                        }
                    }
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                referencePoint = null;
                released = true;
                if(dragger){
                    repaint();
                }
                super.mouseReleased(mouseEvent);
            }
        });
    }

    public void initZoom() {
        this.zoomFactor = 1;
        this.prevZoomFactor = 1;
        this.xOffset = 0;
        this.yOffset = 0;
    }

    private Point getScaledPoint(MouseEvent e){
        return new Point((-xOffset/zoomFactor)+(e.getX()/zoomFactor), (-yOffset/zoomFactor)+(e.getY()/zoomFactor));
    }
    private void initComponent() {
        addMouseWheelListener(this);
    }

    private void setDefaultPointerStyles() {
        this.textColor = Color.YELLOW;
        this.pointerColor = Color.RED;
        this.selectedPointerColor = Color.YELLOW;
        this.pointerDimension = 5;
    }

    private void moveAllSelected(Point oldPoint, Point newPoint){
        int xGap = (int)(newPoint.x-oldPoint.x);
        int yGap = (int)(newPoint.y-oldPoint.y);
        this.container.getSelectedPoints().forEach(p-> this.currentImage.movePoint(p, new Point(p.x+xGap,p.y+yGap)));
        this.container.setSelectedPoints(this.container.getSelectedPoints().stream().map(p-> new Point(p.x+xGap,p.y+yGap)).collect(Collectors.toList()));
    }
    private Point getMatIndexFromPoint(Point p){
        return CoordinateConverter.getMatIndexFromPoint(p, currentImage.getMatImage().rows(), currentImage.getMatImage().cols(), getWidth(), getHeight());
    }

    private Point getPointFromMatIndex(Point p){
        return CoordinateConverter.getPointFromMatIndex(p,this.currentImage.getMatImage().rows(), this.currentImage.getMatImage().cols(), this.getWidth(), this.getHeight());
    }

    private boolean imageContains(Point point){
        return Arrays.stream(this.currentImage.getPoints())
                .anyMatch(p-> p.x < point.x+this.pointerDimension*3 && p.x > point.x-this.pointerDimension*3 && p.y < point.y+this.pointerDimension*3 && p.y > point.y-this.pointerDimension*3);
    }

    /**
     * Gets the right pressed corner considering some gap of selection
     * @param selected : selected corner (matrix index)
     * @return the corresponding actual point (matrix index)
     */
    private Point getActualPoint(Point selected){
       Optional<Point> point = Arrays.stream(this.currentImage.getPoints()).filter(p -> p.x < selected.x + this.pointerDimension*3 && p.x > selected.x - this.pointerDimension*3 && p.y < selected.y + this.pointerDimension*3 && p.y > selected.y - this.pointerDimension*3).findFirst();
        if(point.isPresent()) {
            return point.get();
        }else{
            throw new IllegalArgumentException("selected point was not among the image ones");
        }
    }

    public void setCurrentImage(ImagePoints image){
        this.currentImage = image;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        this.removeAll();
        Graphics2D g2d = (Graphics2D) g;
        if(this.currentImage != null){
            super.paintComponent(g);
            if(!dragger && !zoomer){
                AffineTransform at = new AffineTransform();
                at.translate(xOffset, yOffset);
                at.scale(zoomFactor, zoomFactor);
                g2d.transform(at);
                this.container.updateMenu();
            }
            if(zoomer){
                AffineTransform at = new AffineTransform();
                double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
                double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();
                double zoomDiv = zoomFactor / prevZoomFactor;
                xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
                yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;
                if(xOffset > 0){
                    xOffset = 0;
                }else if(xOffset < -this.currentImage.getBufferedImage().getWidth()){
                    xOffset = -this.currentImage.getBufferedImage().getWidth();
                }
                if(yOffset > 0){
                    yOffset = 0;
                }else if(yOffset < -this.currentImage.getBufferedImage().getHeight()){
                    yOffset = -this.currentImage.getBufferedImage().getHeight();
                }
                at.translate(xOffset, yOffset);
                at.scale(zoomFactor, zoomFactor);
                prevZoomFactor = zoomFactor;
                g2d.transform(at);
                zoomer = false;
            }
            if (dragger) {
                AffineTransform at = new AffineTransform();
                double xMove = xOffset + xDiff >0? 0: xOffset + xDiff;
                double yMove = yOffset + yDiff >0? 0: yOffset + yDiff;
                xMove = Math.max(xMove, -this.getWidth() * zoomFactor + this.getWidth());
                yMove = Math.max(yMove, -this.getHeight() * zoomFactor + this.getHeight());
                at.translate(xMove, yMove);
                at.scale(zoomFactor, zoomFactor);
                g2d.transform(at);
                if (released) {
                    xOffset = xMove;
                    yOffset = yMove;
                    dragger = false;
                }
            }
            g2d.drawImage(this.currentImage.getBufferedImage(),0,0, this.getWidth(),this.getHeight(),null);
        }
        this.drawPoints(g2d);
        this.container.updateSettings();
        g2d.dispose();
        System.gc();
    }

    /**
     * Draws all the corners with their index in the right color and dimension
     * @param g2d the drawer
     */
    private void drawPoints(Graphics2D g2d) {
        for (Point p : this.currentImage.getPoints()) {
            //point.getValue() -> is the matrix index of the point.
            //point.getKey() -> is the position of the point to show
            AbstractMap.SimpleEntry<Point, Point> point = new AbstractMap.SimpleEntry<>(this.getPointFromMatIndex(p), p);
            System.out.println(DisplayInfo.getTextSize(5));
            Font f = new Font("Serif", Font.BOLD, DisplayInfo.getTextSize(5));
            g2d.setColor(this.textColor);
            g2d.setFont(f);
            int textX = (int) point.getKey().x - this.pointerDimension * 3 - 12;
            int textY = (int) point.getKey().y + this.pointerDimension * 3 + 12;
            g2d.drawString(Integer.toString(this.currentImage.getIndexOfPoint(point.getValue())), textX, textY);
            //if the corner I'm printing it's not selected I use the not selected color
            g2d.setColor(this.container.getSelectedPoints().contains(point.getValue()) ? this.selectedPointerColor : this.pointerColor);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval((int) point.getKey().x - this.pointerDimension * 3, (int) point.getKey().y - this.pointerDimension * 3, this.pointerDimension * 6, this.pointerDimension * 6);
            g2d.fillOval((int) point.getKey().x - 3, (int) point.getKey().y - 3, POINT_DIAMETER, POINT_DIAMETER);
        }
    }

    /**
     * Listener for the mouse wheel in order to zoom in and out
     * @param e Event listener
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.isControlDown()){
            zoomer = true;
            //Zoom in
            if (e.getWheelRotation() < 0) {
                zoomFactor = zoomFactor <3? zoomFactor*1.1: 3;
                repaint();
            }else{ //Zoom out
                if(zoomFactor/1.1 >=1){
                    zoomFactor /= 1.1;
                }else{
                    zoomFactor = 1;
                    xOffset = 0;
                    yOffset = 0;
                }
                repaint();
            }
        }
    }

    public void setPointerColor(Color selectedColor) {
        this.pointerColor = selectedColor;
        this.repaint();
    }

    public void setSelectedPointerColor(Color selectedColor) {
        this.selectedPointerColor = selectedColor;
        this.repaint();
    }

    public void setTextColor(Color selectedColor) {
        this.textColor = selectedColor;
        this.repaint();
    }
    public void setPointerDimension(int dimension){
        this.pointerDimension = dimension;
        this.repaint();
    }

    public Color getPointerColor() {
        return this.pointerColor;
    }

    public Color getSelectedPointerColor() {
        return this.selectedPointerColor;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public int getPointerDimension(){
        return this.pointerDimension;
    }
}
