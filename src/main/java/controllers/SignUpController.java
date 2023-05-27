package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button logInWindowButton;

    @FXML
    private Button signUpButton;

    @FXML
    private DatePicker signUpDateOfBirth;

    @FXML
    private CheckBox signUpFemale;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpLogin;

    @FXML
    private CheckBox signUpMale;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpSecondName;

    @FXML
    void initialize() {
        logInWindowButton.setOnAction(actionEvent -> {
            FXMLLoader loader  =  new FXMLLoader(Main.class.getResource("logIn.fxml"));

            try {
                loader.load();
            } catch (IOException ignored) {}

            logInWindowButton.getScene().getWindow().hide();

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

        });
    }

}
