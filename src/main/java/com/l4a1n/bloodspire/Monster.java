package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Monster {
    private Circle shape;

    public Monster(double x, double y) {
        shape = new Circle(20, Color.RED);
        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    public Circle getShape() {
        return shape;
    }

    public double returnX(){
        return shape.getCenterX();
    }
    public double returnY(){
        return shape.getCenterY();
    }

    public void moveTowards(double targetX, double targetY) {
        // dx -> delta X ist Spieler-X-Position - eigene Mitte ...
        double dx = targetX - shape.getCenterX();
        double dy = targetY - shape.getCenterY();
        // distance -> Wurzel aus DeltaX quadrat + DeltaY quadrat
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Der Part sorgt dafür das die Monster nicht auf dem Player sitzen sondern den "Personal Space" des Players berücksichtigen
        if (distance > 30) {
            shape.setCenterX(shape.getCenterX() + dx / distance);
            shape.setCenterY(shape.getCenterY() + dy / distance);
        }
    }
}