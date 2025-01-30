package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall {
    private Rectangle shape;

    public Wall(double x, double y, double w, double h){
        shape = new Rectangle(w, h, Color.BLACK);
        shape.setX(x);
        shape.setY(y);
    }

    public Rectangle getShape(){return shape;}
    //public double getX(){}                // return X Koordinate proportional von der Mitte der Figur
    //public double getY(){}                // return Y Koordinate proportional von der Mitte der Figur

}
