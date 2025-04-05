package com.l4a1n.bloodspire;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class XPBar {
    private Rectangle bg;
    private Rectangle vg;
    private Player player;

    private double width = 1180;
    private double height = 14;
    private double margin = 20; // distance from bottom

    public XPBar(Player player){
        bg = new Rectangle(0, 0, width, height);
        vg = new Rectangle(0, 0, 0, height);

        bg.setFill(Color.rgb(51, 0, 25));
        vg.setFill(Color.rgb(204, 0, 102));

        this.player = player;
    }

    public Rectangle getBg(){ return bg; }
    public Rectangle getVg(){ return vg; }

    public void increaseXP(int xp){
        vg.setWidth(vg.getWidth() + xp);
        System.out.println(vg.getWidth() >= bg.getWidth());
        if (vg.getWidth() >= bg.getWidth()){
            player.increaseLevel();
            vg.setWidth(0);
        }
    }

    // Call this whenever the window size changes or on update
    public void updatePosition(double sceneWidth, double sceneHeight){
        double x = (sceneWidth - width) / 2.0;
        double y = sceneHeight - height - margin;

        bg.setX(x);
        bg.setY(y);
        vg.setX(x);
        vg.setY(y);
    }
}
