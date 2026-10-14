package tictacto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("TicTacToView.fxml"));
            Scene scene = new Scene(root); // scene - Inhalt des Fensters mit dem geladenen Layout
            primaryStage.setScene(scene);
            primaryStage.setTitle("Tic-Tac-Toe");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
