import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MatchingShapesGame extends JPanel {
    private List<Shape> shapes;
    private List<Basket> baskets;
    private Shape selectedShape;
    private Point mouseOffset;

    public MatchingShapesGame() {
        shapes = new ArrayList<Shape>();
        baskets = new ArrayList<Basket>();
        baskets.add(new Basket(Color.RED, new Rectangle(50, 50, 100, 100)));
        baskets.add(new Basket(Color.GREEN, new Rectangle(200, 50, 100, 100)));
        baskets.add(new Basket(Color.BLUE, new Rectangle(350, 50, 100, 100)));
        shapes.add(new Shape(Color.RED, new Rectangle(100, 200, 50, 50)));
        shapes.add(new Shape(Color.GREEN, new Rectangle(200, 200, 50, 50)));
        shapes.add(new Shape(Color.BLUE, new Rectangle(300, 200, 50, 50)));
        Collections.shuffle(shapes); // randomize shape order
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                selectedShape = null;
                for (Shape shape : shapes) {
                    if (shape.getBounds().contains(e.getPoint())) {
                        selectedShape = shape;
                        mouseOffset = new Point(e.getX() - shape.getBounds().x, e.getY() - shape.getBounds().y);
                        break;
                    }
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selectedShape != null) {
                    selectedShape.setBounds(e.getX() - mouseOffset.x, e.getY() - mouseOffset.y, selectedShape.getBounds().width, selectedShape.getBounds().height);
                    repaint();
                }
            }
        });
    }

    private class Basket {
        private Color color;
        private Rectangle bounds;
    
        public Basket(Color color, Rectangle bounds) {
            this.color = color;
            this.bounds = bounds;
        }
    
        public Color getColor() {
            return color;
        }
    
        public Rectangle getBounds() {
            return bounds;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Basket basket : baskets) {
            g.setColor(basket.getColor());
            g.fillRect(basket.getBounds().x, basket.getBounds().y, basket.getBounds().width, basket.getBounds().height);
        }
        for (Shape shape : shapes) {
            g.setColor(shape.getColor());
            g.fillRect(shape.getBounds().x, shape.getBounds().y, shape.getBounds().width, shape.getBounds().height);
        }
        if (selectedShape != null) {
            g.setColor(selectedShape.getColor());
            g.fillRect(selectedShape.getBounds().x, selectedShape.getBounds().y, selectedShape.getBounds().width, selectedShape.getBounds().height);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Matching Shapes to Baskets Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.add(new MatchingShapesGame());
        frame.setVisible(true);
    }

    private class Shape {
        private Color color;
        private Rectangle bounds;

        public Shape(Color color, Rectangle bounds) {
            this.color = color;
            this.bounds = bounds;
        }

        public Color getColor() {
            return color;
        }

        public Rectangle getBounds() {
            return bounds;
        }

        public void setBounds(int x, int y, int width, int height) {
            bounds.setBounds(x, y, width, height);
            for (Basket basket : baskets) {
                if (basket.getBounds().contains(bounds)) {
                    if (basket.getColor().equals(color)) {
                        // Shape is in the correct basket
                        // Remove the shape and create a new one in a new location
                        shapes.remove(this);
                        if (!shapes.isEmpty()) {
                        shapes.add(new Shape(color, getRandomShapeBounds()));
                        }
                        selectedShape = null;
                        repaint();
                        System.out.println("Shape matched to basket!");
                        return;
                        } else {
                        // Shape is in the wrong basket, reset its position
                        bounds.setLocation(getRandomShapeBounds().getLocation());
                        selectedShape = null;
                        repaint();
                        System.out.println("Shape not matched to correct basket.");
                        return;
                        }
                        }
                        // Shape is not in any basket, reset its position
                        bounds.setLocation(getRandomShapeBounds().getLocation());
                        selectedShape = null;
                        repaint();
                        System.out.println("Shape not matched to any basket.");
                        }
        }   
    }

    private Rectangle getRandomShapeBounds() {
        int x = (int) (Math.random() * (getWidth() - 50));
        int y = (int) (Math.random() * (getHeight() - 50)) + 100;
        return new Rectangle(x, y, 50, 50);
    }
}



