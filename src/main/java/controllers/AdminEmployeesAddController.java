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

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        // сохранение
        button_save.setOnAction(actionEvent -> {

            // проверка фамилии
            last_name = text_last_name.getText().trim();
            if (last_name.equals("")) {
                System.out.println("Error: empty last name.");
                text_mistake.setText("Вы не ввели фамилию!");
                flag = false;
            }

            // проверка имени
            first_name = text_first_name.getText().trim();
            if (first_name.equals("")) {
                System.out.println("Error: empty first name.");
                text_mistake.setText("Вы не ввели имя!");
                flag = false;
            }

            // фамилия
            second_name = text_second_name.getText().trim();

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
                String query_employees = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " =?";

                PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query_clients);
                statement.setString(1, login);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    System.out.println("Error: login already exist.");
                    text_mistake.setText("Логин уже существует!");
                    flag = false;
                }

                statement = databaseHandler.getDbConnection().prepareStatement(query_employees);
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
                Stage stage = new Stage();
                PassController.setId(2);
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pass.fxml"));
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

    public static void add()
            throws SQLException, ClassNotFoundException {


        String insertNew = "INSERT INTO " + EMPLOYEE_TABLE + " (" + EMPLOYEE_LAST_NAME + ", " +
                EMPLOYEE_FIRST_NAME + ", " + EMPLOYEE_SECOND_NAME + ", " + EMPLOYEE_ADDRESS + ", " +
                EMPLOYEE_LOGIN + ", " + EMPLOYEE_PASSWORD + ", " + EMPLOYEE_ACCESS_RIGHTS +
                ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
        preparedStatement.setString(1, last_name);
        preparedStatement.setString(2, first_name);
        preparedStatement.setString(3, second_name);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, login);
        preparedStatement.setString(6, password);
        preparedStatement.setInt(7, 1);
        preparedStatement.executeUpdate();
        System.out.println("Access!");

    }

}