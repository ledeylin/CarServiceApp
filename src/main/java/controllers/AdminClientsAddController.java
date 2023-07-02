package controllers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class AdminClientsAddController extends Constants {

    @FXML
    private Button button_save;

    @FXML
    private TextField signUpPhoneNumber;

    @FXML
    private TextField text_address;

    @FXML
    private TextField text_first_name;

    @FXML
    private TextField text_last_name;

    @FXML
    private TextField text_login;

    @FXML
    private Text text_mistake;

    @FXML
    private TextField text_password;

    @FXML
    private TextField text_second_name;

    private static String last_name = "";

    private static String first_name = "";

    private static String second_name = "";

    private static String address = "";

    private static String phone = "";

    private static String login = "";

    private static String pass = "";

    private boolean flag = true;

    @FXML
    void initialize() {

        // сохранение
        button_save.setOnAction(actionEvent -> {

            last_name = text_last_name.getText().trim();
            first_name = text_first_name.getText().trim();
            second_name = text_second_name.getText().trim();
            address = text_address.getText().trim();
            phone = signUpPhoneNumber.getText().trim();
            login = text_login.getText().trim();
            pass = text_password.getText().trim();

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

            try {
                lowerCase = text_second_name.getText().trim().toLowerCase();
                second_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
            } catch (Exception ignored) {}

            // проверка логина на пустоту
            if (login.equals("")) {
                System.out.println("Error: empty login.");
                text_mistake.setText("Вы не ввели логин!");
                flag = false;
            }

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

            // проверка номера на пустоту
            if (phone.equals("")) {
                System.out.println("Error: empty phone number.");
                text_mistake.setText("Вы не ввели номер телефона!");
                flag = false;
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

            // проверка пароля на пустоту
            if (pass.equals("")) {
                System.out.println("Error: empty pass.");
                text_mistake.setText("Вы не ввели пароль!");
                flag = false;
            }


            // проверка пароля пользователя
            if (flag) {
                PassController.setId(9);
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


        String insertNew = "INSERT INTO " + CLIENTS_TABLE + " (" + CLIENTS_LAST_NAME + ", " +
                CLIENTS_FIRST_NAME + ", " + CLIENTS_SECOND_NAME + ", " + CLIENTS_ADDRESS + ", " +
                CLIENTS_LOGIN + ", " + CLIENTS_PASSWORD + ", " + CLIENTS_PHONE_NUMBER + ", status" +
                ") VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = DatabaseHandler.getInstance().prepareStatement(insertNew);
        preparedStatement.setString(1, last_name);
        preparedStatement.setString(2, first_name);
        preparedStatement.setString(3, second_name);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, login);
        preparedStatement.setString(6, pass);
        preparedStatement.setString(7, phone);
        preparedStatement.setInt(8, 1);
        preparedStatement.executeUpdate();

        Main.changeScene("admin_clients.fxml");

    }

}
