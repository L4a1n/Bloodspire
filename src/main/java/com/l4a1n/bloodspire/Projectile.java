package com.l4a1n.bloodspire;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class Projectile {
    private Circle shape;
    private int radius = 3;
    private long aliveUntil;
    private List<Monster> targets;
    private double directionX;
    private boolean playerHit;
    private double directionY;
    private static final double SPEED = 1000;
    private int damgage;
    private int source;

    public Projectile(double x, double y, double targetX, double targetY, int source, int kind, int damgage){
        shape = new Circle(radius, Color.ORANGERED);
        shape.setCenterX(x);
        shape.setCenterY(y);
        aliveUntil = 0;
        targets = new ArrayList<>();
        this.source = source;
        this.damgage = damgage;
        playerHit = false;

        // Berechnet beim erstellen des Objekts die Richtung in die es sich bewegen soll
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        directionX = dx / distance;
        directionY = dy / distance;
    }

    public Circle getShape(){return shape;}
    public long getAliveUntil(){return aliveUntil;}
    public double getX(){return shape.getCenterX();}
    public double getY(){return shape.getCenterY();}
    public void setAliveUntil(long time){aliveUntil = time + 2000000000L;} // + 2 Sekunden
    public void setTarget(Monster target){targets.add(target);}
    public boolean getPlayerHit(){return playerHit;}
    public void setPlayerHit(){playerHit = true;}
    public List<Monster> getTargets(){return targets;}
    public int getSource(){return source;}
    public int getDamgage(){return damgage;}

    public void update(double dTime, List<Wall> walls){
        boolean collide = false;
        for (Wall wall : walls){    // Checkt ob es mit irgendwelchen Wänden kollidiert
            if (wall.collidesWith(getX(), getY(), radius)) collide = true;
        }
        if (!collide && getTargets().size() < 2){   // Wenn es nicht mit Wänden kollidiert und die Anzahl der Treffer kleiner wie 2 ist dann bewegt es sich
            shape.setCenterX(getX() + directionX * dTime * SPEED);
            shape.setCenterY(getY() + directionY * dTime * SPEED);
        }
        else{
            setAliveUntil(1);
        }
    }
}
