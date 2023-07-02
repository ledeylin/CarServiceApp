package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminEmployeesAddController extends Constants {

    @FXML
    private Button button_save;

    @FXML
    private Text text_mistake;

    @FXML
    private TextField text_address;

    @FXML
    private TextField text_first_name;

    @FXML
    private TextField text_last_name;

    @FXML
    private TextField text_login;

    @FXML
    private TextField text_password;

    @FXML
    private TextField text_second_name;

    private static String last_name = "";

    private static String first_name = "";

    private static String second_name = "";

    private static String address = "";

    private static String login = "";

    private static String password = "";

    private boolean flag = true;

    @FXML
    void initialize() {

        // сохранение
        button_save.setOnAction(actionEvent -> {

            last_name = text_last_name.getText().trim();
            first_name = text_first_name.getText().trim();
            second_name = text_second_name.getText().trim();

            String lowerCase;
            // проверка фамилии на пустоту
            if (last_name.equals("")) {
                System.out.println("Error: empty last name.");
                text_mistake.setText("Вы не ввели фамилию!");
                flag = false;
            }
            else {
                lowerCase = text_last_name.getText().trim().toLowerCase();
                last_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
            }

            // проверка имени на пустоту
            if (first_name.equals("")) {
                System.out.println("Error: empty first name.");
                text_mistake.setText("Вы не ввели имя!");
                flag = false;
            }
            else {
                lowerCase = text_first_name.getText().trim().toLowerCase();
                first_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
            }

            // отчество
            try {
                lowerCase = text_second_name.getText().trim().toLowerCase();
                second_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
            } catch (Exception ignored) {}

            // проверка адреса
            address = text_address.getText().trim();
            if (address.equals("")) {
                System.out.println("Error: empty address.");
                text_mistake.setText("Вы не ввели адрес!");
                flag = false;
            }

            // проверка логина
            login = text_login.getText().trim();
            if (login.equals("")) {
                System.out.println("Error: empty login.");
                text_mistake.setText("Вы не ввели логин!");
                flag = false;
            }

            // проверка логина
            password = text_password.getText().trim();
            if (password.equals("")) {
                System.out.println("Error: empty password.");
                text_mistake.setText("Вы не ввели пароль!");
                flag = false;
            }

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

            // проверка пароля админа
            if (flag) {
                PassController.setId(2);
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

    public static void add()
            throws SQLException, ClassNotFoundException {


        String insertNew = "INSERT INTO " + EMPLOYEES_TABLE + " (" + EMPLOYEES_LAST_NAME + ", " +
                EMPLOYEES_FIRST_NAME + ", " + EMPLOYEES_SECOND_NAME + ", " + EMPLOYEES_ADDRESS + ", " +
                EMPLOYEES_LOGIN + ", " + EMPLOYEES_PASSWORD + ", " + EMPLOYEES_ACCESS_RIGHTS +
                ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = DatabaseHandler.getInstance().prepareStatement(insertNew);
        preparedStatement.setString(1, last_name);
        preparedStatement.setString(2, first_name);
        preparedStatement.setString(3, second_name);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, login);
        preparedStatement.setString(6, password);
        preparedStatement.setInt(7, 1);
        preparedStatement.executeUpdate();
        System.out.println("Access!");

        Main.changeScene("admin_employees.fxml");

    }

}
