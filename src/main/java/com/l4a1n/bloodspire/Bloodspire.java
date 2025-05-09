package com.l4a1n.bloodspire;

import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Bloodspire extends Application {

    @Override
    public void start(Stage stage) {
        Level level = new Level(); // Hauptspiel-Logik
        Pane gamePane = level.getGamePane(); // Spielbereich

        Scene scene = new Scene(gamePane, 1280, 820); // Spielszene

        Image cursorIcon = new Image(getClass().getResource("/CursorIcon.png").toExternalForm());
        scene.setCursor(new ImageCursor(cursorIcon));

        stage.getIcons().add(new Image(getClass().getResource("/Icon.png").toExternalForm()));
        stage.setTitle("Bloodspire");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
    }


    public static void main(String[] args) {
        launch(args);
    }

}