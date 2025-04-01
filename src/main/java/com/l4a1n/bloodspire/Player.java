package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class Player {
    private Circle shape;
    private double targetX;
    private double targetY;
    private int SPEED = 2;
    private int health = 1000;
    private int radius = 20;
    private int damage = 100;
    private double knockback = -2;
    private double distance;

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
    public double X(){return shape.getCenterX() - radius;}      // return Links oben X Koordinate
    public double Y(){return shape.getCenterY() - radius;}      // return Links oben Y Koordinate
    public int getHealth(){return health;}                      // return aktuelles Leben des Spielers
    public int getDamage(){return damage;}                      // return Schaden den der Spieler macht
    public void decHealth(int damage){health -= damage;}        // verringert das Leben des Spielers um angegebenen Schaden
    public double getKnockback(){return knockback;}             // return Rückstoß den der Spieler mit Attacken verursacht
    public void heal(int amount){health += amount;}

    public void setDamage(int damage){this. damage = damage;}   // Legt den Schaden fest, den der Spieler verursacht

    // Der Target setzer. Er macht was er sagt, er setzt das neue Ziel das der Spieler verfolgen soll.
    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
    }

    public void sprint(long time){

    }

    // In der Update Methode wird die Position des Spielers aktualisiert und Kollisionsabfragen gemacht
    public void update(double dTime, List<Wall> walls) {
        double dx = targetX - getX();                   // Delta X -> Distanzunterschied zwischen dem Ziel und dem Spieler
        double dy = targetY - getY();                   // Delta Y ->       - " -
        distance = Math.sqrt(dx * dx + dy * dy);        // Die ausgerechnete Distanz zum Ziel
        double moveX = 0;                               // Gibt an um wie viele Pixel sich der Spieler auf der X Achse umpositionieren soll
        double moveY = 0;                               // - " -
        if (distance > 4){                              // Wenn die Distanz mindestenz 5 Pixel weit vom Ziel entfernt ist dann ...
            moveX = (dx / distance) * SPEED * dTime;    // Wird moveX ausgerechnet
            moveY = (dy / distance) * SPEED * dTime;    // Wird moveY ausgerechnet
        }

        boolean collideX = false, collideY = false;     // initialisierung und deklarierung der booleans
        for (Wall wall : walls){                        // Checkt für alle Wände ob eine Kollision auf einer der Achsen stattfindet
            if (wall.collidesWith(getX() + moveX, getY(), radius)) collideX = true;
            if (wall.collidesWith(getX(), getY() + moveY, radius)) collideY = true;
        }
        if (!collideX) shape.setCenterX(getX() + moveX);       // Verändert die X Position relativ zum Ziel hin.
        if (!collideY) shape.setCenterY(getY() + moveY);       // Verändert die Y Position relativ zum Ziel hin.
    }
}