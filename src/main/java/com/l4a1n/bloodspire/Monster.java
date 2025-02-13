package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.List;

public class Monster {
    private Circle shape;
    private static final double SPEED = 1.0;        // Geschwindigkeit des Monsters
    private static final double AVOID_DISTANCE = 40;        // Abstand der zu andern Entitäten eingehalten werden soll

    public Monster(double x, double y) {
        shape = new Circle(20, Color.RED);
        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    // Getters
    public Circle getShape() {return shape;}        // Gibt das Objekt Circle zurück
    public double getX(){return shape.getCenterX();}                // return X Koordinate proportional von der Mitte der Figur
    public double getY(){return shape.getCenterY();}                // return Y Koordinate proportional von der Mitte der Figur

    // Positions- und Collisions Update-Methode
    public void moveTowards(double targetX, double targetY, List<Monster> monsters, List<Wall> walls, double dTime) {
        double dx = targetX - shape.getCenterX();   // X Differenz zu Target
        double dy = targetY - shape.getCenterY();   // Y Differenz zu Target
        double distance = Math.sqrt(dx * dx + dy * dy);     // Rechnet die Distanz zu Target aus

        if (distance < AVOID_DISTANCE) return;      // Bricht ab hier ab, wenn die Distanz zu Target kleiner als AVOID_DISTANCE ist

        double moveX = (dx / distance) * SPEED;     // Bestimmt wie viele Pixel auf X das Monster hinter sich legen soll
        double moveY = dy / distance * SPEED;       // Bestimmt wie viele Pixel auf Y das Monster hinter sich legen soll

        for (Monster other : monsters){
            if (other == this) continue;    // Wenn es sich um sich selber handelt, dann soll die nächste Iteration der Schleife fortgesetzt werden (Damit man keine Kollision mit sich selber überprüft)

            double diffX = getX() - other.getX();       // X Differenz zu andern Monstern
            double diffY = getY() - other.getY();       // Y Differenz zu andern Monstern
            double otherDistance = Math.sqrt(diffX * diffX + diffY * diffY);    // Rechnet die Distanz zu anderen Monstern aus

            if (otherDistance < AVOID_DISTANCE && otherDistance > 0){
                moveX += diffX / otherDistance * (SPEED * dTime) * 0.5;
                moveY += diffY / otherDistance * (SPEED * dTime) * 0.5;
            }
        }

        boolean collideX = false, collideY = false;     // Kollisions Attribute
        for (Wall wall : walls){
            if (wall.collidesWith(getX() + moveX, getY(), 20)) collideX = true;     // Kollision auf X dann True
            if (wall.collidesWith(getX(), getY() + moveY, 20)) collideY = true;     // Kollision auf Y dann True
        }
        if (!collideX) shape.setCenterX(getX() + moveX);    // Wenn auf X keine Kollision dann bewegen auf X möglich
        if (!collideY) shape.setCenterY(getY() + moveY);    // Wenn auf Y keine Kollision dann bewegen auf Y möglich
    }

}