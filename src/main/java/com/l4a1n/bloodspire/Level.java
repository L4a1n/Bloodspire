package com.l4a1n.bloodspire;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

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
    private boolean rightMouseDown = false;
    private double mouseX = 0, mouseY = 0;
    private MouseEvent currentMouseEvent = null;

    public Level() {
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: lightgray;");
        gamePane.setFocusTraversable(true);
        random = new Random();

        healthbars = new ArrayList<>();

        setupRoom();
        setupWalls();
        setupSpawns();
        setupChest();
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
        GraphicsContext gc;
        Canvas canvas = new Canvas(1200, 1000);
        Image floor = new Image(getClass().getResource("/Floor.png").toExternalForm());
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,1200,1000);
        gc.setImageSmoothing(false);
        gc.drawImage(floor, 0,0,1200,1000);
        gamePane.getChildren().add(canvas);
    }

    private void setupWalls(){
        walls = new ArrayList<>();

        walls.add(new Wall(378, 499, 243, 130));
        walls.add(new Wall(0, 990, 1200, 10)); // Unten
        walls.add(new Wall(0, 0, 1200,40)); // Oben
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
        for (Rectangle cooldownOverlay : abilityBar.getCooldownOverlays()){
            gamePane.getChildren().add(cooldownOverlay);
        }
    }

    private void setupSpawns(){
        spawnareas = new ArrayList<>();
        monsters = new ArrayList<>();
        Spawnarea spawn = new Spawnarea(800, 200);
        spawnareas.add(spawn);
        gamePane.getChildren().addAll(spawn.getShape(), spawn.getCanvas());
        Spawnarea spawn2 = new Spawnarea(200, 800);
        spawnareas.add(spawn2);
        gamePane.getChildren().addAll(spawn2.getShape(), spawn2.getCanvas());
    }

    private void setupChest() {
        chests = new ArrayList<>();

        chests.add(new Chest(random.nextInt(700) + 50, random.nextInt(700) + 50));
        for (Chest chest : chests){
            gamePane.getChildren().add(chest);
        }
    }

    private void setupMouseClick() {
        // Wenn die Maus gezogen wird...
        gamePane.setOnMouseMoved((MouseEvent event) ->{
            currentMouseEvent = event;
        });
        gamePane.setOnMouseDragged((MouseEvent event) -> {
            currentMouseEvent = event;
            // Wenn es die Linke Maustaste ist
            if (event.getButton() == MouseButton.PRIMARY){
                // Setzt die Koordinaten der Maus zu Zielkoordinaten des Spielers
                double targetX = event.getX();
                double targetY = event.getY();
                player.setTarget(targetX, targetY); // Zielposition setzen
            }

        });
        // Wenn die Maus nur geclickt wird
        gamePane.setOnMousePressed((MouseEvent event) -> {
            currentMouseEvent = event;
            // Wenn es die Linke Maustaste ist
            if (event.getButton() == MouseButton.PRIMARY){
                // Setzt die Koordinaten der Maus zu Zielkoordinaten des Spielers
                double targetX = event.getX();
                double targetY = event.getY();
                player.setTarget(targetX, targetY); // Zielposition setzen
            }
            if (event.getButton() == MouseButton.SECONDARY){
                rightMouseDown = true;
            }
        });
        gamePane.setOnMouseReleased((MouseEvent event) -> {
            if (event.getButton() == MouseButton.SECONDARY){
                rightMouseDown = false;
            }
        });
        // Wenn der Spieler auf das Monster klickt wird es angegriffen
    }

    private void setupKeyDown() {
        gamePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) { // Sicherstellen, dass die Scene existiert
                newScene.setOnKeyPressed(this::handleKeyPress);
            }
        });
    }

    private void handleKeyPress(KeyEvent event){
        switch (event.getCode()) {
            case DIGIT1:
                if (!abilityBar.getLockedSlots(0)){
                    abilityBar.setActiveSlot(0);
                    player.setCurrentAbility(0);
                    player.setDamage(20);
                }
                break;
            case DIGIT2:
                if (!abilityBar.getLockedSlots(1)){
                    abilityBar.setActiveSlot(1);
                    player.setCurrentAbility(1);
                    player.setDamage(50);
                }
                break;
            case DIGIT3:
                if (!abilityBar.getLockedSlots(2)){
                    abilityBar.setActiveSlot(2);
                    player.setCurrentAbility(2);
                }
                break;
            case DIGIT4:
                if (!abilityBar.getLockedSlots(3)){
                    abilityBar.setActiveSlot(3);
                    player.setCurrentAbility(3);
                }
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

                // Rechnet DeltaTime aus (die Zeit die Seit dem letzen Frame vergangen ist)
                // Wird benutzt um die Geschwindigkeit anzupassen, sodass die Bewwegung Framerate-unabhängig ist
                double dTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                if (currentMouseEvent != null) {
                    mouseX = currentMouseEvent.getX();
                    mouseY = currentMouseEvent.getY();
                }

                if (rightMouseDown){
                    switch (player.getCurrentAbility()){
                        case 0:
                            if (abilityBar.getSlotCooldown(0) == 0L){
                                Projectile projectile = new Projectile(player.getX(), player.getY(), mouseX, mouseY, 0, player.getCurrentAbility(), player.getDamage());
                                abilityBar.setSlotCooldown(0, 300000000L);
                                projectiles.add(projectile);
                                gamePane.getChildren().add(projectile.getShape());
                            }
                            break;
                        case 1:
                            if (abilityBar.getSlotCooldown(1) == 0L){
                                player.fireSalve(projectiles, getGamePane(), player.getX(), player.getY(), mouseX, mouseY);
                                abilityBar.setSlotCooldown(1, 1000000000L);
                            }
                            break;
                    }
                }


                abilityBar.animate(dTime);

                for (Spawnarea spawn : spawnareas){
                    spawn.animate(dTime);
                    if (spawn.spawnMonster(now)){
                        if (monsters.isEmpty()){
                            Monster monster = new Monster(spawn.getX()+ random.nextDouble(spawn.getSize()), spawn.getY()+ random.nextDouble(spawn.getSize()), 1, random.nextInt(2)+1);
                            monsters.add(monster);
                            gamePane.getChildren().addAll(monster.getShape(), monster.getCanvas());
                            Healthbar healthbar = new Healthbar(monster.getX()-20, monster.getY()-20, monster.getHealth(), 1);
                            healthbars.add(healthbar);
                            gamePane.getChildren().add(healthbar.getBg());
                            gamePane.getChildren().add(healthbar.getVg());
                        }
                        else {
                            Monster monster = new Monster(spawn.getX()+ random.nextDouble(spawn.getSize()), spawn.getY()+ random.nextDouble(spawn.getSize()), monsters.get(monsters.size()-1).getId()+1, random.nextInt(2)+1);
                            monsters.add(monster);
                            gamePane.getChildren().addAll(monster.getShape(), monster.getCanvas());
                            Healthbar healthbar = new Healthbar(monster.getX()-20, monster.getY()-20, monster.getHealth(), monsters.get(monsters.size()-1).getId());
                            healthbars.add(healthbar);
                            gamePane.getChildren().add(healthbar.getBg());
                            gamePane.getChildren().add(healthbar.getVg());
                        }
                    }
                }

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

                player.update(dTime, walls); // Spieler-Update
                healthbars.get(0).animate(dTime);   // Spieler-Healthbar animation

                for (Chest chest : chests){
                    //                   if ((player.getX() >= chest.getX() && player.getX() <= chest.getX()+ chest.getSize()) && (player.getY() >= chest.getY() && player.getY() <= chest.getY()+ chest.getSize()) && chest.getAccessible()){
                    Shape intersection = Shape.intersect(player.getShape(), chest.getShape());
                    System.out.println("Test");
                    if (!intersection.getBoundsInLocal().isEmpty() && chest.getAccessible()){
                        String item = chest.openChest();
                        switch (item){
                            case "Salve":
                                abilityBar.unlockSlot(1);
                                chest.setUsed(now);
                                chest.setInaccessible();
                                break;
                            case "Wave":
                                abilityBar.unlockSlot(2);
                                chest.setUsed(now);
                                chest.setInaccessible();
                                break;
                            case "Blast":
                                abilityBar.unlockSlot(3);
                                chest.setUsed(now);
                                chest.setInaccessible();
                                break;
                            case "HealthPotion":
                                if (!chest.getUsed()){
                                    System.out.println("Potion!!!");
                                    player.heal(300);
                                    for (Healthbar hb : healthbars){
                                        if (hb.getId() != 0) continue;
                                        hb.incHealth(300);
                                    }
                                }
                                chest.setUsed(now);
                                chest.setInaccessible();
                                break;
                        }
                    }
                    if (chest.getTimeSinceUsed() < now && chest.getUsed()){
                        gamePane.getChildren().remove(chest);
                        chests.remove(chest);
                        break;
                    }
                }

                for (Projectile projectile : projectiles){
                    if (projectile.getSource() == 0) continue;
                    Shape intersection = Shape.intersect(player.getShape(), projectile.getShape());
                    if (!intersection.getBoundsInLocal().isEmpty() && !projectile.getPlayerHit()){
                        player.decHealth(projectile.getDamgage());
                        System.out.println(player.getHealth());
                        for (Healthbar hb : healthbars){
                            if (hb.getId() != 0) continue;
                            hb.decHealth(projectile.getDamgage());
                            break;
                        }
                        projectile.setPlayerHit();
                    }
                }
                for (Monster monster : monsters) {
                    if (monster.isAlive()){
                        monster.moveTowards(player.getX(), player.getY(), monsters, walls, dTime);  // Sofern ein Monster am Leben ist bewegt es sich
                        switch (monster.getKind()){     // Monster attackieren je nach Kind of Monster
                            case 2:
                                monster.animate(dTime);
                                if (monster.getAttacking() && monster.getAttackCooldown() <= now){
                                    Projectile projectile = new Projectile(monster.getX(), monster.getY(), player.getX(), player.getY(), 1, 0, monster.getDamage());
                                    projectiles.add(projectile);
                                    gamePane.getChildren().add(projectile.getShape());
                                    monster.setAttackCooldown(now);
                                }
                                break;
                            case 1:
                                if (monster.getAttacking() && monster.getAttackCooldown() <= now) {
                                    player.decHealth(monster.getDamage());
                                    for (Healthbar hb : healthbars) {
                                        if (hb.getId() != 0) continue;
                                        hb.decHealth(monster.getDamage());
                                        break;
                                    }
                                    monster.setAttackCooldown(now);
                                }
                                break;
                        }
                        for (Healthbar hb : healthbars){
                            if (monster.getId() == hb.getId()) hb.setPos(monster.getX(), monster.getY());   // Der Healthbar bewegt sich mit dem Monster mit
                        }
                        for (Projectile projectile : projectiles){  // Überprüft die Kollision von Projektilen und Monstern
                            if (projectile.getSource() == 1) continue;
                            Shape intersection = Shape.intersect(monster.getShape(), projectile.getShape());
                            if (!intersection.getBoundsInLocal().isEmpty() && !projectile.getTargets().contains(monster)) {
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
                                gamePane.getChildren().add(chest);
                            }
                        }
                        if (monster.getDeadSince() <= now){
                            gamePane.getChildren().remove(monster.getShape());
                            gamePane.getChildren().remove(monster.getCanvas());
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