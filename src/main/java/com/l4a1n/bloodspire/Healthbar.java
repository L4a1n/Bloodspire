package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Healthbar {
    private Rectangle bg;
    private Rectangle vg;
    private double w;
    private double h;
    private int id;
    private int health;

    public Healthbar(double x, double y, int health, int id){
        this.id = id;
        this.health = health;
        if (id == 0){
            w = 300;
            h = 70;
        }
        else {
            w = 50;
            h = 10;
        }
        bg = new Rectangle(w,h, Color.BLACK);
        bg.setX(x);
        bg.setY(y);
        vg = new Rectangle(w,h, Color.RED);
        vg.setX(x);
        vg.setY(y);
    }

    public Rectangle getBg(){return bg;}
    public Rectangle getVg(){return vg;}
    public int getId(){return id;}

    public void setPos(double newX, double newY){
        bg.setX(newX-25);
        bg.setY(newY-20);
        vg.setX(newX-25);
        vg.setY(newY-20);
    }
    public void decHealth(double damage){
        System.out.println("Decreasing: " + id);
        vg.setWidth(vg.getWidth()-(bg.getWidth()*(damage/health)));
        if (vg.getWidth() <= 0) bg.setFill(Color.TRANSPARENT);
    }
}
