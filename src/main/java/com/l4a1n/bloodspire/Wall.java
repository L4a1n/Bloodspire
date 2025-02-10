package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall {
    private Rectangle shape;

    public Wall(double x, double y, double w, double h){
        shape = new Rectangle(w, h, Color.BLACK);
        shape.setX(x);
        shape.setY(y);
        shape.setWidth(w);
        shape.setHeight(h);
    }

    // Getter Methoden
    public Rectangle getShape(){return shape;}
    public double getX(){return shape.getX();}
    public double getY(){return shape.getY();}
    public double getW(){return shape.getWidth();}
    public double getH(){return shape.getHeight();}

    // Collisionsmethode kann von anderen Klassen benutzt werden um zurück zu geben ob sie mit der Wand kollidieren.
    public boolean collidesWith(double x, double y, double r){
         double left = getX();
         double right = getX() + getW();
         double top = getY();
         double bottom = getY() + getH();
         
         return (x + r > left && x - r < right && y + r > top && y - r < bottom);
    }


}
