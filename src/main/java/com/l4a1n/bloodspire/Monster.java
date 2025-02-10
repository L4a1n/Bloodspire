package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.List;

public class Monster {
    private Circle shape;
    private static final double SPEED = 1.0;
    private static final double AVOID_DISTANCE = 40;

    public Monster(double x, double y) {
        shape = new Circle(20, Color.RED);
        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    // The "David" Getters
    public Circle getShape() {return shape;}
    public double getX(){return shape.getCenterX();}                // return X Koordinate proportional von der Mitte der Figur
    public double getY(){return shape.getCenterY();}                // return Y Koordinate proportional von der Mitte der Figur

    // Positions- und Collisions Update-Methode
    public void moveTowards(double targetX, double targetY, List<Monster> monsters, List<Wall> walls, double dTime) {
        double dx = targetX - shape.getCenterX();
        double dy = targetY - shape.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < SPEED) return;
        double moveX = (dx / distance) * SPEED;
        double moveY = dy / distance * SPEED;

        for (Monster other : monsters){
            if (other == this) continue;

            double diffX = getX() - other.getX();
            double diffY = getY() - other.getY();
            double otherDistance = Math.sqrt(diffX * diffX + diffY * diffY);

            if (otherDistance < AVOID_DISTANCE && otherDistance > 0){
                moveX += diffX / otherDistance * (SPEED * dTime) * 0.5;
                moveY += diffY / otherDistance * (SPEED * dTime) * 0.5;
            }
        }

        boolean collideX = false, collideY = false;
        for (Wall wall : walls){
            if (wall.collidesWith(getX() + moveX, getY(), 20)) collideX = true;
            if (wall.collidesWith(getX(), getY() + moveY, 20)) collideY = true;
        }
        if (!collideX) shape.setCenterX(getX() + moveX);
        if (!collideY) shape.setCenterY(getY() + moveY);
    }

}