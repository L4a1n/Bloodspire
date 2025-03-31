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
    private int animCount;
    private int percantage = 100;
    private int colorG = 0;
    private int colorB = 0;

    public Healthbar(double x, double y, int health, int id){
        this.id = id;
        this.health = health;
        if (id == 0){
            w = 300;
            h = 70;
            animCount = 0;
        }
        else {
            w = 50;
            h = 10;
        }
        bg = new Rectangle(w,h, Color.BLACK);
        bg.setX(x);
        bg.setY(y);
        vg = new Rectangle(w,h, Color.rgb(240, 0,0));
        vg.setX(x);
        vg.setY(y);
    }

    public Rectangle getBg(){return bg;}
    public Rectangle getVg(){return vg;}
    public int getId(){return id;}
    public int getPercantage(){return percantage;}

    // Animation für niedriges Leben
    public void animate(double dTime){
        if (animCount < 100){
            animCount ++;
            colorG ++;
            colorB ++;
            vg.setFill(Color.rgb(240, colorG, colorB));
        }
        else{
            animCount = 0;
            colorB = 0;
            colorG = 0;
        }
    }

    public void setPos(double newX, double newY){
        bg.setX(newX-25);
        bg.setY(newY-20);
        vg.setX(newX-25);
        vg.setY(newY-20);
    }
    public void decHealth(double damage){
        vg.setWidth(vg.getWidth()-(bg.getWidth()*(damage/health)));
        percantage = (int)Math.round(vg.getWidth()/bg.getWidth()*100);
        if (vg.getWidth() <= 0) bg.setFill(Color.TRANSPARENT);
    }
}
