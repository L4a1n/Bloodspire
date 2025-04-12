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
    private Image titel;
    private Animation titelAnimation;
    private Level level;
    private Pane gamePane;
    private AnimationTimer menuLoop;
    private MouseEvent currentMouseEvent;

    public MainMenu(){
        icons = new Image(getClass().getResource("/Menu_Icons.png").toExternalForm());
        background = new Rectangle(0, 0, 1280, 820);
        background.setFill(Color.BLACK);

        titel = new Image(getClass().getResource("/Title_Sprite.png").toExternalForm());
        titelAnimation = new Animation(titel, 140, 200, 1000, 175.5, 103, 18, 15, 5);


        playButton = new Button(icons, 400, 610, 192, 96, 64, 32, 0);
        quitButton = new Button(icons, 680, 610, 192, 96, 64, 32, 96);


        this.getChildren().addAll(background, titelAnimation.getCanvas(), playButton.getCanvas(), quitButton.getCanvas());
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
        level.setupLevelUp();

        Platform.runLater(() -> level.startGameLoop());     // Funktioniert nicht wie es soll, könnte aber...
    }


    public void run(){
        gamePane.getChildren().add(this);
        gamePane.requestFocus();
        playButton.setAvailable();
        quitButton.setAvailable();

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

                if (titelAnimation.getCurrentFrame() != 14){
                    titelAnimation.animate(dTime);
                }
                else {
                    playButton.setOnClick(() -> startLevel());
                    quitButton.setOnClick(Platform::exit);
                    playButton.update();
                    quitButton.update();
                }

            }

        };
        menuLoop.start();
    }
}
