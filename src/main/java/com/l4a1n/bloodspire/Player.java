package com.l4a1n.bloodspire;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class Player {
    private Circle shape;
    private double targetX;
    private double targetY;
    private int SPEED = 150;
    private int health = 1000;
    private int radius = 10;
    private int damage = 25;
    private double knockback = -2;
    private double distance;
    private int currentAbility;
    private int level = 1;
    private double globalCooldown = 1;
    private int baseDamage = 5;

    public Player(double x, double y) {
        shape = new Circle(radius, Color.BLUE);
        shape.setCenterX(x);
        shape.setCenterY(y);
        targetX = x;
        targetY = y;
        currentAbility = 0;
    }

    // The "David" Getters
    public Circle getShape() {return shape;}                    // Gibt die ganze Figur zurück. Nützlich für Kollision oder ähnliches.
    public double getX() {return shape.getCenterX();}           // return X Koordinate proportional von der Mitte der Figur.
    public double getY() {return shape.getCenterY();}           // return Y Koordinate proportional von der Mitte der Figur.
    public int getHealth(){return health;}                      // return aktuelles Leben des Spielers
    public int getDamage(){return damage;}                      // return Schaden den der Spieler macht
    public void decHealth(int damage){health -= damage;}        // verringert das Leben des Spielers um angegebenen Schaden
    public double getKnockback(){return knockback;}             // return Rückstoß den der Spieler mit Attacken verursacht
    public void heal(int amount){health += amount;}
    public int getCurrentAbility(){return currentAbility;}
    public void setCurrentAbility(int i){currentAbility = i;}
    public int getLevel(){return level;}
    public void increaseLevel(){
        level++;
        baseDamage += 5;
        if (globalCooldown > 0.2){
            globalCooldown -= 0.1;
        }
    }
    public double getCooldown(){return globalCooldown;}

    public void setDamage(int damage){this.damage = damage + baseDamage;
        System.out.println(this.damage);
    }   // Legt den Schaden fest, den der Spieler verursacht

    // Der Target setzer. Er macht was er sagt, er setzt das neue Ziel das der Spieler verfolgen soll.
    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
    }

    double[] rotateVector(double x, double y, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new double[]{
                x * cos - y * sin,  // Rotated X
                x * sin + y * cos   // Rotated Y
        };
    }

    public void fireSalve(List<Projectile> projectiles, Pane gamePane, double x, double y, double targetX, double targetY){
        double angleOffset = Math.toRadians(10);

        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double dirX = dx / distance;
        double dirY = dy / distance;

        Projectile p1 = new Projectile(x, y, targetX, targetY, 0, 0, getDamage());                                                  // Mittleres Projektil

        double[] leftDir = rotateVector(dirX, dirY, -angleOffset);
        Projectile p2 = new Projectile(x, y, x + leftDir[0] * distance, y + leftDir[1] * distance, 0, 0, damage);   // Linkes Projektil

        double[] rightDir = rotateVector(dirX, dirY, angleOffset);
        Projectile p3 = new Projectile(x, y, x + rightDir[0] * distance, y + rightDir[1] * distance, 0, 0, damage); // Rechtes Projektil

        projectiles.add(p1);
        projectiles.add(p2);
        projectiles.add(p3);
        gamePane.getChildren().addAll(p1.getShape(), p2.getShape(), p3.getShape());
    }

    public void sprint(long time){
        // NYA
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