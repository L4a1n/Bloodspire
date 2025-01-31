package com.l4a1n.bloodspire;

import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private Pane gamePane;
    private List<Wall> walls;
    private Player player;
    private List<Monster> monsters;
    private List<Door> doors;
    private Chest chest;
    private Random random;

    public Game() {
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: lightgray;");
        random = new Random();

        setupRoom();
        setupWalls();
        setupPlayer();
        setupMonsters();
        setupChest();
        setupDoors();
        setupMouseClick();
        startGameLoop();
    }

    public Pane getGamePane() {
        return gamePane;
    }

    private void setupRoom() {
        // Raumbegrenzungen
        Rectangle room = new Rectangle(800, 800);
        room.setFill(Color.LIGHTGRAY);
        room.setStroke(Color.BLACK);
        room.setStrokeWidth(5);
        gamePane.getChildren().add(room);
    }

    private void setupWalls(){
        walls = new ArrayList<>();
        
        walls.add(new Wall(100, 200, 30, 80));
        walls.add(new Wall(200, 100, 80, 30));
        walls.add(new Wall(260, 130, 20, 120));
    
        for (Wall wall : walls){
          gamePane.getChildren().add(wall.getShape());
        }

    }

    private void setupPlayer() {
        player = new Player(400, 400);
        gamePane.getChildren().add(player.getShape());
    }

    private void setupMonsters() {
        monsters = new ArrayList<>();
        int monsterCount = random.nextInt(3) + 2; // 2-4 Monster

        for (int i = 0; i < monsterCount; i++) {
            Monster monster = new Monster(random.nextInt(700) + 50, random.nextInt(700) + 50);
            monsters.add(monster);
            gamePane.getChildren().add(monster.getShape());
        }
    }

    private void setupChest() {
        chest = new Chest(random.nextInt(700) + 50, random.nextInt(700) + 50);
        gamePane.getChildren().add(chest.getShape());
    }

    private void setupDoors() {
        doors = new ArrayList<>();
        int doorCount = random.nextInt(3) + 1; // 1-4 Türen

        for (int i = 0; i < doorCount; i++) {
            int side = i % 4; // Bestimmt die Wall
            Door door = new Door(side, random.nextInt(700) + 50);
            doors.add(door);
            gamePane.getChildren().add(door.getShape());
        }
    }

    private void setupMouseClick() {
        // Wenn die Maus gezogen wird...
        gamePane.setOnMouseDragged((MouseEvent event) -> {
            // Wenn es die Linke Maustaste ist
            if (event.getButton() == MouseButton.PRIMARY){
                // Setzt die Koordinaten der Maus zu Zielkoordinaten des Spielers
                double targetX = event.getX();
                double targetY = event.getY();
                player.setTarget(targetX, targetY); // Zielposition setzen
            }
        });
        // Wenn die Maus nur geclickt wird
        gamePane.setOnMouseClicked((MouseEvent event) -> {
            // Wenn es die Linke Maustaste ist
            if (event.getButton() == MouseButton.PRIMARY){
                // Setzt die Koordinaten der Maus zu Zielkoordinaten des Spielers
                double targetX = event.getX();
                double targetY = event.getY();
                player.setTarget(targetX, targetY); // Zielposition setzen
            }
        });
    }

    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update(); // Spielerbewegung
                for (Monster monster : monsters) {
                    monster.moveTowards(player.getX(), player.getY(), monsters);
                }
            }
        };
        gameLoop.start();
    }
}