import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Bloodspire extends Application {
    @Override
    public void start(Stage stage) {
        Game game = new Game(); // Hauptspiel-Logik
        Pane gamePane = game.getGamePane(); // Spielbereich

        Scene scene = new Scene(gamePane, 800, 800); // Spielszene
        stage.setTitle("Bloodspire");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}