package com.l4a1n.bloodspire;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class Button {
    private Canvas canvas;
    private GraphicsContext gc;
    private Image sprite;
    private double x, y, w, h;
    private int frameW, frameH, baseFrameY;
    private int currentState = 0; // 0 = normal, 1 = hover, 2 = pressed, 3 = unavailable

    private final AudioClip hoverSound = new AudioClip(getClass().getResource("/sounds/ButtonHover.wav").toExternalForm());
    private final AudioClip clickSounds = new AudioClip(getClass().getResource("/sounds/ButtonClick.wav").toExternalForm());

    private Runnable onClick;

    public Button(Image sprite, double x, double y, double w, double h, int frameW, int frameH, int frameY) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.frameW = frameW;
        this.frameH = frameH;
        this.baseFrameY = frameY;

        canvas = new Canvas(w, h);
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);

        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        setupMouseEvents();
    }

    public Canvas getCanvas() {return canvas;}

    public void setOnClick(Runnable onClick) {this.onClick = onClick;}

    public void setUnavailable(){currentState = 3;}

    public void setAvailable(){currentState = 0;}

    public boolean getAvailable(){
        return currentState < 3;
    }

    private void setupMouseEvents() {
        canvas.setOnMouseEntered(e -> {
            if (currentState != 3){
                currentState = 1;
                hoverSound.play(); // Hover
            }
        });

        canvas.setOnMouseExited(e -> {
            if (currentState != 3)currentState = 0; // Normal
        });

        canvas.setOnMousePressed(e -> {
            if (currentState != 3){
                currentState = 2;
                clickSounds.play(); // Pressed
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (currentState == 2 && onClick != null) {
                onClick.run(); // Call action
            }
            if (currentState != 3) currentState = 1; // Back to hover
        });
    }

    public void update() {
        gc.clearRect(0, 0, w, h);
        gc.drawImage(sprite, 0, baseFrameY + (currentState * frameH), frameW, frameH, 0, 0, w, h);
    }
}