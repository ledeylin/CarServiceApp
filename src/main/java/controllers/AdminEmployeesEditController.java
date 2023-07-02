package controllers;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class AdminEmployeesEditController extends Constants {

    @FXML
    private TextField signUpAddress;

    @FXML
    private Button button_save;

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
    private TextField signUpPermissions;

    @FXML
    private Text text_mistake;

    private static String last_name = "";

    private static String first_name = "";

    private static String second_name = "";

    private static String address = "";

    private static String login = "";

    private static String pass = "";

    private static int permissions = 0;

    private static String old_login;

    private boolean flag = true;

    public static void setOld_login(String old_login) {
        AdminEmployeesEditController.old_login = old_login;
    }

    @FXML
    void initialize() {
        button_save.setOnAction(actionEvent -> {

            last_name = signUpLastName.getText().trim();
            first_name = signUpFirstName.getText().trim();
            second_name = signUpSecondName.getText().trim();
            address = signUpAddress.getText().trim();
            login = signUpLogin.getText().trim();
            pass = signUpPassword.getText().trim();
            try { permissions = Integer.parseInt(signUpPermissions.getText().trim()); }
            catch (NumberFormatException ignored) { }

            String lowerCase;
            try {
                lowerCase = last_name.toLowerCase();
                last_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
            } catch (Exception ignored) {}

            try {
                lowerCase = first_name.toLowerCase();
                first_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
            } catch (Exception ignored) {}

            try {
                lowerCase = second_name.toLowerCase();
                second_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
            } catch (Exception ignored) {}

            // проверка логина на повторение
            try {
                String query_clients = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =?";
                String query_employees = "SELECT * FROM " + EMPLOYEES_TABLE + " WHERE " + EMPLOYEES_LOGIN + " =?";

                PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query_clients);
                statement.setString(1, login);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    System.out.println("Error: login already exist.");
                    text_mistake.setText("Логин уже существует!");
                    flag = false;
                }

                statement = DatabaseHandler.getInstance().prepareStatement(query_employees);
                statement.setString(1, login);
                result = statement.executeQuery();
                if (result.next()) {
                    System.out.println("Error: login already exist.");
                    text_mistake.setText("Логин уже существует!");
                    flag = false;
                }

            } catch (SQLException | ClassNotFoundException e) { throw new RuntimeException(e); }

            if (flag) {
                PassController.setId(0);
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pass.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 400, 250);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();
            }

        });
    }

    public static void save()
            throws SQLException, ClassNotFoundException {

        // пароль
        if (!Objects.equals(pass, "")) {
            String sqlAlterTable = "UPDATE " + EMPLOYEES_TABLE +
                    " SET " + EMPLOYEES_PASSWORD + " = '" + pass +
                    "' WHERE " + EMPLOYEES_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // фамилия
        if (!Objects.equals(last_name, "")) {
            String sqlAlterTable = "UPDATE " + EMPLOYEES_TABLE +
                    " SET " + EMPLOYEES_LAST_NAME + " = '" + last_name +
                    "' WHERE " + EMPLOYEES_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // имя
        if (!Objects.equals(first_name, "")) {
            String sqlAlterTable = "UPDATE " + EMPLOYEES_TABLE +
                    " SET " + EMPLOYEES_FIRST_NAME + " = '" + first_name +
                    "' WHERE " + EMPLOYEES_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // отчество
        if (!Objects.equals(second_name, "")) {
            String sqlAlterTable = "UPDATE " + EMPLOYEES_TABLE +
                    " SET " + EMPLOYEES_SECOND_NAME + " = '" +
                    second_name + "' WHERE " + EMPLOYEES_LOGIN +
                    " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // адрес
        if (!Objects.equals(address, "")) {
            String sqlAlterTable = "UPDATE " + EMPLOYEES_TABLE +
                    " SET " + EMPLOYEES_ADDRESS + " = '" + address +
                    "' WHERE " + EMPLOYEES_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // права
        if (permissions != 0) {
            String sqlAlterTable = "UPDATE " + EMPLOYEES_TABLE +
                    " SET " + EMPLOYEES_ACCESS_RIGHTS + " = " +
                    permissions + " WHERE " + CLIENTS_LOGIN +
                    " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        Main.changeScene("admin_employees.fxml");

    }
}
