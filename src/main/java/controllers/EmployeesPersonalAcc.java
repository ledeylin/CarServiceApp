package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

public class EmployeesPersonalAcc {

    @FXML
    private Button personal_edit;

    @FXML
    private Button personal_service;

    @FXML
    private Text text_address;

    @FXML
    private Text text_login;

    @FXML
    private Text text_name;

    @FXML
    private Text text_pass;

    @FXML
    private Text text_permissions;

    @FXML
    void initialize() {

        // отображение информации о сотруднике
        text_name.setText(SignInController.user.getLast_name() + " " + SignInController.user.getFirst_name() + " " + SignInController.user.getSecond_name());
        text_address.setText(SignInController.user.getAddress());
        text_login.setText(SignInController.user.getLogin());
        text_pass.setText("*".repeat(SignInController.user.getPassword().length()));
        if (SignInController.user.getPermission() == 1) text_permissions.setText("Рабочий");
        else if (SignInController.user.getPermission() == 2) text_permissions.setText("Администратор");


        // переход на окно редактирования информации
        personal_edit.setOnAction(actionEvent -> {
            personal_edit.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit_account_employeers.fxml"));
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
        personal_service.setOnAction(actionEvent -> {
            personal_service.getScene().getWindow().hide();
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

    }
}
