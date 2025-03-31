package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Chest {
    private Rectangle shape;
    private String item;
    private boolean used = false;

    public Chest(double x, double y) {
        shape = new Rectangle(40, 20, Color.GOLD);
        shape.setX(x);
        shape.setY(y);

        item = generateRandomItem();
        shape.setOnMouseClicked(e -> openChest());
    }

    public Rectangle getShape() {
        return shape;
    }
    public String getItem(){return item;}

    private String generateRandomItem() {
        List<String> items = Arrays.asList("Wave-Attack", "Spiral-Attack", "Health-Potion", "Blast-Attack");
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    private void openChest() {
        if (!used){
            System.out.println("Du hast gefunden: " + item);
        }
        shape.setFill(Color.GRAY); // Markiere geöffnete Truhe
        used = true;
    }
}