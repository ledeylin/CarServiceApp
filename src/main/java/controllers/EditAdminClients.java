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
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class EditAdminClients extends Constants {

    @FXML
    private TextField signUpAddress;

    @FXML
    private Button signUpButton;

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

    private static String last_name = "";

    private static String first_name = "";

    private static String second_name = "";

    private static String address = "";

    private static String phone = "";

    private static String login = "";

    private static String pass = "";

    private static String old_login;

    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private boolean flag = true;

    public static void setOld_login(String old_login) {
        EditAdminClients.old_login = old_login;
    }

    @FXML
    void initialize() {
        signUpButton.setOnAction(actionEvent -> {

            last_name = signUpLastName.getText().trim();
            first_name = signUpFirstName.getText().trim();
            second_name = signUpSecondName.getText().trim();
            address = signUpAddress.getText().trim();
            phone = signUpPhoneNumber.getText().trim();
            login = signUpLogin.getText().trim();
            pass = signUpPassword.getText().trim();

            // проверка логина на повторение
            if (!Objects.equals(login, "")) {
                try {
                    String query_clients = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =?";
                    String query_employees = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " =?";

                    PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query_clients);
                    statement.setString(1, login);
                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                        flag = false;
                    }

                    statement = databaseHandler.getDbConnection().prepareStatement(query_employees);
                    statement.setString(1, login);
                    result = statement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                        flag = false;
                    }

                } catch (SQLException | ClassNotFoundException e) { throw new RuntimeException(e); }
            }

            // проверка номера телефона на корректность (вид +7 (012)-345-67-89)
            if (!Objects.equals(phone, "")) {
                try {
                    String regex = "\\+7\\s\\([0-9]{3}\\)-[0-9]{3}-[0-9]{2}-[0-9]{2}";
                    if (!phone.matches(regex)) {
                        System.out.println("Error: incorrect phone number");
                        flag = false;
                    }
                } catch (Exception e) {
                    System.out.println("Error: incorrect phone number.");
                    flag = false;
                }
            }

            // проверка пароля пользователя
            if (flag) {
                MainPass.setId(8);
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main_pass.fxml"));
                Scene scene = null;
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

            Connection connection = databaseHandler.getDbConnection();
            System.out.println(sqlAlterTable);
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
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // фамилия
        if (!Objects.equals(last_name, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_LAST_NAME + " = '" + last_name +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // имя
        if (!Objects.equals(first_name, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_FIRST_NAME + " = '" + first_name +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // отчество
        if (!Objects.equals(second_name, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_SECOND_NAME + " = '" +
                    second_name + "' WHERE " + CLIENTS_LOGIN +
                    " = '" + old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // адрес
        if (!Objects.equals(address, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_ADDRESS + " = '" + address +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // телефон
        if (!Objects.equals(phone, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE +
                    " SET " + CLIENTS_PHONE_NUMBER + " = '" + phone +
                    "' WHERE " + CLIENTS_LOGIN + " = '" + old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

    }
}
