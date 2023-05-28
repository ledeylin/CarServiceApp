package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.DatabaseHandler;
import main.Main;

public class SignInController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signInButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpWindowButton;

    @FXML
    void initialize() {

        signUpWindowButton.setOnAction(actionEvent -> {
            FXMLLoader  loader  =  new FXMLLoader(Main.class.getResource("signUp.fxml"));

            try {
                loader.load();
            } catch (IOException ignored) {}

            signUpWindowButton.getScene().getWindow().hide();

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });

        signInButton.setOnAction(actionEvent -> {
            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();
            
            if (!login.equals("") && !password.equals("")) signInUser(login, password);
            else System.out.println("Error: login or password is epmty.");
        });
    }

    private void signInUser(String login, String password) {
    }
}
