package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class AdminClientsEditController extends Constants {

    @FXML
    private Button button_save;

    @FXML
    private TextField signUpAddress;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpLogin;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpPhoneNumber;

    @FXML
    private TextField signUpSecondName;

    @FXML
    private Text text_mistake;

    private static String last_name = "";

    private static String first_name = "";

    private static String second_name = "";

    private static String address = "";

    private static String phone = "";

    private static String login = "";

    private static String pass = "";

    private static String old_login;

    private boolean flag = true;

    public static void setOld_login(String old_login) {
        AdminClientsEditController.old_login = old_login;
    }

    @FXML
    void initialize() {
        button_save.setOnAction(actionEvent -> {

            last_name = signUpLastName.getText().trim();
            first_name = signUpFirstName.getText().trim();
            second_name = signUpSecondName.getText().trim();
            address = signUpAddress.getText().trim();
            phone = signUpPhoneNumber.getText().trim();
            login = signUpLogin.getText().trim();
            pass = signUpPassword.getText().trim();

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
            if (!Objects.equals(login, "")) {
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
            }

            // проверка номера телефона на корректность
            if (!Objects.equals(phone, "")) {
                try {
                    String regex = "(?:\\+\\d{1,3}|8)[\\s-]?\\(?\\d{3}\\)?[\\s-]?\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{2}";
                    if (!phone.matches(regex)) {
                        System.out.println("Error: incorrect phone number");
                        text_mistake.setText("Некорректный номер телефона!");
                        flag = false;
                    }
                } catch (Exception e) {
                    System.out.println("Error: incorrect phone number.");
                    text_mistake.setText("Некорректный номер телефона!");
                    flag = false;
                }
            }

            // проверка пароля пользователя
            if (flag) {
                PassController.setId(8);
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

        // логин
        if (!Objects.equals(login, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " +
                    CLIENTS_LOGIN + " = '" + login + "' WHERE " +
                    CLIENTS_LOGIN + " = '" + old_login + "';";

            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
            old_login = login;
        }

        // пароль
        if (!Objects.equals(pass, "")) {
            String sqlAlterTable = "UPDATE " +  CLIENTS_TABLE +
                    " SET " + CLIENTS_PASSWORD + " = '" + pass +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // фамилия
        if (!Objects.equals(last_name, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_LAST_NAME + " = '" + last_name +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // имя
        if (!Objects.equals(first_name, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_FIRST_NAME + " = '" + first_name +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // отчество
        if (!Objects.equals(second_name, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_SECOND_NAME + " = '" +
                    second_name + "' WHERE " + CLIENTS_LOGIN +
                    " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // адрес
        if (!Objects.equals(address, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_ADDRESS + " = '" + address +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // телефон
        if (!Objects.equals(phone, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_PHONE_NUMBER + " = '" + phone +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        Main.changeScene("admin_clients.fxml");

    }

}
