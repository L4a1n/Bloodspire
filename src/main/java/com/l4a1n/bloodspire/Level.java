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

public class Level {
    private Pane gamePane;
    private List<Wall> walls;
    private Player player;
    private List<Monster> monsters;
    private List<Door> doors;
    private List<Healthbar> healthbars;
    private List<Projectile> projectiles = new ArrayList<>();
    private Chest chest;
    private Random random;

    public Level() {
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
        healthbars = new ArrayList<>();

        int monsterCount = 4; // 2-4 Monster
        for (int i = 0; i < monsterCount; i++) {
            boolean again = false;
            double x = random.nextDouble(700)+50;
            double y = random.nextDouble(700)+50;
            for (Wall wall : walls){
                if (x+20 >= wall.getX() && x-20 <= wall.getX()+wall.getW() && y+20 >= wall.getY() && y-20 <= wall.getY()+ wall.getH()){     // Checkt ob die Spawn-Koordinaten auf den Wänden liegen damit die Monster nicht auf den Wänden spawnen
                    System.out.println("Oh oh");
                    i -= 1;     // Wenn ja, dann reduziert es den Iterations-Zähler um 1 und unterbricht den for-loop
                    again = true;
                    break;
                }
            }
            if (!again){
                Monster monster = new Monster(x, y, i);
                monsters.add(monster);
                gamePane.getChildren().add(monster.getShape());
                Healthbar healthbar = new Healthbar(x-20,y-20, i);
                healthbars.add(healthbar);
                gamePane.getChildren().add(healthbar.getBg());
                gamePane.getChildren().add(healthbar.getVg());
            }
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
        // Wenn der Spieler auf das Monster klickt wird es angegriffen
        gamePane.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton() == MouseButton.SECONDARY){
                Projectile projectile = new Projectile(player.getX(), player.getY(), event.getX(), event.getY());
                projectiles.add(projectile);
                gamePane.getChildren().add(projectile.getShape());
            }
        });
    }

    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (monsters.isEmpty()){
                    setupMonsters();
                }
                if (lastUpdate == 0){
                    lastUpdate = now;
                    return;
                }

                double dTime = (now - lastUpdate) / 10_000_000.0;

                for (Projectile projectile : projectiles){
                    if (projectile.getAliveUntil() == 0){   // Wenn die Zeit 0 ist dann wird die aktuelle Zeit gesetzt
                        projectile.setAliveUntil(now);
                        continue;
                    }
                    if (projectile.getAliveUntil() <= now){     // Wenn die Zeit zu leben überschritten ist dann wird es gelöscht
                        gamePane.getChildren().remove(projectile.getShape());
                        projectiles.remove(projectile);
                        return;
                    }
                    projectile.update(dTime, walls);
                }
                // Rechnet DeltaTime aus (die Zeit die Seit dem letzen Frame vergangen ist)
                // Wird benutzt um die Geschwindigkeit anzupassen, sodass die Bewwegung Framerate-unabhängig ist

                lastUpdate = now;
                player.update(dTime, walls); // Spielerbewegung
                for (Monster monster : monsters) {
                    if (monster.isAlive()){
                        monster.moveTowards(player.getX(), player.getY(), monsters, walls, dTime);
                        for (Healthbar hb : healthbars){
                            if (monster.getId() == hb.getId()) hb.setPos(monster.getX(), monster.getY());
                        }
                        for (Projectile projectile : projectiles){
                            if ((projectile.getX() >= monster.X() && projectile.getX() <= monster.X()+40) && (projectile.getY() >= monster.Y() && projectile.getY() <= monster.Y()+40) && !projectile.getTargets().contains(monster)) {
                                monster.kill(25, now);
                                projectile.setTarget(monster);
                                for (Healthbar hb : healthbars){
                                    if (monster.getId() == hb.getId()){
                                        hb.decHealth(25);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (monster.getDeadSince() <= now){
                            gamePane.getChildren().remove(monster.getShape());
                            monsters.remove(monster);
                            return;
                        }
                    }
                }
            }
        };
        gameLoop.start();
    }
}