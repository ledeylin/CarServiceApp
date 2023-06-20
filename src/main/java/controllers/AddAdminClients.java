package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class AddAdminClients extends Constants {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpButton;

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

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        // сохранение
        signUpButton.setOnAction(actionEvent -> {

            last_name = text_last_name.getText().trim();
            first_name = text_first_name.getText().trim();
            second_name = text_second_name.getText().trim();
            address = text_address.getText().trim();
            phone = signUpPhoneNumber.getText().trim();
            login = text_login.getText().trim();
            pass = text_password.getText().trim();

            // проверка фамилии на пустоту
            if (last_name.equals("")) {
                System.out.println("Error: empty last name.");
                flag = false;
            }

            // проверка имени на пустоту
            if (first_name.equals("")) {
                System.out.println("Error: empty first name.");
                flag = false;
            }

            // проверка логина на пустоту
            if (login.equals("")) {
                System.out.println("Error: empty login.");
                flag = false;
            }

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

            // проверка номера на пустоту
            if (phone.equals("")) {
                System.out.println("Error: empty phone number.");
                flag = false;
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

            // проверка пароля на пустоту
            if (pass.equals("")) {
                System.out.println("Error: empty pass.");
                flag = false;
            }


            // проверка пароля пользователя
            if (flag) {
                MainPass.setId(9);
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

    public static void add()
            throws SQLException, ClassNotFoundException {


        String insertNew = "INSERT INTO " + CLIENTS_TABLE + " (" + CLIENTS_LAST_NAME + ", " +
                CLIENTS_FIRST_NAME + ", " + CLIENTS_SECOND_NAME + ", " + CLIENTS_ADDRESS + ", " +
                CLIENTS_LOGIN + ", " + CLIENTS_PASSWORD + ", " + CLIENTS_PHONE_NUMBER + ", status" +
                ") VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println(insertNew);

        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
        preparedStatement.setString(1, last_name);
        preparedStatement.setString(2, first_name);
        preparedStatement.setString(3, second_name);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, login);
        preparedStatement.setString(6, pass);
        preparedStatement.setString(7, phone);
        preparedStatement.setInt(8, 0);
        preparedStatement.executeUpdate();

    }

}
