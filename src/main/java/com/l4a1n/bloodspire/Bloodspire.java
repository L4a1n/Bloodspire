package com.l4a1n.bloodspire;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Bloodspire extends Application {
    @Override
    public void start(Stage stage) {
        Level level = new Level(); // Hauptspiel-Logik
        Pane gamePane = level.getGamePane(); // Spielbereich

        Scene scene = new Scene(gamePane, 1280, 820); // Spielszene
        stage.setTitle("Bloodspire");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}