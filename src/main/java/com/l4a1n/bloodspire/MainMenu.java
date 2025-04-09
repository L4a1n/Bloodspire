package com.l4a1n.bloodspire;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainMenu extends Group {
    private Rectangle background;
    private Button playButton;
    private Button quitButton;
    private Image icons;
    private Level level;
    private Pane gamePane;
    private AnimationTimer menuLoop;
    private MouseEvent currentMouseEvent;

    public MainMenu(){
        icons = new Image(getClass().getResource("/Menu_Icons.png").toExternalForm());
        background = new Rectangle(0, 0, 1280, 820);
        background.setFill(Color.BLACK);

        playButton = new Button(icons, 540, 460, 200, 100, 64, 32, 0);
        quitButton = new Button(icons, 540, 610, 200, 100, 64, 32, 96);

        playButton.setOnClick(this::startLevel);
        quitButton.setOnClick(()-> Platform.exit());

        this.getChildren().addAll(background, playButton.getCanvas(), quitButton.getCanvas());
    }

    public void setLevel(Level level){this.level = level;}
    public void setGamePane(Pane gamePane){this.gamePane = gamePane;}


    public void startLevel(){
        menuLoop.stop();
        gamePane.getChildren().remove(this);

        Platform.runLater(() -> gamePane.requestFocus());

        level.setupRoom();
        level.setupWalls();
        level.setupSpawns();
        level.setupChest();
        level.setupPlayer();
        level.setupMouseClick();
        level.setupKeyDown();
        level.setupIntro();
        level.setupGameOver();

        level.startGameLoop();
    }


    public void run(){
        gamePane.getChildren().add(this);
        gamePane.requestFocus();

        menuLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0){
                    lastUpdate = now;
                    return;
                }

                double dTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                playButton.update();
                quitButton.update();

            }
        };
        menuLoop.start();
    }
}
