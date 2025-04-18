package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Monster {
    private Circle shape;
    private Image spriteSheet;
    private Canvas canvas;
    private GraphicsContext gc;
    private int frameSize;
    private int numFrames;
    private int currentFrame = 0;
    private double frameDuration;
    private double lastUpdate = 0;
    private double SPEED = 40;                // Geschwindigkeit des Monsters
    private double AVOID_DISTANCE = 40;        // Abstand der zu andern Entitäten eingehalten werden soll
    private int health;
    private boolean alive;
    private double radius = 10;
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
    private int directionRow = 0;
    private int xp;
    private boolean returnedXP = false;

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
                spriteSheet = new Image(getClass().getResource("/SlimeSpritesheet.png").toExternalForm());
                frameSize = 16;
                numFrames = 9;
                frameDuration = 12;
                shape.setVisible(false);

                radius = 15;
                health = 100;
                damage = 10;
                range = 50;
                cooldown = 1000000000;
                xp = 30;
                break;
            case 2:
                spriteSheet = new Image(getClass().getResource("/EvilEye_Spritesheet.png").toExternalForm());
                frameSize = 32;
                numFrames = 8;
                frameDuration = 10;
                shape.setVisible(false);

                health = 50;
                damage = 15;
                SPEED = 20;
                range = 160;
                cooldown = 1000000000;
                AVOID_DISTANCE = 150;
                xp = 30;
                break;
            case 3:
                spriteSheet = new Image(getClass().getResource("/Lich_Spritesheet.png").toExternalForm());
                frameSize = 16;
                numFrames = 2;
                frameDuration = 3;
                shape.setVisible(false);

                radius = 20;
                health = 120;
                damage = 25;
                SPEED = 15;
                range = 200;
                cooldown = 1500000000;
                AVOID_DISTANCE = 70;
                xp = 50;
                break;
        }
        canvas = new Canvas(radius*2, radius*2);
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
    }

    // Getters
    public Circle getShape() {return shape;}        // Gibt das Objekt Circle zurück
    public Canvas getCanvas(){return canvas;}
    public double getX(){return shape.getCenterX();}                // return X Koordinate proportional von der Mitte der Figur
    public double getY(){return shape.getCenterY();}                // return Y Koordinate proportional von der Mitte der Figur
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
    public int getXp(){returnedXP = true; return xp;}
    public boolean getReturnedXP(){return returnedXP;}

    public void animate(double dTime){
        if (spriteSheet == null) return;

        if (attacking || kind == 1 || kind == 3) {
            lastUpdate += dTime;
            if (lastUpdate >= 1.0 / frameDuration){
                gc.clearRect(0, 0, radius*2, radius*2);
                int frameX = (currentFrame % numFrames) * frameSize;
                int frameY = directionRow * frameSize;

                gc.drawImage(spriteSheet, frameX, frameY, frameSize, frameSize, 0, 0, radius*2, radius*2);
                currentFrame = (currentFrame + 1) % numFrames;
                lastUpdate = 0;
            }
        } else {
            // Just draw first frame of current direction
            gc.clearRect(0, 0, radius*2, radius*2);
            int frameY = directionRow * frameSize;
            gc.drawImage(spriteSheet, 0, frameY, frameSize, frameSize, 0, 0, radius*2, radius*2);
        }
    }

    // Positions- und Collisions Update-Methode
    public void moveTowards(double targetX, double targetY, List<Monster> monsters, List<Wall> walls, double dTime) {
        double dx = targetX - shape.getCenterX();   // X Differenz zu Target
        double dy = targetY - shape.getCenterY();   // Y Differenz zu Target
        double distance = Math.sqrt(dx * dx + dy * dy);     // Rechnet die Distanz zu Target aus
        double angle = Math.atan2(dy, dx); // Radians: -π to π

        if (angle >= -Math.PI / 8 && angle < Math.PI / 8)
            directionRow = 3; // → right
        else if (angle >= Math.PI / 8 && angle < 3 * Math.PI / 8)
            directionRow = 0; // ↘ right-down
        else if (angle >= 3 * Math.PI / 8 && angle < 5 * Math.PI / 8)
            directionRow = 2; // ↓ down
        else if (angle >= 5 * Math.PI / 8 && angle < 7 * Math.PI / 8)
            directionRow = 1; // ↙ left-down
        else if (angle >= 7 * Math.PI / 8 || angle < -7 * Math.PI / 8)
            directionRow = 4; // ← left
        else if (angle >= -7 * Math.PI / 8 && angle < -5 * Math.PI / 8)
            directionRow = 5; // ↑ up
        else if (angle >= -5 * Math.PI / 8 && angle < -3 * Math.PI / 8)
            directionRow = 5; // ↑ up
        else if (angle >= -3 * Math.PI / 8 && angle < -Math.PI / 8)
            directionRow = 5; // ↑ up

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
        if (!collideX) shape.setCenterX(getX() + moveX); canvas.setLayoutX((getX() - radius) + moveX);    // Wenn auf X keine Kollision dann bewegen auf X möglich
        if (!collideY) shape.setCenterY(getY() + moveY); canvas.setLayoutY((getY() - radius) + moveY);    // Wenn auf Y keine Kollision dann bewegen auf Y möglich
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