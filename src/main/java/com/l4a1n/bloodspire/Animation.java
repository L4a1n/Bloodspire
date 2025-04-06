package com.l4a1n.bloodspire;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Animation {
    private int frameW;
    private int frameH;
    private int numFrames;
    private double frameDuration;
    private Image spritesheet;
    private double width;
    private double heigth;
    private double lastUpdate = 0;
    private int currentFrame = 0;
    private int row = 0;
    private Canvas canvas;
    private GraphicsContext gc;

    public Animation(Image spritesheet, double x, double y, double width, double heigth, int frameW, int frameH, int numFrames, double frameDuration){
        this.spritesheet = spritesheet;
        this.frameW = frameW;
        this.frameH = frameH;
        this.numFrames = numFrames;
        this.frameDuration = frameDuration;
        this.width = width;
        this.heigth = heigth;

        canvas = new Canvas(width, heigth);
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
    }

    public Canvas getCanvas(){return canvas;}

    public void changeRow(int row){
        this.row = row;
    }

    public void animate(double dTime){
        lastUpdate += dTime;
        if (lastUpdate >= 1.0 / frameDuration){
            gc.clearRect(0, 0, width, heigth);
            int frameX = (currentFrame % numFrames) * frameW;
            int frameY = row * frameH;

            gc.drawImage(spritesheet, frameX, frameY, frameW, frameH, 0, 0, width, heigth);
            currentFrame = (currentFrame + 1) %numFrames;
            lastUpdate = 0;

        }
    }

}
