import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {
    private Circle shape;
    private double targetX;
    private double targetY;

    public Player(double x, double y) {
        shape = new Circle(20, Color.BLUE);
        shape.setCenterX(x);
        shape.setCenterY(y);
        targetX = x;
        targetY = y;
    }

    public Circle getShape() {
        return shape;
    }

    public double getX() {
        return shape.getCenterX();
    }

    public double getY() {
        return shape.getCenterY();
    }

    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
    }

    public void update() {
        double dx = targetX - shape.getCenterX();
        double dy = targetY - shape.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 2) { // Bewegung nur, wenn das Ziel nicht erreicht ist
            shape.setCenterX(shape.getCenterX() + dx / distance * 2);
            shape.setCenterY(shape.getCenterY() + dy / distance * 2);
        }
    }
}