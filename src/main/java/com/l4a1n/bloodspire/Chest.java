package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Chest {
    private Rectangle shape;
    private String item;
    private boolean used;
    private boolean accesible;
    private double size = 50;
    private long timeSinceUsed;
    private Image opened;
    private Image closed;
    private Canvas canvas;
    private GraphicsContext gc;

    public Chest(double x, double y) {
        shape = new Rectangle(size, size);
        shape.setX(x);
        shape.setY(y);
        shape.setFill(Color.TRANSPARENT);

        canvas = new Canvas(size, size);
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);

        opened = new Image(getClass().getResource("/Chest_Opened.png").toExternalForm());
        closed = new Image(getClass().getResource("/Chest_Closed.png").toExternalForm());

        gc = canvas.getGraphicsContext2D();
        gc.clearRect(x, y, size, size);
        gc.setImageSmoothing(false);
        gc.drawImage(closed, 0, 0, size, size);

        used = false;
        accesible = true;

        item = generateRandomItem();
    }

    public double getX(){return shape.getX();}
    public double getY(){return shape.getY();}
    public double getSize(){return size;}
    public Rectangle getShape() {return shape;}
    public Canvas getCanvas(){return canvas;}
    public String getItem(){return item;}
    public void setInaccessible(){accesible = false;}
    public boolean getUsed(){return used;}
    public void setUsed(long time){used = true; timeSinceUsed = time + 5000000000L;}
    public boolean getAccessible(){return accesible;}
    public long getTimeSinceUsed(){return timeSinceUsed;}

    private String generateRandomItem() {
        List<String> items = Arrays.asList("Salve", "HealthPotion", "HealthPotion");
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    public String openChest() {
        if (!used && accesible){
            System.out.println("Du hast gefunden: " + item);
        }
        gc.drawImage(opened, 0, 0, size, size);
        return item;
    }
}