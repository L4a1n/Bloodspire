package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Chest {
    private Rectangle shape;
    private String item;

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

    private String generateRandomItem() {
        List<String> items = Arrays.asList("Schwert", "Schild", "Heiltrank", "Schatz");
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    private void openChest() {
        System.out.println("Du hast gefunden: " + item);
        shape.setFill(Color.GRAY); // Markiere geöffnete Truhe
    }
}