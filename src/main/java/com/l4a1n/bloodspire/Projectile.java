package com.l4a1n.bloodspire;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class Projectile {
    private Circle shape;
    private Shape sickle;
    private int radius = 5;
    private long aliveUntil;
    private List<Monster> targets;
    private List<Circle> projectiles;
    private double directionX;
    private boolean playerHit;
    private double directionY;
    private  double SPEED = 1000;
    private int damgage;
    private int source;
    private int kind;
    private double x, y;
    private int possibleCollisions;

    public Projectile(double x, double y, double targetX, double targetY, int source, int kind, int damgage){
        projectiles = new ArrayList<>();
        this.kind = kind;
        switch (kind){
            case 0:
                possibleCollisions = 2;
                shape = new Circle(radius, Color.ORANGERED);
                projectiles.add(shape);
                shape.setCenterX(x);
                shape.setCenterY(y);
                aliveUntil = 0;
                targets = new ArrayList<>();
                this.source = source;
                this.damgage = damgage;
                playerHit = false;

                // Berechnet beim Erstellen des Objekts die Richtung in die es sich bewegen soll
                double dx = targetX - x;
                double dy = targetY - y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                directionX = dx / distance;
                directionY = dy / distance;
                break;
            case 1:
                break;
            case 2:
                this.x = x;
                this.y = y;

                possibleCollisions = 4;
                radius = 20;
                SPEED = 700;
                double dx2 = targetX - x;
                double dy2 = targetY - y;
                double distance2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);
                directionX = dx2 / distance2;
                directionY = dy2 / distance2;

                // Create the base and cutout centered at (0, 0)
                Circle base = new Circle(radius);
                Circle cutout = new Circle(radius * 2);
                cutout.setCenterX(-directionX * radius * 1.5);
                cutout.setCenterY(-directionY * radius * 1.5);

                // Subtract to get the sickle
                sickle = Shape.subtract(base, cutout);
                sickle.setFill(Color.LIGHTBLUE);

                // Now move the result to (x, y)
                sickle.setLayoutX(x);
                sickle.setLayoutY(y);

                aliveUntil = 0;
                targets = new ArrayList<>();
                this.source = source;
                this.damgage = damgage;
                playerHit = false;
                break;
        }

    }

    public Circle getShape(){return shape;}
    public Shape getSickle(){return sickle;}
    public long getAliveUntil(){return aliveUntil;}
    public double getX() {return kind == 2 ? x : shape.getCenterX();}
    public double getY() {return kind == 2 ? y : shape.getCenterY();}
    public void setAliveUntil(long time){aliveUntil = time + 2000000000L;} // + 2 Sekunden
    public void setTarget(Monster target){targets.add(target);}
    public boolean getPlayerHit(){return playerHit;}
    public void setPlayerHit(){playerHit = true;}
    public List<Monster> getTargets(){return targets;}
    public int getSource(){return source;}
    public int getDamgage(){return damgage;}
    public int getKind(){return kind;}

    public void update(double dTime, List<Wall> walls){
        boolean collide = false;
        for (Wall wall : walls){    // Checkt ob es mit irgendwelchen Wänden kollidiert
            if (wall.collidesWith(getX(), getY(), radius)) collide = true;
        }
        if (kind == 2){
            if (!collide && getTargets().size() < possibleCollisions){   // Wenn es nicht mit Wänden kollidiert und die Anzahl der Treffer kleiner wie 2 ist dann bewegt es sich
                x += directionX * dTime * SPEED;
                y += directionY * dTime * SPEED;

                sickle.setLayoutX(x);
                sickle.setLayoutY(y);
            }
            else{
                setAliveUntil(1);
            }

        }
        else {
            if (!collide && getTargets().size() < possibleCollisions){   // Wenn es nicht mit Wänden kollidiert und die Anzahl der Treffer kleiner wie 2 ist dann bewegt es sich
                shape.setCenterX(getX() + directionX * dTime * SPEED);
                shape.setCenterY(getY() + directionY * dTime * SPEED);
            }
            else{
                setAliveUntil(1);
            }
        }
    }
}
