package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {

    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        Main.stage = stage;
        Parent panel = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("sign_in.fxml")));
        stage.setScene(new Scene(panel));
        stage.setTitle("Автосервис");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

    public static void changeScene(String fxml) {
        try {
            Parent screen = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml)));
            stage.setScene(new Scene(screen));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setStage(Stage stage) {
        Main.stage = stage;
    }
}