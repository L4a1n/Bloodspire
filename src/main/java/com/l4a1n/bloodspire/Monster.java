package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.List;

public class Monster {
    private Circle shape;
    private double SPEED = 110;                // Geschwindigkeit des Monsters
    private double AVOID_DISTANCE = 40;        // Abstand der zu andern Entitäten eingehalten werden soll
    private int health;
    private boolean alive;
    private double radius = 20;
    private int id;
    private long deadSince;
    private boolean attacking;
    private int damage;
    private int kind;
    private int range;
    private long attackCooldown = 0;
    private long cooldown;
    private double reciveKnockback = 1;
    private boolean droppedLoot;

    public Monster(double x, double y, int id, int kind) {
        shape = new Circle(radius, Color.RED);
        shape.setCenterX(x);
        shape.setCenterY(y);
        alive = true;
        droppedLoot = false;
        this.id = id;
        this.kind = kind;
        switch (kind){
            case 1:
                health = 100;
                damage = 10;
                range = 50;
                cooldown = 1000000000;
                break;
            case 2:
                health = 200;
                damage = 30;
                shape.setFill(Color.DARKRED);
                SPEED = 70;
                range = 350;
                cooldown = 1000000000;
                AVOID_DISTANCE = 150;
                break;
        }
    }

    // Getters
    public Circle getShape() {return shape;}        // Gibt das Objekt Circle zurück
    public double getX(){return shape.getCenterX();}                // return X Koordinate proportional von der Mitte der Figur
    public double getY(){return shape.getCenterY();}                // return Y Koordinate proportional von der Mitte der Figur
    public double X(){return shape.getCenterX()-radius;}
    public double Y(){return shape.getCenterY()-radius;}
    public boolean isAlive(){return alive;}
    public int getId(){return id;}
    public long getDeadSince(){return deadSince;}
    public int getHealth(){return health;}
    public boolean getAttacking(){return attacking;}
    public int getDamage(){return damage;}
    public int getKind(){return kind;}
    public void setAttackCooldown(long timeNow){attackCooldown = timeNow + cooldown;}
    public long getAttackCooldown(){return attackCooldown;}
    private double getReciveKnockback(){return reciveKnockback;}
    private void setReciveKnockback(double kb){reciveKnockback = kb;}
    public boolean getDroppedLoot(){return droppedLoot;}
    public void setDroppedLoot(){droppedLoot = true;}

    // Positions- und Collisions Update-Methode
    public void moveTowards(double targetX, double targetY, List<Monster> monsters, List<Wall> walls, double dTime) {
        double dx = targetX - shape.getCenterX();   // X Differenz zu Target
        double dy = targetY - shape.getCenterY();   // Y Differenz zu Target
        double distance = Math.sqrt(dx * dx + dy * dy);     // Rechnet die Distanz zu Target aus

        if (distance <= range && alive){attacking = true;}
        else {attacking = false;}

        if (distance < AVOID_DISTANCE) return;      // Bricht ab hier ab, wenn die Distanz zu Target kleiner als AVOID_DISTANCE ist

        double moveX = (dx / distance) * SPEED * dTime;     // Bestimmt wie viele Pixel auf X das Monster hinter sich legen soll
        double moveY = dy / distance * SPEED * dTime;       // Bestimmt wie viele Pixel auf Y das Monster hinter sich legen soll

        for (Monster other : monsters){
            if (other == this) continue;    // Wenn es sich um sich selber handelt, dann soll die nächste Iteration der Schleife fortgesetzt werden (Damit man keine Kollision mit sich selber überprüft)

            double diffX = getX() - other.getX();       // X Differenz zu andern Monstern
            double diffY = getY() - other.getY();       // Y Differenz zu andern Monstern
            double otherDistance = Math.sqrt(diffX * diffX + diffY * diffY);    // Rechnet die Distanz zu anderen Monstern aus

            if (otherDistance < AVOID_DISTANCE && otherDistance > 0){
                moveX += diffX / otherDistance * SPEED * dTime * 0.5;
                moveY += diffY / otherDistance * SPEED * dTime * 0.5;
            }
        }
        moveX = moveX * getReciveKnockback();       // berechnet den erlittenen Knockback auf der X Achse
        moveY = moveY * getReciveKnockback();       // berechnet den erlittenen Knockback auf der Y Achse
        boolean collideX = false, collideY = false;     // Kollisions Attribute
        for (Wall wall : walls){
            if (wall.collidesWith(getX() + moveX, getY(), radius)) collideX = true;     // Kollision auf X dann True
            if (wall.collidesWith(getX(), getY() + moveY, radius)) collideY = true;     // Kollision auf Y dann True
        }
        if (!collideX) shape.setCenterX(getX() + moveX);    // Wenn auf X keine Kollision dann bewegen auf X möglich
        if (!collideY) shape.setCenterY(getY() + moveY);    // Wenn auf Y keine Kollision dann bewegen auf Y möglich
        if (getReciveKnockback() < 1) setReciveKnockback(getReciveKnockback()+0.1);       // Sorgt dafür das der erlittene Knockback immer weniger wird
    }

    public void kill(int damage, long time, double knockback){
        health -= damage;
        setReciveKnockback(knockback);
        if (health <= 0){
            shape.setFill(Color.YELLOW);
            alive = false;
            deadSince = time + 1000000000L;
        }
    }

}