package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class AddAdminEmployee extends Constants {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpButton;

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
        signUpButton.setOnAction(actionEvent -> {

            // проверка фамилии
            last_name = text_last_name.getText().trim();
            if (last_name.equals("")) {
                System.out.println("Error: empty last name.");
                flag = false;
            }

            // проверка имени
            first_name = text_first_name.getText().trim();
            if (first_name.equals("")) {
                System.out.println("Error: empty first name.");
                flag = false;
            }

            // фамилия
            second_name = text_second_name.getText().trim();

            // проверка адреса
            address = text_address.getText().trim();
            if (address.equals("")) {
                System.out.println("Error: empty address.");
                flag = false;
            }

            // проверка логина
            login = text_login.getText().trim();
            if (login.equals("")) {
                System.out.println("Error: empty login.");
                flag = false;
            }

            // проверка логина
            password = text_password.getText().trim();
            if (password.equals("")) {
                System.out.println("Error: empty password.");
                flag = false;
            }

            // проверка пароля админа
            if (flag) {
                Stage stage = new Stage();
                MainPass.setId(2);
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


        String insertNew = "INSERT INTO " + EMPLOYEE_TABLE + " (" + EMPLOYEE_LAST_NAME + ", " +
                EMPLOYEE_FIRST_NAME + ", " + EMPLOYEE_SECOND_NAME + ", " + EMPLOYEE_ADDRESS + ", " +
                EMPLOYEE_LOGIN + ", " + EMPLOYEE_PASSWORD + ", " + EMPLOYEE_ACCESS_RIGHTS +
                ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        System.out.println(insertNew);

        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
        preparedStatement.setString(1, last_name);
        preparedStatement.setString(2, first_name);
        preparedStatement.setString(3, second_name);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, login);
        preparedStatement.setString(6, password);
        preparedStatement.setInt(7, 1);
        preparedStatement.executeUpdate();

    }

}
