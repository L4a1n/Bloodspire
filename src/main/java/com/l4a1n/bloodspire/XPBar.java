package com.l4a1n.bloodspire;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class XPBar {
    private Rectangle bg;
    private Rectangle vg;
    private Player player;

    private double width = 1220;
    private double height = 12;

    public XPBar(Player player){
        bg = new Rectangle(30, 725, width, height);
        vg = new Rectangle(30, 725, 0, height);

        bg.setFill(Color.rgb(51, 0, 25));
        vg.setFill(Color.rgb(204, 0, 102));
        bg.setArcWidth(12);
        bg.setArcHeight(12);
        vg.setArcWidth(12);
        vg.setArcHeight(12);

        this.player = player;
    }

    public Rectangle getBg(){ return bg; }
    public Rectangle getVg(){ return vg; }

    public void increaseXP(int xp){
        vg.setWidth(vg.getWidth() + xp);
        if (vg.getWidth() >= bg.getWidth()){
            player.increaseLevel();
            vg.setWidth(0);
        }
    }
}
