package controllers;

import graphics.Shake;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.Objects;

public class PassController {
    @FXML
    private Button button_save;
    @FXML
    private TextField passwordField;
    @FXML
    private Text text_mistake;

    private static String password;

    private static int id; // admin:
                           // 0 - edit employees    1 - edit service     8 - edit clients
                           // 2 - add employees     3 - add services     9 - add clients
                           // 4 - delete employees  5 - delete services  10 - delete clients
                           // 6 - edit
                           // employee:
                           // 7 - edit
                           // 15 - add detail
                           // 16 - delete detail
                           // client:
                           // 11 - edit
                           // 12 - edit car
                           // 13 - add car
                           // 14 - delete car
    @FXML
    void initialize() {

        // окно подтверждения пароля перед сохранением редактирования
        button_save.setOnAction(actionEvent -> {

            if (Objects.equals(passwordField.getText(), password)) {
                button_save.getScene().getWindow().hide();

                try {
                    if (id == 0) AdminEmployeesEditController.save();
                    if (id == 1) AdminServicesEditController.save();
                    if (id == 2) AdminEmployeesAddController.add();
                    if (id == 3) AdminServicesAddController.add();
                    if (id == 4) AdminEmployeesController.delete();
                    if (id == 5) AdminServicesController.delete();
                    if (id == 6) AdminEditController.save();
                    if (id == 7) EmployeeEditController.save();
                    if (id == 8) AdminClientsEditController.save();
                    if (id == 9) AdminClientsAddController.add();
                    if (id == 10) AdminClientsController.delete();
                    if (id == 11) ClientEditController.save();
                    if (id == 12) ClientGarageEditCarController.save();
                    if (id == 13) ClientGarageController.add();
                    if (id == 14) ClientGarageController.delete();
                    if (id == 15) EmployeeWorkController.add();
                    if (id == 16) EmployeeWorkController.delete();

                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("Error: password entered incorrectly.");
                text_mistake.setText("Пароль введён неправильно!");
                Shake passwordAnim = new Shake(passwordField);
                passwordAnim.playAnim();
            }

        });
    }

    public static void setId(int id) {
        PassController.id = id;
    }

    public static void setPassword(String password) {
        PassController.password = password;
    }
}

