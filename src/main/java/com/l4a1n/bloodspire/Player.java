package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {
    private Circle shape;
    private double targetX;
    private double targetY;
    private int maxHealth = 100;

    public Player(double x, double y) {
        shape = new Circle(20, Color.BLUE);
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

    public void update() {
        double dx = targetX - getX();
        double dy = targetY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 2) {         // Setzt voraus das die Distanz zum Ziel mindestens größer als 2 sein muss damit der Spieler sich bewegt.
            shape.setCenterX(getX() + dx / distance * 2);       // Verändert die X Position relativ zum Ziel hin.
            shape.setCenterY(getY() + dy / distance * 2);       // Verändert die Y Position relativ zum Ziel hin.
        }
    }
}