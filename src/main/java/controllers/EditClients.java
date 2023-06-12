package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import main.SaveInformation;

public class EditClients {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signUpAddress;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpSecondName;

    @FXML
    private TextField signUpLogin;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpPhoneNumber;

    @FXML
    void initialize() {
        signUpButton.setOnAction(actionEvent -> {
            String lastName = "";
            try { lastName = signUpLastName.getText().trim(); } catch (Exception ignored) { }
            String firstName = "";
            try { firstName = signUpFirstName.getText().trim(); } catch (Exception ignored) { }
            String secondName = "";
            try { secondName = signUpSecondName.getText().trim(); } catch (Exception ignored) { }
            String address = "";
            try { address = signUpAddress.getText().trim(); } catch (Exception ignored) { }
            String phone = "";
            try { phone = signUpPhoneNumber.getText().trim(); } catch (Exception ignored) { }
            String login = "";
            try { login = signUpLogin.getText().trim(); } catch (Exception ignored) { }
            String pass = "";
            try { pass = signUpPassword.getText().trim(); } catch (Exception ignored) { }
            SaveInformation.setText_permissions(0);
            SaveInformation.setPhone(phone);
            SaveInformation.setAddress(address);
            SaveInformation.setFirst_name(firstName);
            SaveInformation.setLast_name(lastName);
            SaveInformation.setSecond_name(secondName);
            SaveInformation.setText_login(login);
            SaveInformation.setText_pass(pass);
            SaveInformation.setText_permissions(0);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pass.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 400, 250);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });
    }
}
