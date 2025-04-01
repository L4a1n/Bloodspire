package com.l4a1n.bloodspire;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
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
    private AbilityBar abilityBar;
    private List<Monster> monsters;
    private List<Door> doors;
    private List<Healthbar> healthbars;
    private List<Projectile> projectiles = new ArrayList<>();
    private List<Chest> chests;
    private Random random;

    public Level() {
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: lightgray;");
        gamePane.setFocusTraversable(true);
        random = new Random();

        healthbars = new ArrayList<>();

        setupRoom();
        setupWalls();
        setupChest();
        setupDoors();
        setupPlayer();
        setupMonsters();
        setupMouseClick();
        setupKeyDown();

        gamePane.requestFocus();

        startGameLoop();
    }

    public Pane getGamePane() {
        return gamePane;
    }

    private void setupRoom() {
        // Raumbegrenzungen
        // Der Part hier muss mit was sinnvollem ersetzt werden...
        // Wird es überhaupt noch gebraucht ?
        Rectangle room = new Rectangle(0, 1000, 1200, 100);
        room.setFill(Color.rgb(100,100,100));
        gamePane.getChildren().add(room);
    }

    private void setupWalls(){
        walls = new ArrayList<>();
        
        walls.add(new Wall(100, 200, 30, 80));
        walls.add(new Wall(200, 100, 80, 30));
        walls.add(new Wall(260, 130, 20, 120));
        walls.add(new Wall(0, 990, 1200, 10)); // Unten
        walls.add(new Wall(0, 0, 1200,10)); // Oben
        walls.add(new Wall(0, 0, 10, 1000)); // Links
        walls.add(new Wall(1190, 0, 10, 1000)); // Rechts
    
        for (Wall wall : walls){
          gamePane.getChildren().add(wall.getShape());
        }

    }

    private void setupPlayer() {
        player = new Player(400, 400);
        gamePane.getChildren().add(player.getShape());
        Healthbar healthbar = new Healthbar(50, 1015, player.getHealth(), 0);
        healthbars.add(healthbar);
        gamePane.getChildren().add(healthbar.getBg());
        gamePane.getChildren().add(healthbar.getVg());
        abilityBar = new AbilityBar(400, 1015);
        for (Rectangle slot : abilityBar.getSlots()){
            gamePane.getChildren().add(slot);
        }

        abilityBar.setAbilityIcon(0, "/Fireball_Icon.png");
        abilityBar.setAbilityIcon(1, "/Salve_Icon.png");
        abilityBar.setAbilityIcon(2, "/Wave_Icon.png");
        abilityBar.setAbilityIcon(3, "/Blast_Icon.png");

        for (Canvas icon : abilityBar.getAbilityIcons()){
            gamePane.getChildren().add(icon);
        }

        for (Rectangle overlay : abilityBar.getOverlays()){
            gamePane.getChildren().add(overlay);
        }
    }

    private void setupMonsters() {
        monsters = new ArrayList<>();


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
            if (!again){    // Wenn die Koordinaten in Ordnung sind dann werden Monster erstellt
                int kind = random.nextInt(2)+1;
                Monster monster = new Monster(x, y, i+1, kind);
                monsters.add(monster);
                gamePane.getChildren().add(monster.getShape());
                Healthbar healthbar = new Healthbar(x-20,y-20,monster.getHealth() , i+1);
                healthbars.add(healthbar);
                gamePane.getChildren().add(healthbar.getBg());
                gamePane.getChildren().add(healthbar.getVg());
                System.out.println(healthbar.getId());
            }
        }
    }

    private void setupChest() {
        chests = new ArrayList<>();

        chests.add(new Chest(random.nextInt(700) + 50, random.nextInt(700) + 50));
        for (Chest chest : chests){
            gamePane.getChildren().add(chest.getShape());
            gamePane.getChildren().add(chest.getCanvas());
        }
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
                Projectile projectile = new Projectile(player.getX(), player.getY(), event.getX(), event.getY(), 0);
                projectiles.add(projectile);
                gamePane.getChildren().add(projectile.getShape());
            }
        });
    }

    private void setupKeyDown() {
        gamePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) { // Sicherstellen, dass die Scene existiert
                newScene.setOnKeyPressed(event -> handleKeyPress(event));
            }
        });
    }

    private void handleKeyPress(KeyEvent event){
        System.out.println("Taste gedrückt: " + event.getCode()); // Testausgabe

        switch (event.getCode()) {
            case DIGIT1:
                abilityBar.setActiveSlot(0);
                break;
            case DIGIT2:
                abilityBar.setActiveSlot(1);
                break;
            case DIGIT3:
                abilityBar.setActiveSlot(2);
                break;
            case DIGIT4:
                abilityBar.setActiveSlot(3);
                break;
            case SPACE:
        }
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
                player.update(dTime, walls); // Spielerupdate
                if (healthbars.get(0).getPercantage() <= 40) healthbars.get(0).animate(dTime);  // Wenn das Leben des Spielers unter 40% ist dann wird der Healthbar animiert

                for (Chest chest : chests){
                    if ((player.getX() >= chest.getX() && player.getX() <= chest.getX()+ chest.getSize()) && (player.getY() >= chest.getY() && player.getY() <= chest.getY()+ chest.getSize())){
                        String item = chest.openChest();
                        switch (item){
                            case "Salve":
                                abilityBar.unlockSlot(1);
                                break;
                            case "Wave":
                                abilityBar.unlockSlot(2);
                                break;
                            case "Blast":
                                abilityBar.unlockSlot(3);
                                break;
                            case "HealthPotion":
                                System.out.println("Potion!!!");
                                break;
                        }
                    }
                }

                for (Projectile projectile : projectiles){
                    if (projectile.getSource() == 0) continue;
                    if ((projectile.getX() >= player.X() && projectile.getX() <= player.X()+40) && (projectile.getY() >= player.Y() && projectile.getY() <= player.Y()+40)){
                        player.decHealth(5);
                        for (Healthbar hb : healthbars){
                            if (hb.getId() != 0) continue;
                            hb.decHealth(5);
                        }
                    }
                }
                for (Monster monster : monsters) {
                    if (monster.isAlive()){
                        monster.moveTowards(player.getX(), player.getY(), monsters, walls, dTime);  // Sofern ein Monster am Leben ist bewegt es sich
                        switch (monster.getKind()){     // Monster attackieren je nach Kind of Monster
                            case 2:
                                if (monster.getAttacking() && monster.getAttackCooldown() <= now){
                                    Projectile projectile = new Projectile(monster.getX(), monster.getY(), player.getX(), player.getY(), 1);
                                    projectiles.add(projectile);
                                    gamePane.getChildren().add(projectile.getShape());
                                    monster.setAttackCooldown(now);
                                }
                                break;
                            case 1:
                                break;
                        }
                        for (Healthbar hb : healthbars){
                            if (monster.getId() == hb.getId()) hb.setPos(monster.getX(), monster.getY());   // Der Healthbar bewegt sich mit dem Monster mit
                        }
                        for (Projectile projectile : projectiles){  // Überprüft die Kollision von Projektilen und Monstern
                            if (projectile.getSource() == 1) continue;
                            if ((projectile.getX() >= monster.X() && projectile.getX() <= monster.X()+40) && (projectile.getY() >= monster.Y() && projectile.getY() <= monster.Y()+40) && !projectile.getTargets().contains(monster)) {
                                monster.kill(player.getDamage(), now, player.getKnockback());  // Monster bekommt schaden
                                projectile.setTarget(monster);  // getroffenes Monster wird einer Liste des Projektils hinzugefügt
                                for (Healthbar hb : healthbars){
                                    if (monster.getId() == hb.getId()){
                                        System.out.println("MonsterID: "+monster.getId()+" HealthbarID: "+ hb.getId());
                                        hb.decHealth(player.getDamage());   // reduziert den Healthbar
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
                            for (Healthbar hb : healthbars){
                                if (monster.getId() == hb.getId()){
                                    healthbars.remove(hb);
                                    gamePane.getChildren().remove(hb.getVg());
                                    gamePane.getChildren().remove(hb.getVg());
                                    break;
                                }
                            }
                            return;
                        }
                    }
                }
            }
        };
        gameLoop.start();
    }
}