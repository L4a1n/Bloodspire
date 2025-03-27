package com.l4a1n.bloodspire.leveleditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LevelEditor extends Application{
    @Override
    public void start(Stage stage){
        Pane editorPane = new Pane();
        Scene scene = new Scene(editorPane, 800, 900);

        stage.setTitle("Level Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
