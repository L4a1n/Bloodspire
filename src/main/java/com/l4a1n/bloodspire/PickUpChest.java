package com.l4a1n.bloodspire;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class PickUpChest extends Group {
    private Level level;
    private Player player;
    private AnimationTimer loop;
    private Canvas canvas;
    private GraphicsContext gc;
    private Pane gamePane;
    private double x, y, w, h;
    private long lastUpdate;
    private Animation chestAnimation;

    private Button continueButton;

    public PickUpChest(){
        x = 448; // 448
        y = 220; // 200
        w = 384;
        h = 384;


        // Konsumiert alle Mouse Events
        Pane blocker = new Pane();
        blocker.setPrefSize(1280, 720);
        blocker.setLayoutX(0);
        blocker.setLayoutY(0);
        blocker.setStyle("-fx-background-color: rgba(0,0,0,0.2);"); // Fully transparent

        blocker.setPickOnBounds(true);
        blocker.addEventFilter(MouseEvent.ANY, MouseEvent::consume);

        Image background = new Image(getClass().getResource("/Chest_Background.png").toExternalForm());
        canvas = new Canvas(w, h);
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.drawImage(background, 0, 0, 128, 128, 0, 0, 384, 384);

        Image continueButtonImage = new Image(getClass().getResource("/ContinueButton.png").toExternalForm());
        continueButton = new Button(continueButtonImage, x+120, y+330, 144, 36, 74, 18, 0);
        continueButton.setOnClick(this::resume);

        Image chestSpritesheet = new Image(getClass().getResource("/ChestAnimation.png").toExternalForm());
        chestAnimation = new Animation(chestSpritesheet, x+96, y+96, 192, 192, 32, 32, 23, 11);


        this.getChildren().addAll(blocker, canvas, continueButton.getCanvas(), chestAnimation.getCanvas());
        continueButton.getCanvas().setStyle("-fx-border-color: red;");

    }

    public void setLevel(Level level){this.level = level;}

    public void setGamePane(Pane gamePane){this.gamePane = gamePane;}

    public void setPlayer(Player player){this.player = player;}

    private void resume(){
        loop.stop();
        gamePane.getChildren().remove(this);
        Platform.runLater(()->level.setPaused(false));
    }

    public void run(String item){
        gamePane.getChildren().add(this);
        gamePane.requestFocus();
        chestAnimation.play();
        chestAnimation.setCurrentFrame(0);

        loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0){
                    lastUpdate = now;
                    return;
                }

                double dTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                if (chestAnimation.getCurrentFrame() == 22) chestAnimation.pause();
                chestAnimation.animate(dTime);
                continueButton.update();

            }
        };
        loop.start();
    }
}
