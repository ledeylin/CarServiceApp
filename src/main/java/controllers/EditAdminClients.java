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
import main.SaveInformationPeople;

public class EditAdminClients {

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
            String firstName = "";
            String secondName = "";
            String address = "";
            String phone = "";
            String login = "";
            String pass = "";

            try { lastName = signUpLastName.getText().trim(); } catch (Exception ignored) { }
            try { firstName = signUpFirstName.getText().trim(); } catch (Exception ignored) { }
            try { secondName = signUpSecondName.getText().trim(); } catch (Exception ignored) { }
            try { address = signUpAddress.getText().trim(); } catch (Exception ignored) { }
            try { phone = signUpPhoneNumber.getText().trim(); } catch (Exception ignored) { }
            try { login = signUpLogin.getText().trim(); } catch (Exception ignored) { }
            try { pass = signUpPassword.getText().trim(); } catch (Exception ignored) { }

            SaveInformationPeople.setText_permissions(0);
            SaveInformationPeople.setPhone(phone);
            SaveInformationPeople.setAddress(address);
            SaveInformationPeople.setFirst_name(firstName);
            SaveInformationPeople.setLast_name(lastName);
            SaveInformationPeople.setSecond_name(secondName);
            SaveInformationPeople.setText_login(login);
            SaveInformationPeople.setText_pass(pass);
            SaveInformationPeople.setText_permissions(0);

            Stage stage = new Stage();
            MainPass.setId(0);
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
