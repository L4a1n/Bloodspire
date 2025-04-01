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
    private List<Healthbar> healthbars;
    private List<Projectile> projectiles = new ArrayList<>();
    private List<Chest> chests;
    private List<Spawnarea> spawnareas;
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
        setupSpawns();
        setupPlayer();
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

    private void setupSpawns(){
        spawnareas = new ArrayList<>();
        monsters = new ArrayList<>();
        Spawnarea spawn = new Spawnarea(600, 600);
        spawnareas.add(spawn);
        gamePane.getChildren().add(spawn.getShape());
        Spawnarea spawn2 = new Spawnarea(200, 800);
        spawnareas.add(spawn2);
        gamePane.getChildren().add(spawn2.getShape());
    }

    private void setupChest() {
        chests = new ArrayList<>();

        chests.add(new Chest(random.nextInt(700) + 50, random.nextInt(700) + 50));
        for (Chest chest : chests){
            gamePane.getChildren().add(chest.getShape());
            gamePane.getChildren().add(chest.getCanvas());
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
                //if (monsters.isEmpty()){
                //    setupMonsters();
                //}
                if (lastUpdate == 0){
                    lastUpdate = now;
                    return;
                }

                for (Spawnarea spawn : spawnareas){
                    if (spawn.spawnMonster(now)){
                        if (monsters.isEmpty()){
                            Monster monster = new Monster(spawn.getX()+ random.nextDouble(spawn.getSize()), spawn.getY()+ random.nextDouble(spawn.getSize()), 1, random.nextInt(3));
                            monsters.add(monster);
                            gamePane.getChildren().add(monster.getShape());
                            Healthbar healthbar = new Healthbar(monster.getX()-20, monster.getY()-20, monster.getHealth(), 1);
                            healthbars.add(healthbar);
                            gamePane.getChildren().add(healthbar.getBg());
                            gamePane.getChildren().add(healthbar.getVg());
                            System.out.println(monsters.get(monsters.size()-1).getId());
                        }
                        else {
                            Monster monster = new Monster(spawn.getX()+ random.nextDouble(spawn.getSize()), spawn.getY()+ random.nextDouble(spawn.getSize()), monsters.get(monsters.size()-1).getId()+1, random.nextInt(3));
                            monsters.add(monster);
                            gamePane.getChildren().add(monster.getShape());
                            Healthbar healthbar = new Healthbar(monster.getX()-20, monster.getY()-20, monster.getHealth(), monsters.get(monsters.size()-1).getId());
                            healthbars.add(healthbar);
                            gamePane.getChildren().add(healthbar.getBg());
                            gamePane.getChildren().add(healthbar.getVg());
                            System.out.println(monsters.get(monsters.size()-1).getId());
                        }
                    }
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
                                chest.setUsed();
                                break;
                            case "Wave":
                                abilityBar.unlockSlot(2);
                                chest.setUsed();
                                break;
                            case "Blast":
                                abilityBar.unlockSlot(3);
                                chest.setUsed();
                                break;
                            case "HealthPotion":
                                if (!chest.getUsed()){
                                    System.out.println("Potion!!!");
                                    player.heal(100);
                                    for (Healthbar hb : healthbars){
                                        if (hb.getId() != 0) continue;
                                        hb.incHealth(100);
                                    }
                                }
                                chest.setUsed();
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
                                        hb.decHealth(player.getDamage());   // reduziert den Healthbar
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (!monster.getDroppedLoot()){
                            monster.setDroppedLoot();
                            if (random.nextInt(10) == 1){
                                Chest chest = new Chest(monster.getX(), monster.getY());
                                chests.add(chest);
                                gamePane.getChildren().add(chest.getCanvas());
                            }
                        }
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