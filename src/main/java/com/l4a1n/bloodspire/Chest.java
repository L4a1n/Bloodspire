package com.l4a1n.bloodspire;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Chest extends Group {
    private Rectangle shape;
    private String item;
    private boolean used;
    private boolean accesible;
    private double size = 20;
    private long timeSinceUsed;
    private Image opened;
    private Image closed;
    private Canvas canvas;
    private GraphicsContext gc;
    private Player player;
    private Random random;

    public Chest(double x, double y, Player player) {
        shape = new Rectangle(size, size);
        shape.setFill(Color.TRANSPARENT);

        this.player = player;

        canvas = new Canvas(size, size);

        this.setLayoutX(x);
        this.setLayoutY(y);

        opened = new Image(getClass().getResource("/Chest_Opened.png").toExternalForm());
        closed = new Image(getClass().getResource("/Chest_Closed.png").toExternalForm());

        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, size, size);
        gc.setImageSmoothing(false);
        gc.drawImage(closed, 0, 0, size, size);

        used = false;
        accesible = true;

        generateRandomItem();

        this.getChildren().addAll(canvas, shape);
    }

    public Rectangle getShape(){return shape;}
    public String getItem(){ return item; }
    public boolean getUsed(){ return used; }
    public void setUsed(long time) { used = true; timeSinceUsed = time + 5000000000L; }
    public boolean getAccessible(){ return accesible; }
    public void setInaccessible(){ accesible = false; }
    public long getTimeSinceUsed(){ return timeSinceUsed; }

    public String openChest() {
        if (!used && accesible){
            System.out.println("Du hast gefunden: " + item);
        }
        gc.drawImage(opened, 0, 0, size, size);
        return item;
    }

    private void generateRandomItem() {
        List<String> items = Arrays.asList("Salve", "Wave", "HealthPotion", "HealthPotion", "HealthPotion");
        random = new Random();
        item = items.get(random.nextInt(items.size()));

    }
}