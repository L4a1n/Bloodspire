package com.l4a1n.bloodspire;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.Random;

// Spawnareas are created at random places.. then inside these spawnareas, monster will be spawned
public class Spawnarea {
    private Rectangle shape;
    private double size = 100;
    private Random random;
    private long lastSpawnTime;
    private long spawnDelay = 1000000000L;

    public Spawnarea(double x, double y){
        shape = new Rectangle(x, y, size, size);
        shape.setFill(Color.BLUE);
        random = new Random();
        lastSpawnTime = 0;
    }
    public Rectangle getShape(){return shape;}
    public double getX(){return shape.getX();}
    public double getY(){return shape.getY();}
    public double getSize(){return size;}

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
