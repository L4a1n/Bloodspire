package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class Player {
    private Circle shape;
    private double targetX;
    private double targetY;
    private int SPEED = 3;
    private int maxHealth = 100;    // ist noch unbenutzt
    private int radius = 20;

    public Player(double x, double y) {
        shape = new Circle(radius, Color.BLUE);
        shape.setCenterX(x);
        shape.setCenterY(y);
        targetX = x;
        targetY = y;
    }

    // The "David" Getters
    public Circle getShape() {return shape;}                    // Gibt die ganze Figur zurück. Nützlich für Kollision oder ähnliches.
    public double getX() {return shape.getCenterX();}           // return X Koordinate proportional von der Mitte der Figur.
    public double getY() {return shape.getCenterY();}           // return Y Koordinate proportional von der Mitte der Figur.

    // Der Target setzer. Er macht was er sagt, er setzt das neue Ziel das der Spieler verfolgen soll.
    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
    }

    public void update(double dTime, List<Wall> walls) {
        double dx = targetX - getX();
        double dy = targetY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        boolean collideX = false, collideY = false;
        for (Wall wall : walls){
            if (wall.collidesWith(getX() + dx, getY(), radius)) collideX = true;
            if (wall.collidesWith(getX(), getY() + dy, radius)) collideY = true;
        }
        if (distance > 4) {         // Setzt voraus das die Distanz zum Ziel mindestens größer als 4 sein muss damit der Spieler sich bewegt.
            if (!collideX) shape.setCenterX(getX() + dx / distance * SPEED * dTime);       // Verändert die X Position relativ zum Ziel hin.
            if (!collideY) shape.setCenterY(getY() + dy / distance * SPEED * dTime);       // Verändert die Y Position relativ zum Ziel hin.
        }
    }
}