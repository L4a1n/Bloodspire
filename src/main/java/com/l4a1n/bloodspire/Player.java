package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class Player {
    private Circle shape;
    private double targetX;
    private double targetY;
    private int SPEED = 3;
    private int health = 500;    // ist noch unbenutzt
    private int radius = 20;
    private int damage = 10;
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
    public double X(){return shape.getCenterX() - radius;}
    public double Y(){return shape.getCenterY() - radius;}
    public int getHealth(){return health;}
    public int getDamage(){return damage;}
    public void decHealth(int damage){health -= damage;}

    public void setDamage(int damage){this. damage = damage;}

    // Der Target setzer. Er macht was er sagt, er setzt das neue Ziel das der Spieler verfolgen soll.
    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
    }

    public void update(double dTime, List<Wall> walls) {
        double dx = targetX - getX();
        double dy = targetY - getY();
        distance = Math.sqrt(dx * dx + dy * dy);
        double moveX = 0;
        double moveY = 0;
        if (distance > 4){
            moveX = (dx / distance) * SPEED * dTime;
            moveY = (dy / distance) * SPEED * dTime;
        }

        boolean collideX = false, collideY = false;
        for (Wall wall : walls){
            if (wall.collidesWith(getX() + moveX, getY(), radius)) collideX = true;
            if (wall.collidesWith(getX(), getY() + moveY, radius)) collideY = true;
        }
        if (!collideX) shape.setCenterX(getX() + moveX);       // Verändert die X Position relativ zum Ziel hin.
        if (!collideY) shape.setCenterY(getY() + moveY);       // Verändert die Y Position relativ zum Ziel hin.
    }
}