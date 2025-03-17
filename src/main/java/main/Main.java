package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/bankerAlgorithm.fxml"));
        BorderPane root = loader.load();
        primaryStage.setTitle("Banker's Algorithm Simulator");
        primaryStage.setScene(new Scene(root, 720, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
