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
    private GraphicsContext gc2;
    private Player player;
    private Random random;
    private Canvas icon;
    private Image iconSpritesheet;
    private double lastUpdate = 0;
    private int frameSize = 16;
    private int numFrames = 5;
    private int currentFrame = 0;
    private double frameDuration = 7;

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

        icon = new Canvas(size, size);
        icon.setLayoutY(-size/1.5);

        iconSpritesheet = new Image(getClass().getResource("/AttentionIcon.png").toExternalForm());

        gc2 = icon.getGraphicsContext2D();
        gc2.clearRect(0, 0, size, size);
        gc2.setImageSmoothing(false);


        generateRandomItem();

        this.getChildren().addAll(canvas, shape, icon);
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

    public void update(double dTime){
        if (!used && accesible){
            lastUpdate += dTime;
            if (lastUpdate >= 1.0 / frameDuration){
                gc2.clearRect(0, 0, size, size);
                int frameX = (currentFrame % numFrames) * frameSize;
                int frameY = 0;

                gc2.drawImage(iconSpritesheet, frameX, frameY, frameSize, frameSize, 0, 0, size, size);
                currentFrame = (currentFrame + 1) % numFrames;
                lastUpdate = 0;
            }
        }
        else {
            gc2.clearRect(0,0,size,size);
        }


    }
}