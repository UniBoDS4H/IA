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
    private Color pointerColor;
    private Color selectedPointerColor;
    private Color textColor;
    private int pointerDimension;
    private Point pointToEdit;

    public CornerSelectorPanelGUI(CornerSelectorGUI container) {
        this.container = container;
        this.setDefaultPointerStyles();
        addMouseWheelListener(this);

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
                pointToEdit = null;
                Point point = getMatIndexFromPoint(new Point(e.getX(),e.getY()));
                if(imageContains(point)){//point already present in the image
                    Point actualPoint = getActualPoint(point); //getting the exact pressed point
                    referencePoint = actualPoint;
                    if(container.getSelectedPoints().contains(actualPoint) && container.getSelectedPoints().size() == 1){
                        pointToEdit = actualPoint;
                    }

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

    private void setDefaultPointerStyles() {
        this.textColor = Color.YELLOW;
        this.pointerColor = Color.RED;
        this.selectedPointerColor = Color.YELLOW;
        this.pointerDimension = 5;
    }

    private void moveAllSelected(Point oldPoint, Point newPoint){
        int xGap = (int)(newPoint.x-oldPoint.x);
        int yGap = (int)(newPoint.y-oldPoint.y);
        this.container.getSelectedPoints().forEach(p-> this.currentImage.moveCorner(p, new Point(p.x+xGap,p.y+yGap)));
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
                .anyMatch(p-> p.x < point.x+this.pointerDimension*3 && p.x > point.x-this.pointerDimension*3 && p.y < point.y+this.pointerDimension*3 && p.y > point.y-this.pointerDimension*3);
    }

    /**
     * Gets the right pressed corner considering some gap of selection
     * @param selected : selected corner (matrix index)
     * @return the corresponding actual point (matrix index)
     */
    private Point getActualPoint(Point selected){
       Optional<Point> point = Arrays.stream(this.currentImage.getCorners()).filter(p -> p.x < selected.x + this.pointerDimension*3 && p.x > selected.x - this.pointerDimension*3 && p.y < selected.y + this.pointerDimension*3 && p.y > selected.y - this.pointerDimension*3).findFirst();
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
                zoomIn();
            } else {
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
        this.removeAll();
        Graphics2D g2d = (Graphics2D) g;
        if(this.currentImage != null){
            super.paintComponent(g);
            g2d.scale(zoomLevel / 100.0, zoomLevel / 100.0);
            g2d.drawImage(this.currentImage.getBufferedImage(),0,0,this.getWidth(),this.getHeight(),this);
        }
        Arrays.stream(this.currentImage.getCorners())
                .map(p-> new AbstractMap.SimpleEntry<>(this.getPointFromMatIndex(p), p))
                .forEach(point->{ //point.getValue() -> is the matrix index of the point.      point.getKey() -> is the position of the point to show
                    Font f = new Font("Serif", Font.BOLD, 16);
                    g2d.setColor(this.textColor);
                    g2d.setFont(f);
                    int textX = (int)point.getKey().x - this.pointerDimension*3-12;
                    int textY = (int)point.getKey().y+this.pointerDimension*3+12;
                    if(this.pointToEdit != null && this.pointToEdit.equals(point.getValue())){
                        JTextField textField = new JTextField();
                        textField.setBounds(textX-5, textY-20, 25, 25); // x, y, width, height
                        textField.setFont(f);
                        textField.requestFocus();
                        textField.setHorizontalAlignment(SwingConstants.CENTER);
                        textField.setText(Integer.toString(this.currentImage.getIndexOfCorner(point.getValue())));
                        this.setLayout(null); // set layout to null to position components manually
                        this.add(textField);
                    }else{
                        g2d.drawString(Integer.toString(this.currentImage.getIndexOfCorner(point.getValue())), textX, textY);
                    }
                    //if the corner I'm printing it's not selected I use the not selected color
                    if(!this.container.getSelectedPoints().contains(point.getValue())){
                        g2d.setColor(this.pointerColor);
                    }else{
                        g2d.setColor(this.selectedPointerColor);
                    }
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawOval((int)point.getKey().x - this.pointerDimension*3, (int)point.getKey().y - this.pointerDimension*3, this.pointerDimension*6, this.pointerDimension*6);
                    g2d.fillOval((int)point.getKey().x - 3, (int)point.getKey().y - 3, POINT_DIAMETER, POINT_DIAMETER);
                });
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
