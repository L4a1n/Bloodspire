package com.l4a1n.bloodspire;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    private MainMenu menu;
    private AnimationTimer gameLoop;
    private LevelUp levelUp;
    private Pane gamePane;
    private List<Wall> walls;
    private Player player;
    private AbilityBar abilityBar;
    private XPBar xpBar;
    private Animation IntroAnimation;
    private Animation IntroAnimation2;
    private Animation GameOverAnimation;
    private Rectangle GameOverBackground;
    private double GameOverBackgroundVisibility;
    private List<Monster> monsters = new ArrayList<>();
    private List<Healthbar> healthbars;
    private List<Projectile> projectiles = new ArrayList<>();
    private List<Chest> chests;
    private List<Spawnarea> spawnareas = new ArrayList<>();
    private Random random;
    private boolean rightMouseDown = false;
    private double mouseX = 0, mouseY = 0;
    public MouseEvent currentMouseEvent = null;
    private boolean tutorialDone = false;
    private boolean mouseMoved = false;
    private boolean mouseFired = false;
    private boolean abilityUsed = false;
    private double passedTime = 59;
    private double timeUntilNextWave = 60;
    private double passedTimeSinceGameover = 0;
    private boolean paused = false;
    private long lastUpdate = 0;
    private int playerLevel;
    public boolean ignoreNextClick = false;
    private AudioClip projectileShot;
    private AudioClip projectileImpact;
    private AudioClip waveShot;
    private AudioClip waveImpact;
    private AudioClip playerImpact;
    private List<AudioClip> soundEffects = new ArrayList<>();

    public Level() {
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: darkgrey;");
        gamePane.setFocusTraversable(true);
        random = new Random();

        healthbars = new ArrayList<>();


        menu = new MainMenu();
        menu.setLevel(this);
        menu.setGamePane(gamePane);

        levelUp = new LevelUp();
        levelUp.setLevel(this);
        levelUp.setGamePane(gamePane);
        levelUp.setPlayer(player);

        menu.run();

        gamePane.requestFocus();

    }

    public Pane getGamePane() {return gamePane;}
    public AnimationTimer getGameLoop(){return gameLoop;}

    private void setTopLevel(Node node){
        gamePane.getChildren().remove(node);
        gamePane.getChildren().add(node);
    }
    public void setPaused(boolean paused){this.paused = paused;}

    public void setupSoundEffects(){
        projectileShot = new AudioClip(getClass().getResource("/sounds/ProjectileShot.wav").toExternalForm());
        soundEffects.add(projectileShot);
        projectileImpact = new AudioClip(getClass().getResource("/sounds/ProjectileImpact.wav").toExternalForm());
        soundEffects.add(projectileImpact);
        waveShot = new AudioClip(getClass().getResource("/sounds/WaveShot.wav").toExternalForm());
        soundEffects.add(waveShot);
        waveImpact = new AudioClip(getClass().getResource("/sounds/WaveImpact.wav").toExternalForm());
        soundEffects.add(waveImpact);
        playerImpact = new AudioClip(getClass().getResource("/sounds/PlayerImpact.wav").toExternalForm());
        soundEffects.add(playerImpact);


    }

    public void setEffectVolume(double amount){
        for (AudioClip a : soundEffects){
            a.setVolume(amount);
        }
    }

    public void setupIntro(){
        Image spritesheet = new Image(getClass().getResource("/Mouse_Intro.png").toExternalForm());
        IntroAnimation = new Animation(spritesheet, 640, 250, 150, 150, 33, 32, 2, 4);
        gamePane.getChildren().add(IntroAnimation.getCanvas());
        Image spritesheet2 = new Image(getClass().getResource("/Ability_Intro.png").toExternalForm());
        IntroAnimation2 = new Animation(spritesheet2, 440, 250, 400, 100, 128, 32, 8, 2);
        gamePane.getChildren().add(IntroAnimation2.getCanvas());

    }

    public void setupGameOver(){
        Image GOspritesheet = new Image(getClass().getResource("/GameOver_Sprite.png").toExternalForm());
        GameOverAnimation = new Animation(GOspritesheet, 340, 250, 600, 300, 128, 64, 13, 4);
        gamePane.getChildren().add(GameOverAnimation.getCanvas());
        GameOverBackground = new Rectangle(0, 0, 1280, 720);
        GameOverBackground.setFill(Color.rgb(0, 0, 0, GameOverBackgroundVisibility));
        GameOverBackground.setVisible(false);
        gamePane.getChildren().add(GameOverBackground);
    }

    public void setupRoom() {
        GraphicsContext gc;
        Canvas canvas = new Canvas(1280, 720);
        Image floor = new Image(getClass().getResource("/Floor.png").toExternalForm());
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,1280,720);
        gc.setImageSmoothing(false);
        gc.drawImage(floor, 0,0,1280,720);
        gamePane.getChildren().add(canvas);
    }

    public void setupWalls(){
        walls = new ArrayList<>();

        walls.add(new Wall(325, 307, 21, 58));
        walls.add(new Wall(869, 210, 86, 23));
        walls.add(new Wall(0, 720, 1280, 10)); // Unten
        walls.add(new Wall(0, 0, 1280,20)); // Oben
        walls.add(new Wall(0, 0, 4, 720)); // Links
        walls.add(new Wall(1276, 0, 4, 720)); // Rechts

        for (Wall wall : walls){
            gamePane.getChildren().add(wall.getShape());
        }

    }

    public void setupPlayer() {
        Image abilityBackground = new Image(getClass().getResource("/AbilityBar_Background.png").toExternalForm());
        Canvas canvas = new Canvas(1280, 100);
        canvas.setLayoutX(0);
        canvas.setLayoutY(720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0,0,1280, 100);
        gc.drawImage(abilityBackground, 0, 0, 409, 32, 0, 0, 1280, 100);
        gamePane.getChildren().add(canvas);

        player = new Player(400, 400);
        gamePane.getChildren().add(player.getShape());

        Healthbar healthbar = new Healthbar(50, 740, player.getHealth(), 0);
        player.setHealthbar(healthbar);
        healthbars.add(healthbar);
        gamePane.getChildren().add(healthbar.getBg());
        gamePane.getChildren().add(healthbar.getVg());
        gamePane.getChildren().add(healthbar.getHealthText());

        abilityBar = new AbilityBar(400, 745);
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
        xpBar = new XPBar(player);
        gamePane.getChildren().addAll(xpBar.getBg(), xpBar.getVg());
    }

    public void setupSpawns(){
        for (int i = 0; i < 1; i++) {
            boolean again = false;
            double x = random.nextDouble(1180) + 70;
            double y = random.nextDouble(630) + 70;
            for (Wall wall : walls) {
                if (x + 60 >= wall.getX() && x - 60 <= wall.getX() + wall.getW() && y + 60 >= wall.getY() && y - 60 <= wall.getY() + wall.getH()) {     // Checkt ob die Spawn-Koordinaten auf den Wänden liegen damit die Spawns nicht auf den Wänden spawnen
                    System.out.println("Oh oh");
                    i -= 1;     // Wenn ja, dann reduziert es den Iterations-Zähler um 1 und unterbricht den for-loop
                    again = true;
                    break;
                }
            }
            if (!again) {    // Wenn die Koordinaten in Ordnung sind dann werden Spawns erstellt
                spawnareas.add(new Spawnarea(x, y));
                gamePane.getChildren().addAll(spawnareas.get(spawnareas.size()-1).getShape(), spawnareas.get(spawnareas.size()-1).getCanvas());
            }
        }
    }

    public void setupChest() {
        chests = new ArrayList<>();

        chests.add(new Chest(random.nextInt(700) + 50, random.nextInt(700) + 50, player));
        for (Chest chest : chests){
            gamePane.getChildren().add(chest);
        }
    }

    public void setupMouseClick() {
        gamePane.setOnMouseMoved((MouseEvent event) ->{
            currentMouseEvent = event;
        });
        // Wenn die Maus gezogen wird...
        gamePane.setOnMouseDragged((MouseEvent event) -> {
            currentMouseEvent = event;
            // Wenn es die Linke Maustaste ist
            if (!paused && event.getButton() == MouseButton.PRIMARY){
                // Setzt die Koordinaten der Maus zu Zielkoordinaten des Spielers
                double targetX = event.getX();
                double targetY = event.getY();
                player.setTarget(targetX, targetY); // Zielposition setzen
            }

        });
        // Wenn die Maus nur gedrückt wird
        gamePane.setOnMousePressed((MouseEvent event) -> {
            currentMouseEvent = event;
            // Wenn es die Linke Maustaste ist
            if (!paused && event.getButton() == MouseButton.PRIMARY){
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

    public void setupKeyDown() {
        Scene scene = gamePane.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(this::handleKeyPress);
            gamePane.setFocusTraversable(true); // Make it focusable
            gamePane.requestFocus();           // Request focus for key input
        } else {
            // If the scene isn't ready yet, listen for it
            gamePane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.setOnKeyPressed(this::handleKeyPress);
                    gamePane.setFocusTraversable(true);
                    gamePane.requestFocus();
                }
            });
        }
    }

    public void handleKeyPress(KeyEvent event){
        switch (event.getCode()) {
            case DIGIT1:
                if (!abilityBar.getLockedSlots(0)){
                    abilityBar.setActiveSlot(0);
                    player.setCurrentAbility(0);
                }
                break;
            case DIGIT2:
                if (!abilityBar.getLockedSlots(1)){
                    abilityBar.setActiveSlot(1);
                    player.setCurrentAbility(1);
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
        }
    }

    public void resetLevel(){
        System.out.println("Resetting Level");
        monsters.clear();
        chests.clear();
        projectiles.clear();
        spawnareas.clear();
        player.reset();
        healthbars.get(0).reset(player);
        healthbars.clear();

        gamePane.getChildren().clear();

        GameOverBackgroundVisibility = 0;
        passedTimeSinceGameover = 0;
        paused = false;
        passedTime = 50;
        timeUntilNextWave = 60;
    }

    public void startGameLoop() {
        playerLevel = player.getLevel();
        setEffectVolume(0.2);
        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (lastUpdate == 0 || paused){
                    lastUpdate = now;
                    return;
                }

                // Rechnet DeltaTime aus (die Zeit die Seit dem letzen Frame vergangen ist)
                // Wird benutzt um die Geschwindigkeit anzupassen, sodass die Bewwegung Framerate-unabhängig ist
                double dTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                levelUp.setPlayer(player);

                if (currentMouseEvent != null) {
                    mouseX = currentMouseEvent.getX();
                    mouseY = currentMouseEvent.getY();
                }

                if (rightMouseDown && player.getHealth() > 0){
                    switch (player.getCurrentAbility()){
                        case 0:
                            if (abilityBar.getSlotCooldown(0) == 0L){
                                Projectile projectile = new Projectile(player.getX(), player.getY(), mouseX, mouseY, 0, player.getCurrentAbility(), player.getDamage());
                                projectileShot.play();
                                abilityBar.setSlotCooldown(0, (long) (500000000L * player.getCooldown()));
                                projectiles.add(projectile);
                                gamePane.getChildren().add(projectile.getShape());
                            }
                            break;
                        case 1:
                            if (abilityBar.getSlotCooldown(1) == 0L){
                                player.fireSalve(projectiles, getGamePane(), player.getX(), player.getY(), mouseX, mouseY);
                                projectileShot.play();
                                abilityBar.setSlotCooldown(1, (long) (1000000000L * player.getCooldown()));
                            }
                            break;
                        case 2:
                            if (abilityBar.getSlotCooldown(2) == 0L){
                                Projectile projectile = new Projectile(player.getX(), player.getY(), mouseX, mouseY, 0, player.getCurrentAbility(), player.getDamage());
                                waveShot.play();
                                abilityBar.setSlotCooldown(2, (long) (1000000000L * player.getCooldown()));
                                projectiles.add(projectile);
                                gamePane.getChildren().add(projectile.getSickle());
                            }
                    }
                }

                abilityBar.animate(dTime);

                if (!mouseMoved && currentMouseEvent != null){
                    IntroAnimation.animate(dTime);
                    if (currentMouseEvent.getButton() == MouseButton.PRIMARY) {
                        IntroAnimation.changeRow(1);
                        mouseMoved = true;
                    }
                }
                if (mouseMoved && !mouseFired){
                    IntroAnimation.animate(dTime);
                    if (currentMouseEvent.getButton() == MouseButton.SECONDARY){
                        mouseFired = true;
                        tutorialDone = true;
                        gamePane.getChildren().remove(IntroAnimation.getCanvas());
                    }
                }
                if (!abilityBar.getLockedSlots(1) || !abilityBar.getLockedSlots(2) && !abilityUsed){
                    IntroAnimation2.animate(dTime);
                    if (player.getCurrentAbility() == 1 || player.getCurrentAbility() == 2){
                        abilityUsed = true;
                        gamePane.getChildren().remove(IntroAnimation2.getCanvas());
                    }
                }

                if (player.getHealth() > 0){
                    player.update(dTime, walls); // Spieler-Update
                }
                healthbars.get(0).animate(dTime);   // Spieler-Healthbar Animation
                if (tutorialDone){
                    passedTime += dTime;
                    if (passedTime >= timeUntilNextWave){
                        setupSpawns();
                        passedTime = 0;
                        if (timeUntilNextWave > 20) timeUntilNextWave -= 5;
                    }
                    for (Spawnarea spawn : spawnareas){
                        if (spawnareas.isEmpty()) break;
                        spawn.animate(dTime);
                        if (spawn.hasEnded(dTime)){
                            Chest chest = new Chest(spawn.getX(), spawn.getY(), player);
                            chests.add(chest);
                            gamePane.getChildren().add(chest);
                            spawnareas.remove(spawn);
                            gamePane.getChildren().remove(spawn.getCanvas());
                            break;
                        }
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
                }
                if (player.getHealth() <= 0){
                    GameOverBackground.setVisible(true);
                    passedTimeSinceGameover += dTime;
                    if (GameOverBackgroundVisibility <= 0.7){
                        if (passedTimeSinceGameover > 0.2){
                            GameOverBackground.setFill(Color.rgb(0,0,0, GameOverBackgroundVisibility));
                            GameOverBackgroundVisibility += 0.1;
                            passedTimeSinceGameover = 0;
                        }

                    }
                    else {
                        if (GameOverAnimation.getCurrentFrame() != 12){
                            GameOverAnimation.animate(dTime);
                        }
                        passedTimeSinceGameover += dTime;
                        if (passedTimeSinceGameover > 10){
                            gameLoop.stop();
                            resetLevel();
                            Platform.runLater(()-> menu.run());
                        }
                    }
                }

                for (Projectile projectile : projectiles){
                    if (projectile.getAliveUntil() == 0){   // Wenn die Zeit 0 ist dann wird die aktuelle Zeit gesetzt
                        projectile.setAliveUntil(now);
                        continue;
                    }
                    if (projectile.getAliveUntil() <= now){     // Wenn die Zeit zu leben überschritten ist dann wird es gelöscht
                        if (projectile.getKind() == 2) gamePane.getChildren().remove(projectile.getSickle());
                        else gamePane.getChildren().remove(projectile.getShape());
                        projectiles.remove(projectile);
                        return;
                    }
                    projectile.update(dTime, walls);
                }

                for (Chest chest : chests){
                    //                   if ((player.getX() >= chest.getX() && player.getX() <= chest.getX()+ chest.getSize()) && (player.getY() >= chest.getY() && player.getY() <= chest.getY()+ chest.getSize()) && chest.getAccessible()){
                    Shape intersection = Shape.intersect(player.getShape(), chest.getShape());
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
                        playerImpact.play();
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
                                    playerImpact.play();
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
                            Shape projShape = projectile.getKind() == 2 ? projectile.getSickle() : projectile.getShape();
                            if (projShape == null) continue; // extra safety
                            Shape intersection = Shape.intersect(monster.getShape(), projShape);
                            if (!intersection.getBoundsInLocal().isEmpty() && !projectile.getTargets().contains(monster)) {
                                monster.kill(player.getDamage(), now, player.getKnockback());  // Monster bekommt schaden
                                projectile.setTarget(monster);  // getroffenes Monster wird einer Liste des Projektils hinzugefügt
                                projectileImpact.play();
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
                        if (!monster.getReturnedXP()) xpBar.increaseXP(monster.getXp());
                        if (!monster.getDroppedLoot()){
                            monster.setDroppedLoot();
                            // Might introduce Item drops ??
                        }
                        if (monster.getDeadSince() <= now){
                            for (Healthbar hb : healthbars){
                                if (monster.getId() == hb.getId()){
                                    gamePane.getChildren().remove(hb.getVg());
                                    gamePane.getChildren().remove(hb.getBg());
                                    healthbars.remove(hb);
                                    break;
                                }
                            }
                            gamePane.getChildren().remove(monster.getShape());
                            gamePane.getChildren().remove(monster.getCanvas());
                            monsters.remove(monster);

                            return;
                        }
                    }
                }
                setTopLevel(player.getShape());
                setTopLevel(GameOverBackground);
                setTopLevel(GameOverAnimation.getCanvas());

                if (player.getLevel() > playerLevel){
                    playerLevel = player.getLevel();
                    levelUp.run();
                }
            }
        };
        gameLoop.start();
    }
}