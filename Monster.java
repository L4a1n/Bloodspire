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

    public Circle getShape() {
        return shape;
    }

    public double returnX(){
        return shape.getCenterX();
    }
    public double returnY(){
        return shape.getCenterY();
    }

    public void moveTowards(double targetX, double targetY, List<Monster> monsters) {
        // dx -> delta X ist Spieler-X-Position - eigene Mitte ...
        double dx = targetX - shape.getCenterX();
        double dy = targetY - shape.getCenterY();
        // distance -> Wurzel aus DeltaX quadrat + DeltaY quadrat
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Der Part sorgt dafür das die Monster nicht auf dem Player sitzen sondern den "Personal Space" des Players berücksichtigen
        if (distance > 30) {
            double moveX = dx / distance * SPEED;
            double moveY = dy / distance * SPEED;
            
            for (Monster other : monsters) {
                if (other == this) continue;
                System.out.println("YES"+ this);                
        
                double diffX = shape.getCenterX() - other.shape.getCenterX();
                double diffY = shape.getCenterY() - other.shape.getCenterY();
                double otherDistance = Math.sqrt(diffX * diffX + diffY * diffY);
        
                if (otherDistance < AVOID_DISTANCE) {
                  moveX += diffX / otherDistance * 0.5;
                  System.out.println(moveX);
                  moveY += diffY / otherDistance * 0.5;
                } // end of if                       
        
            } // end of for
            
            shape.setCenterX(shape.getCenterX() + moveX);
            shape.setCenterY(shape.getCenterY() + moveY);
        }
    }
}