package com.l4a1n.bloodspire;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class LevelUp extends Group {
    private Level level;
    private Player player;
    private AnimationTimer levelUpLoop;
    private Canvas canvas;
    private GraphicsContext gc;
    private Pane gamePane;
    private double x, y, w, h;
    private int availablePoints;
    private int leftPoints;
    private List<Button> buttonListPls = new ArrayList<>();
    private List<Button> buttonListMin = new ArrayList<>();
    private List<Integer> checkList = new ArrayList<>();

    private Button dmgBtnPls;
    private Button dmgBtnMin;
    private Button colBtnPls;
    private Button colBtnMin;
    private Button HltBtnPls;
    private Button HltBtnMin;

    private Button Ab1BtnPls;
    private Button Ab1BtnMin;
    private Button Ab2BtnPls;
    private Button Ab2BtnMin;

    private Button continueButton;

    private Label damageText;
    private Label cooldownText;
    private Label healthText;
    private Label pointsText;

    public LevelUp(){
        x = 380;
        y = 90;
        w = 520;
        h = 640;

        // Konsumiert alle Mouse Events
        Pane blocker = new Pane();
        blocker.setPrefSize(1280, 720);
        blocker.setLayoutX(0);
        blocker.setLayoutY(0);
        blocker.setStyle("-fx-background-color: rgba(0,0,0,0.2);"); // Fully transparent

        blocker.setPickOnBounds(true);
        blocker.addEventFilter(MouseEvent.ANY, MouseEvent::consume);

        Image background = new Image(getClass().getResource("/LevelUp_Background.png").toExternalForm());
        canvas = new Canvas(w, h);
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.drawImage(background, 0, 0, 256, 315, 0, 0, 512, 630);

        Image buttons = new Image(getClass().getResource("/Buttons_1.png").toExternalForm());
        dmgBtnPls = new Button(buttons, 540, 202, 32, 32, 16, 16, 0);
        buttonListPls.add(dmgBtnPls);
        dmgBtnMin = new Button(buttons, 604, 202, 32, 32, 16, 16, 64);
        buttonListMin.add(dmgBtnMin);
        colBtnPls = new Button(buttons, 540, 298, 32, 32, 16, 16, 0);
        buttonListPls.add(colBtnPls);
        colBtnMin = new Button(buttons, 604, 298, 32, 32, 16, 16, 64);
        buttonListMin.add(colBtnMin);
        HltBtnPls = new Button(buttons, 540, 394, 32, 32, 16, 16, 0);
        buttonListPls.add(HltBtnPls);
        HltBtnMin = new Button(buttons, 604, 394, 32, 32, 16, 16, 64);
        buttonListMin.add(HltBtnMin);

        Ab1BtnPls = new Button(buttons, 540, 490, 32, 32, 16, 16, 0);
        buttonListPls.add(Ab1BtnPls);
        Ab1BtnMin = new Button(buttons, 604, 490, 32, 32, 16, 16, 64);
        buttonListMin.add(Ab1BtnMin);
        Ab2BtnPls = new Button(buttons, 540, 586, 32, 32, 16, 16, 0);
        buttonListPls.add(Ab2BtnPls);
        Ab2BtnMin = new Button(buttons, 604, 586, 32, 32, 16, 16, 64);
        buttonListMin.add(Ab2BtnMin);

        for (int i = 0; i < 5; i++){
            checkList.add(0);
        }

        Image continueButtonImage = new Image(getClass().getResource("/ContinueButton.png").toExternalForm());
        continueButton = new Button(continueButtonImage, x+ 184, y+ 576, 144, 36, 74, 18, 0);
        continueButton.setOnClick(this::resume);

        Font textFont = Font.loadFont(getClass().getResourceAsStream("/joystix monospace.otf"), 9);
        damageText = new Label("Damage: ");
        damageText.setFont(textFont);
        damageText.setTextFill(Color.WHITE);
        damageText.setLayoutX(x + 368);
        damageText.setLayoutY(y + 126);
        damageText.setPrefWidth(72);
        damageText.setPrefHeight(18);
        damageText.setAlignment(Pos.BASELINE_RIGHT);

        cooldownText = new Label("Cooldown: ");
        cooldownText.setFont(textFont);
        cooldownText.setTextFill(Color.WHITE);
        cooldownText.setLayoutX(x + 368);
        cooldownText.setLayoutY(y + 148);
        cooldownText.setPrefWidth(72);
        cooldownText.setPrefHeight(18);
        cooldownText.setAlignment(Pos.BASELINE_RIGHT);

        healthText = new Label("Health: ");
        healthText.setFont(textFont);
        healthText.setTextFill(Color.WHITE);
        healthText.setLayoutX(x + 368);
        healthText.setLayoutY(y + 170);
        healthText.setPrefWidth(72);
        healthText.setPrefHeight(18);
        healthText.setAlignment(Pos.BASELINE_RIGHT);

        pointsText = new Label("Available Points: "+availablePoints);
        pointsText.setFont(textFont);
        pointsText.setTextFill(Color.WHITE);
        pointsText.setLayoutX(x + 284);
        pointsText.setLayoutY(y + 74);
        pointsText.setPrefWidth(168);
        pointsText.setPrefHeight(18);
        pointsText.setAlignment(Pos.BASELINE_CENTER);

        dmgBtnPls.setOnClick(() -> increase("damage"));
        dmgBtnMin.setOnClick(() -> decrease("damage"));
        colBtnPls.setOnClick(() -> increase("cooldown"));
        colBtnMin.setOnClick(() -> decrease("cooldown"));
        HltBtnPls.setOnClick(() -> increase("health"));
        HltBtnMin.setOnClick(() -> decrease("health"));


        this.getChildren().addAll(blocker, canvas, dmgBtnPls.getCanvas(), dmgBtnMin.getCanvas(), colBtnPls.getCanvas(),
                colBtnMin.getCanvas(), HltBtnPls.getCanvas(), HltBtnMin.getCanvas(), Ab1BtnPls.getCanvas(),
                Ab1BtnMin.getCanvas(), Ab2BtnPls.getCanvas(), Ab2BtnMin.getCanvas(), damageText,
                cooldownText, healthText, pointsText, continueButton.getCanvas());
    }

    public void setLevel(Level level){this.level = level;}

    public void setGamePane(Pane gamePane){this.gamePane = gamePane;}

    public void setPlayer(Player player){this.player = player;}

    private void resume(){
        leftPoints = availablePoints;
        levelUpLoop.stop();
        gamePane.getChildren().remove(this);
        Platform.runLater(()->level.setPaused(false));
    }

    private void increase(String kind){
        availablePoints --;
        switch (kind){
            case "damage":
                checkList.set(0, checkList.get(0)+1);
                buttonListMin.get(0).setAvailable();
                player.incBaseDamage(5);
                break;
            case "cooldown":
                checkList.set(1, checkList.get(1)+1);
                buttonListMin.get(1).setAvailable();
                player.decGlobalCooldown(0.05);
                break;
            case "health":
                checkList.set(2, checkList.get(2)+1);
                buttonListMin.get(2).setAvailable();
                player.incMaxHealth(50);
                break;
        }
    }

    private void decrease(String kind){
        availablePoints ++;
        for (Button button : buttonListPls){
            button.setAvailable();
        }
        switch (kind){
            case "damage":
                checkList.set(0, checkList.get(0)-1);
                player.incBaseDamage(-5);
                break;
            case "cooldown":
                checkList.set(1, checkList.get(1)-1);
                player.decGlobalCooldown(-0.05);
                break;
            case "health":
                checkList.set(2, checkList.get(2)-1);
                player.incMaxHealth(-50);
        }
    }

    public void run(){
        level.setPaused(true);
        gamePane.getChildren().add(this);
        gamePane.requestFocus();
        availablePoints = 2 + leftPoints;
        for (Button button : buttonListPls){
            button.setAvailable();
        }
        for (Button button : buttonListMin){
            button.setUnavailable();
        }
        checkList.replaceAll(ignored -> 0);
        player.heal(player.getMaxHealth());

        levelUpLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 0; i < checkList.size(); i++){
                    if (checkList.get(i) == 0){
                        buttonListMin.get(i).setUnavailable();
                    }
                    if (availablePoints == 0){
                        buttonListPls.get(i).setUnavailable();
                    }
                }

                if (player.getCooldown() == 0.2){colBtnPls.setUnavailable();}

                dmgBtnPls.update();
                dmgBtnMin.update();
                colBtnPls.update();
                colBtnMin.update();
                HltBtnPls.update();
                HltBtnMin.update();

                Ab1BtnPls.update();
                Ab1BtnMin.update();
                Ab2BtnPls.update();
                Ab2BtnMin.update();

                continueButton.update();

                damageText.setText("" + player.getBaseDamage());
                cooldownText.setText(String.format("%.2f", player.getCooldown()));
                healthText.setText("" + player.getMaxHealth());
                pointsText.setText("Available Points: "+availablePoints);
            }
        };
        levelUpLoop.start();
    }

}
