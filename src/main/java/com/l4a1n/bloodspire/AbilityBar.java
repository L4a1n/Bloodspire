package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class AbilityBar {
    private List<Rectangle> slots;
    private List<Canvas> abilityIcons;
    private List<Rectangle> overlays;
    private List<Integer> slotsOnCooldown;
    private int activeSlot = 0;
    private double w = 70, h = 70;

    public AbilityBar(double x, double y){
        slots = new ArrayList<>();
        abilityIcons = new ArrayList<>();
        overlays = new ArrayList<>();

        for (int i = 0; i < 4; i++){
            slots.add(new Rectangle(x, y, w, h));
            slots.get(i).setFill(Color.LIGHTGRAY);
            slots.get(i).setStroke(Color.rgb(51,0,25));
            slots.get(i).setStrokeWidth(8);
            System.out.println("Added Slot");

            Canvas canvas = new Canvas(w, h);
            canvas.setLayoutX(x);
            canvas.setLayoutY(y);
            abilityIcons.add(canvas);

            Rectangle overlay = new Rectangle(x, y, w, h);
            overlay.setFill(Color.rgb(0,0,0, 0.4));
            overlay.setVisible(true);
            overlays.add(overlay);

            x += w+10;
        }
        setActiveSlot(activeSlot);
    }
    public List<Rectangle> getSlots(){return slots;}
    public  List<Canvas> getAbilityIcons(){return abilityIcons;}
    public List<Rectangle> getOverlays(){return overlays;}

    public void setActiveSlot(int slot){
        for (int s = 0; s < slots.size(); s++){
            slots.get(s).setStroke(Color.rgb(51,0,25));
            overlays.get(s).setVisible(true);
        }
        slots.get(slot).setStroke(Color.rgb(153,0,76));
        overlays.get(slot).setVisible(false);
        activeSlot = slot;
    }

    public void setAbilityIcon(int slot, String imagePath){
        if (slot >= 0 && slot < abilityIcons.size()){
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl == null){
                System.out.println("Image not found: " + imagePath);
                return;
            }

            Image image = new Image(imgUrl.toExternalForm());
            Canvas canvas = abilityIcons.get(slot);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, w, h);
            gc.setImageSmoothing(false);
            gc.drawImage(image, 0, 0, w, h);

        }
    }

}
