package com.l4a1n.bloodspire;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class LevelUp extends Group {
    private Level level;
    private Canvas canvas;
    private GraphicsContext gc;
    private Image background;
    private double x, y, w, h;

    public LevelUp(){
        x = 380;
        y = 80;
        w = 520;
        h = 640;

        background = new Image(getClass().getResource("/LevelUp_Background.png").toExternalForm());

        canvas = new Canvas(w, h);
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);

        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        gc.drawImage(background, 0, 0, 256, 315, 0, 0, 520, 640);

        this.getChildren().addAll(canvas);
    }

    public void setLevel(Level level){this.level = level;}

}
