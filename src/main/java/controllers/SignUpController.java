package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpController extends Constants {

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
    private TextField signUpLogin;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpPhoneNumber;

    @FXML
    private TextField signUpSecondName;

    @FXML
    private Text text_mistake;

    @FXML
    void initialize()  {

        // переход на окно авторизации
        signInWindowButton.setOnAction(actionEvent -> Main.changeScene("sign_in.fxml"));

        // регистрация
        signUpButton.setOnAction(actionEvent -> {

            boolean flag = true;

            // проверка фамилии на пустоту
            String lastName = null;
            try {
                lastName = signUpLastName.getText().trim();
            } catch (Exception ignored) { }
            if (lastName == null) {
                System.out.println("Error: last name is empty.");
                text_mistake.setText("Вы не ввели фамилию!");
                flag = false;
            }

            // проверка имени на пустоту
            String firstName = null;
            try {
                firstName = signUpFirstName.getText().trim();
            } catch (Exception ignored) { }
            if (firstName == null) {
                System.out.println("Error: first name is empty.");
                text_mistake.setText("Вы не ввели имя!");
                flag = false;
            }

            String secondName = "";
            try {
                secondName = signUpSecondName.getText().trim();
            } catch (Exception ignored) {
            }

            // проверка адреса на пустоту
            String address = null;
            try {
                address = signUpAddress.getText().trim();
            } catch (Exception ignored) { }
            if (address == null) {
                System.out.println("Error: address is empty.");
                text_mistake.setText("Вы не ввели адрес!");
                flag = false;
            }

            // проверка номера телефона на пустоту
            String phoneNumber = null;
            try { phoneNumber = signUpPhoneNumber.getText().trim(); }
            catch (Exception ignored) { }
            if (phoneNumber == null) {
                System.out.println("Error: empty phone number.");
                text_mistake.setText("Вы не ввели номер телефона!");
                flag = false;
            }

            // проверка номера телефона на корректность
            if (phoneNumber != null) {
                try {
                    String regex = "(?:\\+\\d{1,3}|8)[\\s-]?\\(?\\d{3}\\)?[\\s-]?\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{2}";
                    if (!phoneNumber.matches(regex)) {
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

            // проверка логина на пустоту
            String login = null;
            try {
                login = signUpLogin.getText();
            } catch (Exception ignored) { }
            if (login == null) {
                System.out.println("Error: login is empty.");
                text_mistake.setText("Вы не ввели логин!");
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

            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // проверка пароля на пустоту
            String password = null;
            try {
                password = signUpPassword.getText().trim();
            } catch (Exception ignored) { }
            if (password == null) {
                System.out.println("Error: password is empty.");
                text_mistake.setText("Вы не ввели пароль!");
                flag = false;
            }

            // регистрация
            if (flag) {
                try {

                    signUpClient(lastName, firstName, secondName, address, phoneNumber, login, password);
                    System.out.println("Registration successful.");

                    // смена окна на авторизацию
                    Main.changeScene("sign_in.fxml");

                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void signUpClient(String lastName, String firstName, String secondName, String address, String phoneNumber, String login, String password)
            throws SQLException, ClassNotFoundException {

        String insertNew = "INSERT INTO " + CLIENTS_TABLE + " (" + CLIENTS_LAST_NAME + ", " +
                CLIENTS_FIRST_NAME + ", " + CLIENTS_SECOND_NAME + ", " + CLIENTS_ADDRESS + ", " +
                CLIENTS_PHONE_NUMBER + ", " + CLIENTS_LOGIN + ", " + CLIENTS_PASSWORD + ", status" +
                ") VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = DatabaseHandler.getInstance().prepareStatement(insertNew);
        preparedStatement.setString(1, lastName);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, secondName);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, phoneNumber);
        preparedStatement.setString(6, login);
        preparedStatement.setString(7, password);
        preparedStatement.setString(8, "1");
        preparedStatement.executeUpdate();

    }

}
