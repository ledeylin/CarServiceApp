package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import graphics.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import javafx.scene.control.PasswordField;

public class EmployeesEditAcc extends Constants {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button_personal_acc;

    @FXML
    private Button button_personal_service;

    @FXML
    private Button button_save;

    @FXML
    private TextField text_address;

    @FXML
    private static TextField text_login;

    @FXML
    private TextField text_name;

    @FXML
    private TextField text_pass;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
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
        });

        // окно подтверждения пароля перед сохранением редактирования
        signInButton.setOnAction(actionEvent -> {
            if (Objects.equals(passwordField.getText(), SignInController.user.getPassword())) {
                signInButton.getScene().getWindow().hide();
                save();
            }
            else {
                System.out.println("Error: password entered incorrectly.");
                Shake passwordAnim = new Shake(passwordField);
                passwordAnim.playAnim();
            }
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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(".fxml"));
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

    // сохранение отредактированной информации
    private void save() {

        // логин
        if (text_login != null) {
            try {
                String query = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " =?";
                PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
                statement.setString(1, text_login.getText().trim());
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    System.out.println("Error: login already exist.");
                }
                else {
                    String string = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_LOGIN + " = " + text_login.getText().trim()
                            + " WHERE " + EMPLOYEE_LOGIN + " = " + SignInController.user.getLogin() + ";";

                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Error: incorrect login.");
            }
        }

    }

}
