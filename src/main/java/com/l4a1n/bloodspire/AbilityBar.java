package com.l4a1n.bloodspire;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.effect.ColorAdjust;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class AbilityBar {
    private List<Rectangle> slots;
    private List<Canvas> abilityIcons;
    private List<Rectangle> overlays;
    private List<Rectangle> cooldownOverlays;
    private List<Long> slotsCooldown;
    private List<Boolean> slotsLocked;
    private ColorAdjust greyscaleEffect;
    private int activeSlot = 0;
    private double w = 70, h = 70;

    public AbilityBar(double x, double y){
        slots = new ArrayList<>();
        abilityIcons = new ArrayList<>();
        overlays = new ArrayList<>();
        slotsLocked = new ArrayList<>();
        slotsCooldown= new ArrayList<>();
        cooldownOverlays = new ArrayList<>();

        greyscaleEffect = new ColorAdjust();
        greyscaleEffect.setSaturation(-1);

        for (int i = 0; i < 4; i++){
            slots.add(new Rectangle(x, y, w, h));
            slots.get(i).setFill(Color.LIGHTGRAY);
            slots.get(i).setStroke(Color.rgb(51,0,25));
            slots.get(i).setStrokeWidth(8);

            Canvas canvas = new Canvas(w, h);
            canvas.setLayoutX(x);
            canvas.setLayoutY(y);
            abilityIcons.add(canvas);

            Rectangle overlay = new Rectangle(x, y, w, h);
            overlay.setFill(Color.rgb(0,0,0, 0.4));
            overlay.setVisible(true);
            overlays.add(overlay);

            Rectangle cooldownOverlay = new Rectangle(x, y+h, w, 0);
            cooldownOverlay.setFill(Color.rgb(255, 255, 255, 0.7));
            cooldownOverlay.setVisible(false);
            cooldownOverlays.add(cooldownOverlay);

            slotsCooldown.add(0L);
            if (i == 0){slotsLocked.add(false);}
            else slotsLocked.add(true);

            x += w+10;
        }
        setActiveSlot(activeSlot);
    }
    public List<Rectangle> getSlots(){return slots;}
    public  List<Canvas> getAbilityIcons(){return abilityIcons;}
    public List<Rectangle> getOverlays(){return overlays;}
    public List<Rectangle> getCooldownOverlays(){return cooldownOverlays;}
    public Long getSlotCooldown(int i){return slotsCooldown.get(i);}
    public void setSlotCooldown(int i, long value){slotsCooldown.set(i, value);}
    public boolean getLockedSlots(int i){return slotsLocked.get(i);}

    public void unlockSlot(int slot){
        slotsLocked.set(slot, false);
        abilityIcons.get(slot).setEffect(null);
    }
    public void lockSlot(int slot){slotsLocked.set(slot, false);}

    public void animate(double dTime) {
        for (int i = 0; i < slotsCooldown.size(); i++) {
            long slot = slotsCooldown.get(i);
            if (slot == 0L) continue;

            slot -= (long)(dTime * 1_000_000_000.0);
            if (slot < 0L) slot = 0L;

            slotsCooldown.set(i, slot);
        }
    }

    public void setActiveSlot(int slot){
        for (int s = 0; s < slots.size(); s++){
            slots.get(s).setStroke(Color.rgb(51,0,25));
            if (slotsLocked.get(s)) abilityIcons.get(s).setEffect(greyscaleEffect);
            overlays.get(s).setVisible(true);
        }

        if (!slotsLocked.get(slot)){
            activeSlot = slot;
        }
        slots.get(activeSlot).setStroke(Color.rgb(153,0,76));
        overlays.get(activeSlot).setVisible(false);
        abilityIcons.get(activeSlot).setEffect(null);
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
