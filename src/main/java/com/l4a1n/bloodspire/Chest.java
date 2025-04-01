package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Chest {
    private Rectangle shape;
    private String item;
    private boolean used;
    private boolean accesible;
    private double size = 40;

    public Chest(double x, double y) {
        shape = new Rectangle(size, size, Color.GOLD);
        shape.setX(x);
        shape.setY(y);

        used = false;
        accesible = true;

        item = generateRandomItem();
    }

    public double getX(){return shape.getX();}
    public double getY(){return shape.getY();}
    public double getSize(){return size;}
    public Rectangle getShape() {
        return shape;
    }
    public String getItem(){return item;}
    public void setAccesible(){accesible = true;}

    private String generateRandomItem() {
        List<String> items = Arrays.asList("Salve", "Wave", "Blast", "HealthPotion");
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    public String openChest() {
        if (!used && accesible){
            System.out.println("Du hast gefunden: " + item);
        }
        shape.setFill(Color.GRAY); // Markiere geöffnete Truhe
        used = true;
        return item;
    }
}