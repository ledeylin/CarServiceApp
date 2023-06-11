package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import graphics.Shake;
import main.SaveInformation;

import java.util.Objects;

public class PassController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    void initialize() {

        // окно подтверждения пароля перед сохранением редактирования
        signInButton.setOnAction(actionEvent -> {
            if (Objects.equals(passwordField.getText(), SignInController.user.getPassword())) {
                signInButton.getScene().getWindow().hide();
                try { SaveInformation.save(); }
                catch (SQLException | ClassNotFoundException e) { throw new RuntimeException(e); }
            }
            else {
                System.out.println("Error: password entered incorrectly.");
                Shake passwordAnim = new Shake(passwordField);
                passwordAnim.playAnim();
            }
        });
    }

}

