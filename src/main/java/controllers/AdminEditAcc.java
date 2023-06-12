package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import javafx.scene.control.PasswordField;
import main.SaveInformation;

public class AdminEditAcc {

    @FXML
    private Button button_personal_acc;

    @FXML
    private Button button_personal_service;

    @FXML
    private Button button_save;

    @FXML
    private Button personal_clients;

    @FXML
    private Button personal_employees;

    @FXML
    private TextField text_login;

    @FXML
    private TextField text_pass;
    @FXML
    void initialize() {

        // активация кнопки сохранения
        button_save.setOnAction(actionEvent -> {
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
            SaveInformation.setText_login(text_login.getText().trim());
            SaveInformation.setText_old_login(SignInController.user.getLogin());
            SaveInformation.setText_pass(text_pass.getText().trim());
            SaveInformation.setText_permissions(1);
        });

        // переход на окно личного кабинета
        button_personal_acc.setOnAction(actionEvent -> {
            button_personal_acc.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("personal_account_employeers.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

        // переход на окно просмотра услуг
        button_personal_service.setOnAction(actionEvent -> {
            button_personal_service.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("services_employee.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

        // переход на окно клиентов
        personal_clients.setOnAction(actionEvent -> {
            personal_clients.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("clients_admin.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

    }


}
