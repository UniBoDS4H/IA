package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.CoordinateConverter;
import org.opencv.core.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;

public class CornerSelectorPanelGUI extends JPanel implements MouseWheelListener{
    private ImageCorners currentImage;
    private Point referencePoint;
    private int zoomLevel = 100;
    private final CornerSelectorGUI container;
    private final int POINT_DIAMETER = 6;
    private final int RADIUS = 15;
    public CornerSelectorPanelGUI(CornerSelectorGUI container) {
        this.container = container;
        addMouseWheelListener(this);
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_DELETE || keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    container.getSelectedPoints().forEach(s->currentImage.removeCorner(s));
                    container.clearSelectedPoints();
                    repaint();
                }
            }
        });
        setFocusable(true);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(referencePoint != null) {
                    Point newPoint = getMatIndexFromPoint(new Point(e.getX(), e.getY()));
                    //currentImage.moveCorner(referencePoint, newPoint);
                    moveAllSelected(referencePoint, newPoint);
                    referencePoint = newPoint;
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = getMatIndexFromPoint(new Point(e.getX(),e.getY()));

                if(imageContains(point)){//point already present in the image
                    Point actualPoint = getActualPoint(point); //getting the exact pressed point
                    if(!e.isControlDown()){
                        container.clearSelectedPoints();
                        container.addSelectedPoint(actualPoint);
                    }
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Point point = getMatIndexFromPoint(new Point(e.getX(),e.getY()));

                if(imageContains(point)){//point already present in the image
                    Point actualPoint = getActualPoint(point); //getting the exact pressed point
                    referencePoint = actualPoint;

                    if(e.isControlDown()){
                        if(container.getSelectedPoints().contains(actualPoint)){//if the point is already selected
                            container.removeSelectedPoint(actualPoint);
                        }else{
                            container.addSelectedPoint(actualPoint);
                        }
                    }else{
                        if(!container.getSelectedPoints().contains(actualPoint)){
                            container.clearSelectedPoints();
                            container.addSelectedPoint(actualPoint);
                        }
                    }
                }else{
                    //TODO: you are using the JAVA.AWT point, is this correct ? Because we are working on opencv point.
                    //TODO: fix the MVC
                    currentImage.addCorner(getMatIndexFromPoint(new Point(e.getX(), e.getY())));
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                referencePoint = null;
                super.mouseReleased(mouseEvent);
            }
        });

    }
    private void moveAllSelected(Point oldPoint, Point newPoint){
        int xGap = (int)(newPoint.x-oldPoint.x);
        int yGap = (int)(newPoint.y-oldPoint.y);
        this.container.getSelectedPoints().forEach(p->{
            this.currentImage.moveCorner(p, new Point(p.x+xGap,p.y+yGap));
        });
        this.container.setSelectedPoints(this.container.getSelectedPoints().stream().map(p-> new Point(p.x+xGap,p.y+yGap)).collect(Collectors.toList()));
    }
    private Point getMatIndexFromPoint(Point p){
        return CoordinateConverter.getMatIndexFromPoint(p, currentImage.getMatImage().rows(), currentImage.getMatImage().cols(), getWidth(), getHeight());
    }

    private Point getPointFromMatIndex(Point p){
        return CoordinateConverter.getPointFromMatIndex(p,this.currentImage.getMatImage().rows(), this.currentImage.getMatImage().cols(), this.getWidth(), this.getHeight());
    }
    private boolean imageContains(Point point){
        return Arrays.stream(this.currentImage.getCorners())
                .anyMatch(p-> p.x < point.x+RADIUS && p.x > point.x-RADIUS && p.y < point.y+RADIUS && p.y > point.y-RADIUS);
    }

    /**
     * Gets the right pressed corner considering some gap of selection
     * @param selected : selected corner (matrix index)
     * @return the corresponding actual point (matrix index)
     */
    private Point getActualPoint(Point selected){
       Optional<Point> point = Arrays.stream(this.currentImage.getCorners()).filter(p -> p.x < selected.x + RADIUS && p.x > selected.x - RADIUS && p.y < selected.y + RADIUS && p.y > selected.y - RADIUS).findFirst();
        if(point.isPresent()) {
            return point.get();
        }else{
            throw new IllegalArgumentException("selected point was not among the image ones");
        }
    }

    public void setCurrentImage(ImageCorners image){
        this.currentImage = image;
        repaint();
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
                System.out.println("in");
                zoomIn();
            } else {

                System.out.println("out");
                zoomOut();
            }
        }
    }

    public void zoomIn() {
        this.zoomLevel += 10;
        setPreferredSize(new Dimension((int) (getPreferredSize().width * 1.1), (int) (getPreferredSize().height * 1.1)));
        revalidate();
        repaint();
    }

    public void zoomOut() {
        this.zoomLevel -= 10;
        setPreferredSize(new Dimension((int) (getPreferredSize().width / 1.1), (int) (getPreferredSize().height / 1.1)));
        revalidate();
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(this.currentImage != null){
            super.paintComponent(g);
            g2d.scale(zoomLevel / 100.0, zoomLevel / 100.0);
            g2d.drawImage(this.currentImage.getBufferedImage(),0,0,this.getWidth(),this.getHeight(),this);
        }

        Arrays.stream(this.currentImage.getCorners())
                .map(p-> new AbstractMap.SimpleEntry<Point,Point>(this.getPointFromMatIndex(p),p))
                .forEach(point->{ //point.getValue() -> is the matrix index of the point.      point.getKey() -> is the position of the point to show
                    g2d.setColor(Color.YELLOW);
                    g2d.setFont(new Font("Serif", Font.BOLD, 16));
                    g2d.drawString(Integer.toString(this.currentImage.getIndexOfCorner(point.getValue())),(int)point.getKey().x - 25, (int)point.getKey().y+25);

                    if(!this.container.getSelectedPoints().contains(point.getValue())){
                        g2d.setColor(Color.RED);
                    }else{
                        g2d.setColor(Color.YELLOW);
                    }
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawOval((int)point.getKey().x - 15, (int)point.getKey().y - 15, 30, 30);
                    g2d.fillOval((int)point.getKey().x - 3, (int)point.getKey().y - 3, POINT_DIAMETER, POINT_DIAMETER);
                });

    }

}
