package com.l4a1n.bloodspire;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Healthbar {
    private Rectangle bg;
    private Rectangle vg;
    private double w;
    private double h;
    private int id;
    private int health;
    private int maxHealth;
    private int animCount;
    private int percantage = 100;
    private int colorG = 0;
    private int colorB = 0;
    private Label healthText;

    public Healthbar(double x, double y, int health, int id){
        this.id = id;
        this.health = health;
        maxHealth = health;
        if (id == 0){       // Der Spieler hat die ID 0, daher die Variablen angepasst auf den Spieler Healthbar
            w = 300;
            h = 70;
            animCount = 0;

            Font textFont = Font.loadFont(getClass().getResourceAsStream("/joystix monospace.otf"), 14);
            healthText = new Label("");
            healthText.setFont(textFont);
            healthText.setTextFill(Color.WHITE);
            healthText.setLayoutX(x + 20);
            healthText.setLayoutY(y + 30);
            healthText.setPrefSize(300, 30);
            healthText.setAlignment(Pos.BASELINE_LEFT);

        }
        else {
            w = 50;
            h = 5;
        }
        bg = new Rectangle(w,h, Color.BLACK);
        bg.setX(x);
        bg.setY(y);
        vg = new Rectangle(w,h, Color.rgb(240, 0,0));
        vg.setX(x);
        vg.setY(y);
    }

    public Label getHealthText(){return healthText;}
    public Rectangle getBg(){return bg;}                            // return Hintergrund Rectangle
    public Rectangle getVg(){return vg;}                            // return Vordergrund Rectangle
    public int getId(){return id;}                                  // return ID, welche beim Erstellen vergeben wird. Wird benutzt, um die Healthbars den Entitäten zuzuordnen
    public int getPercantage(){return percantage;}                  // return die Prozentanzahl des übrigen Lebens
    public int getHealth(){return health;}
    public void setNewHealth(int health){this.health = health; maxHealth = health;}     // Wenn das maximale Leben des Spielers sich verändert muss es auch hier verändert werden

    public void reset(Player player){
        health = player.getHealth();
        vg.setWidth(bg.getWidth());
        percantage = (int)Math.round(vg.getWidth()/bg.getWidth()*100);
    }

    // Animation für niedriges Leben
    public void animate(double dTime){
        if (percantage <= 40){              // Wenn die Prozentzahl unter oder gleich 40 ist, dann wird die Animation ausgeführt
            if (animCount < 120){           // Zählt einen Animationscounter hoch und erhöht ebenfalls die Farben
                animCount ++;
                colorG ++;
                colorB ++;
                vg.setFill(Color.rgb(240, colorG, colorB));
            }
            else{       // Sobald das Ziel erreicht ist, wird alles wieder auf null gesetzt und der Kreislauf beginnt erneut
                animCount = 0;
                colorB = 0;
                colorG = 0;
            }
        }
        else vg.setFill(Color.rgb(240, 0, 0));      // Andernfalls wird die Farbe wieder auf die ursprüngliche Farbe zurückgesetzt
        healthText.setText(health+ " / "+maxHealth);
    }

    public void setPos(double newX, double newY){           // Passt die Position des Healthbars an
        bg.setX(newX-25);
        bg.setY(newY-25);
        vg.setX(newX-25);
        vg.setY(newY-25);
    }
    public void decHealth(double damage){
        health -= (int) damage;
        if (health <= 0) health = 0;
        vg.setWidth(vg.getWidth()-(bg.getWidth()*(damage/(double)maxHealth)));         // Vermindert den Healthbar, um wie viel schaden angegeben wird
        percantage = (int)Math.round(vg.getWidth()/bg.getWidth()*100);      // Passt die neue Prozentzahl an
        if (vg.getWidth() <= 0 && id != 0) {
            bg.setFill(Color.TRANSPARENT);              // Sofern das gesamte Leben verloren wurde, wird der Healthbar unsichtbar gemacht
        }
    }
    public void incHealth(double amount){
        if (vg.getWidth() < bg.getWidth()){
            health += (int) amount;
            if (health >= maxHealth) health = maxHealth;
            vg.setWidth(vg.getWidth()+(bg.getWidth()*(amount/maxHealth)));         // NYA
            if (vg.getWidth() > bg.getWidth()) vg.setWidth(bg.getWidth());      // Checkt, ob zu viel geheilt wurde und falls ja, dann wird der Balken wieder angepasst
            percantage = (int)Math.round(vg.getWidth()/bg.getWidth()*100);      // Passt die neue Prozentzahl an
        }
    }
}
