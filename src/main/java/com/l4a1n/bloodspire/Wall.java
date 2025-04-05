package com.l4a1n.bloodspire;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends Group {
    private Rectangle shape;

    public Wall(double x, double y, double w, double h){
        shape = new Rectangle(w, h, Color.TRANSPARENT);

        this.setLayoutX(x);
        this.setLayoutY(y);

        this.getChildren().add(shape);
    }

    // Getter Methoden
    public Rectangle getShape() { return shape; }
    public double getX() { return this.getLayoutX(); }
    public double getY() { return this.getLayoutY(); }
    public double getW() { return shape.getWidth(); }
    public double getH() { return shape.getHeight(); }

    // Kollisionsmethode
    public boolean collidesWith(double x, double y, double r){
        double left = getX();
        double right = getX() + getW();
        double top = getY();
        double bottom = getY() + getH();

        return (x + r >= left && x - r <= right && y + r >= top && y - r <= bottom);
    }
}