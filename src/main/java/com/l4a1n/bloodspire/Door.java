package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Door {
    private Rectangle shape;

    public Door(int side, double position) {
        shape = new Rectangle(40, 10, Color.BROWN);

        switch (side) {
            case 0 -> { // Oben
                shape.setX(position);
                shape.setY(0);
            }
            case 1 -> { // Rechts
                shape.setX(790);
                shape.setY(position);
                shape.setWidth(10);
                shape.setHeight(40);
            }
            case 2 -> { // Unten
                shape.setX(position);
                shape.setY(790);
            }
            case 3 -> { // Links
                shape.setX(0);
                shape.setY(position);
                shape.setWidth(10);
                shape.setHeight(40);
            }
        }
    }

    public Rectangle getShape() {
        return shape;
    }
}