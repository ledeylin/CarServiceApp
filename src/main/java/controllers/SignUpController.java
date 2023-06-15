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

public class SignUpController extends Constants {

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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signIn.fxml"));
            Scene scene = null;
            try { scene = new Scene(fxmlLoader.load(), 700, 400); }
            catch (IOException e) { System.out.println("Error: unidentified error."); }
            stage.setScene(scene);
            stage.show();

        });

        // регистрация в приложении
        signUpButton.setOnAction(actionEvent -> {
            while (true) {

                // проверка на фамилию
                String lastName = null;
                try {
                    lastName = signUpLastName.getText().trim();
                } catch (Exception e) {
                    System.out.println("Error: empty last name.");
                    break;
                }

                // проверка на имя
                String firstName = null;
                try {
                    firstName = signUpFirstName.getText().trim();
                } catch (Exception e) {
                    System.out.println("Error: empty first name.");
                    break;
                }

                // проверка на отчетсво
                String secondName = "";
                try {
                    secondName = signUpSecondName.getText().trim();
                } catch (Exception ignored) {
                }

                // проверка на адрес
                String address = null;
                try {
                    address = signUpAddress.getText().trim();
                } catch (Exception e) {
                    System.out.println("Error: empty address.");
                    break;
                }

                // проверка на корректный номер телефона
                String phoneNumber = null;
                try { phoneNumber = signUpPhoneNumber.getText().trim(); }
                catch (Exception e) {
                    System.out.println("Error: empty phone number.");
                    break;
                }
                try {
                    String regex = "\\+7\\s\\([0-9]{3}\\)-[0-9]{3}-[0-9]{2}-[0-9]{2}";
                    if (!phoneNumber.matches(regex)) {
                        System.out.println("Error: incorrect phone number");
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error: incorrect phone number.");
                    break; }


                // проверка на существование логина
                String login = null;
                try { login = signUpLogin.getText(); }
                catch (Exception e) {
                    System.out.println("Error: empty login.");
                    break;
                }
                try {
                    String query = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =?";
                    PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
                    statement.setString(1, login);
                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                        break;
                    }
                    query = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " =?";
                    statement = databaseHandler.getDbConnection().prepareStatement(query);
                    statement.setString(1, login);
                    result = statement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                        break;
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("Error: incorrect login.");
                }

                // проверка на пароль
                String password = null;
                try { password = signUpPassword.getText().trim(); }
                catch (Exception e) {
                    System.out.println("Error: empty password.");
                    break;
                }

                // сама регистрация
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
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private String insertNew = "INSERT INTO " + CLIENTS_TABLE + " (" + CLIENTS_LAST_NAME +  ", " +
            CLIENTS_FIRST_NAME + ", " + CLIENTS_SECOND_NAME + ", " + CLIENTS_ADDRESS + ", " +
            CLIENTS_PHONE_NUMBER + ", " + CLIENTS_LOGIN + ", " + CLIENTS_PASSWORD +
            ") VALUES(?, ?, ?, ?, ?, ?, ?)";

    public void signUpClient(String lastName, String firstName, String secondName, String address, String phoneNumber, String login, String password)
            throws SQLException, ClassNotFoundException {
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
