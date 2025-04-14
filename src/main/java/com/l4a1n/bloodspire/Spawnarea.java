package com.l4a1n.bloodspire;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

// Spawnareas are created at random places.. then inside these spawnareas, monster will be spawned
public class Spawnarea {
    private Rectangle shape;
    private Image spriteSheet;
    private Canvas canvas;
    private GraphicsContext gc;
    private int frameSize = 32;
    private int numFrames = 12;
    private int currentFrame = 0;
    private double frameDuration = 12;
    private double lastUpdate = 0;
    private double size = 60;
    private Random random;
    private long lastSpawnTime;
    private long spawnDelay = 1000000000L;
    private double timeUntilEnd = 50;
    private double aliveSince = 0;

    public Spawnarea(double x, double y){
        shape = new Rectangle(x, y, size, size);
        shape.setFill(Color.TRANSPARENT);

        spriteSheet = new Image(getClass().getResource("/Spawn_Spritesheet.png").toExternalForm());

        canvas = new Canvas(size, size);
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        random = new Random();
        lastSpawnTime = 0;
    }

    public Rectangle getShape(){return shape;}
    public Canvas getCanvas(){return canvas;}
    public double getX(){return shape.getX();}
    public double getY(){return shape.getY();}
    public double getSize(){return size;}

    public boolean hasEnded(double dTime){
        aliveSince += dTime;
        return aliveSince >= timeUntilEnd;
    }

    public void animate(double dTime){
        if (lastUpdate == 0){
            lastUpdate = dTime;
            return;
        }
        if (lastUpdate >= 1.0 / frameDuration){
            gc.clearRect(0, 0, frameSize, frameSize);

            int frameX = (currentFrame % numFrames) * frameSize;
            gc.drawImage(spriteSheet, frameX, 0, frameSize, frameSize, 0, 0, size, size);
            currentFrame = (currentFrame+1) % numFrames;

            lastUpdate = dTime;
        }
        else lastUpdate += dTime;
    }

    public boolean spawnMonster(long time){
        if (lastSpawnTime == 0){
            lastSpawnTime = time + spawnDelay + random.nextLong(5000000000L);
            return false;
        }
        else if (time > lastSpawnTime) {
            lastSpawnTime = time + spawnDelay + random.nextLong(5000000000L);
            return true;
        }
        else return false;
    }
}
