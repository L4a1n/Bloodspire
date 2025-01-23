import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private Pane gamePane;
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
        setupPlayer();
        setupMonsters();
        setupChest();
        setupDoors();
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
            int side = i % 4; // Bestimmt die Wand
            Door door = new Door(side, random.nextInt(700) + 50);
            doors.add(door);
            gamePane.getChildren().add(door.getShape());
        }
    }

    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update();
                for (Monster monster : monsters) {
                    monster.moveTowards(player.getX(), player.getY());
                }
            }
        };
        gameLoop.start();
    }
}