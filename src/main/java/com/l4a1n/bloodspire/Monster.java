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

    public void moveTowards(double targetX, double targetY, List<Monster> monsters) {
        double dx = targetX - shape.getCenterX();
        double dy = targetY - shape.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        
        if (distance > 30) {    // Der Part sorgt dafür das die Monster nicht auf dem Player sitzen, sondern den "Personal Space" des Players berücksichtigen
            double moveX = dx / distance * SPEED;
            double moveY = dy / distance * SPEED;

            for (Monster other : monsters){
                if (other == this) continue;

                double diffX = getX() - other.getX();
                double diffY = getY() - other.getY();
                double otherDistance = Math.sqrt(diffX * diffX + diffY * diffY);

                if (otherDistance < AVOID_DISTANCE){
                    moveX += diffX / otherDistance * 0.5;
                    moveY += diffY / otherDistance * 0.5;
                }
            }
            shape.setCenterX(getX() + moveX);
            shape.setCenterY(getY() + moveY);
        }
    }
}