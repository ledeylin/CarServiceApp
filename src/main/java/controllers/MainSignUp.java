package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class MainSignUp extends Constants {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signInWindowButton;

    @FXML
    private TextField signUpAddress;

    @FXML
    private Button signUpButton;

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
    private TextField signUpPhoneNumber;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize()  {

        // переход на окно авторизации
        signInWindowButton.setOnAction(actionEvent -> {

            signInWindowButton.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main_sign_in.fxml"));
            Scene scene = null;
            try { scene = new Scene(fxmlLoader.load(), 700, 400); }
            catch (IOException e) { throw new RuntimeException(e); }
            stage.setScene(scene);
            stage.show();

        });

        // регистрация
        signUpButton.setOnAction(actionEvent -> {

            while (true) {

                // проверка фамилии на пустоту
                String lastName = null;
                try { lastName = signUpLastName.getText().trim(); }
                catch (Exception e) {
                    System.out.println("Error: last name is empty.");
                    break;
                }

                // проверка имени на пустоту
                String firstName = null;
                try { firstName = signUpFirstName.getText().trim(); }
                catch (Exception e) {
                    System.out.println("Error: first name is empty.");
                    break;
                }

                String secondName = "";
                try { secondName = signUpSecondName.getText().trim(); } catch (Exception ignored) { }

                // проверка адреса на пустоту
                String address = null;
                try { address = signUpAddress.getText().trim(); }
                catch (Exception e) {
                    System.out.println("Error: address is empty.");
                    break;
                }

                // проверка номера телефона на пустоту
                String phoneNumber = null;
                try { phoneNumber = signUpPhoneNumber.getText().trim(); }
                catch (Exception e) {
                    System.out.println("Error: empty phone number.");
                    break;
                }

                // проверка номера телефона на корректность (вид +7 (012)-345-67-89)
                try {
                    String regex = "\\+7\\s\\([0-9]{3}\\)-[0-9]{3}-[0-9]{2}-[0-9]{2}";
                    if (!phoneNumber.matches(regex)) {
                        System.out.println("Error: incorrect phone number");
                        break;
                    }
                }
                catch (Exception e) {
                    System.out.println("Error: incorrect phone number.");
                    break;
                }

                // проверка логина на пустоту
                String login = null;
                try { login = signUpLogin.getText(); }
                catch (Exception e) {
                    System.out.println("Error: login is empty.");
                    break;
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
                        break;
                    }

                    statement = databaseHandler.getDbConnection().prepareStatement(query_employees);
                    statement.setString(1, login);
                    result = statement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                        break;
                    }

                } catch (SQLException | ClassNotFoundException e) { throw new RuntimeException(e); }

                // проверка пароля на пустоту
                String password = null;
                try { password = signUpPassword.getText().trim(); }
                catch (Exception e) {
                    System.out.println("Error: password is empty.");
                    break;
                }

                // регистрация
                try {

                    signUpClient(lastName, firstName, secondName, address, phoneNumber, login, password);
                    System.out.println("Registration successful.");

                    // смена окна на авторизацию
                    FXMLLoader loader  =  new FXMLLoader(Main.class.getResource("signIn.fxml"));

                    try { loader.load(); }
                    catch (IOException ignored) {}

                    signInWindowButton.getScene().getWindow().hide();

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                    break;

                } catch (SQLException | ClassNotFoundException e) { throw new RuntimeException(e); }
            }
        });

    }

    public void signUpClient(String lastName, String firstName, String secondName, String address, String phoneNumber, String login, String password)
            throws SQLException, ClassNotFoundException {

        String insertNew = "INSERT INTO " + CLIENTS_TABLE + " (" + CLIENTS_LAST_NAME + ", " +
                CLIENTS_FIRST_NAME + ", " + CLIENTS_SECOND_NAME + ", " + CLIENTS_ADDRESS + ", " +
                CLIENTS_PHONE_NUMBER + ", " + CLIENTS_LOGIN + ", " + CLIENTS_PASSWORD +
                ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
        preparedStatement.setString(1, lastName);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, secondName);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, phoneNumber);
        preparedStatement.setString(6, login);
        preparedStatement.setString(7, password);
        preparedStatement.executeUpdate();

    }
}
