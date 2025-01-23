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

    public void moveTowards(double targetX, double targetY) {
        double dx = targetX - shape.getCenterX();
        double dy = targetY - shape.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 1) {
            shape.setCenterX(shape.getCenterX() + dx / distance);
            shape.setCenterY(shape.getCenterY() + dy / distance);
        }
    }
}