package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import graphics.Shake;
import main.SaveInformationPeople;

import java.util.Objects;

public class MainPass {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    private static String password;

    private static int id; // admin:
                           // 0 - edit employees    1 - edit service     8 - edit clients
                           // 2 - add employees     3 - add services     9 - add clients
                           // 4 - delete employees  5 - delete services  10 - delete clients
                           // 6 - edit
                           // employee:
                           // 7 - edit


    @FXML
    void initialize() {

        // окно подтверждения пароля перед сохранением редактирования
        signInButton.setOnAction(actionEvent -> {

            if (Objects.equals(passwordField.getText(), password)) {
                signInButton.getScene().getWindow().hide();

                try {
                    if (id == 0) EditAdminEmployees.save();
                    if (id == 1) EditAdminServices.save();
                    if (id == 2) AddAdminEmployee.add();
                    if (id == 3) AddAdminService.add();
                    if (id == 4) AdminEmployees.delete();
                    if (id == 5) AdminServices.delete();
                    if (id == 6) AdminEdit.save();
                    if (id == 7) EmployeeEdit.save();

                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("Error: password entered incorrectly.");
                Shake passwordAnim = new Shake(passwordField);
                passwordAnim.playAnim();
            }

        });
    }

    public static void setId(int id) {
        MainPass.id = id;
    }

    public static void setPassword(String password) {
        MainPass.password = password;
    }
}

