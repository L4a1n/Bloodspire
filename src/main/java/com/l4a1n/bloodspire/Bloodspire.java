package com.l4a1n.bloodspire;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class Bloodspire extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(300);
        settings.setHeight(200);
        settings.setTitle("Basic Game App");
    }

    public static void main(String[] args) {
        launch(args);
    }
}