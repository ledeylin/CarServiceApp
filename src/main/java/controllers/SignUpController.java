package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
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
    private Button signUpButton;

    @FXML
    private DatePicker signUpDateOfBirth;

    @FXML
    private CheckBox signUpFemale;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpLogin;

    @FXML
    private CheckBox signUpMale;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpSecondName;

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

        // реализация возможности выбора только одного пола (чуть-чуть криво работает, приходится нажимать два раза)
        signUpMale.setOnAction(actionEvent ->  {
            if (signUpFemale.isSelected()) signUpFemale.fire();
        });
        signUpFemale.setOnAction(actionEvent ->  {
            if (signUpMale.isSelected()) signUpMale.fire();
        });

        // регистрация в приложении
        signUpButton.setOnAction(actionEvent -> {
            while (true) {

                // проверки на корректную дату рождения
                LocalDate dateOfBirth = null;
                try {
                    dateOfBirth = signUpDateOfBirth.getValue();
                    if (LocalDate.now().isBefore(dateOfBirth)) {
                        System.out.println("Error: incorrect date.");
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error: incorrect date.");
                    break;
                }

                // проверка на существование логина
                try {
                    String login = signUpLogin.getText();
                    String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_LOGIN + " =?";
                    PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
                    statement.setString(1, login);
                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                        break;
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("Error: incorrect login.");
                }

                // проверка на то, введён ли пол
                String gender = null;
                if (signUpMale.isSelected()) gender = "Male";
                if (signUpFemale.isSelected()) gender = "Female";
                else {
                    System.out.println("Error: please select gender.");
                    break;
                }

                // реализация возможности не вводить second name
                String secondName = "";
                try { secondName = signUpSecondName.getText(); }
                catch (Exception ignored) {}

                // остальные данные без проверок
                String firstName = signUpFirstName.getText();
                String lastName = signUpLastName.getText();
                String login = signUpLogin.getText();
                String password = signUpPassword.getText();

                try {
                    signUpUser(firstName, secondName, lastName, gender, dateOfBirth, login, password);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                // сама регистрация
                try {
                    signUpUser(firstName, secondName, lastName, gender, dateOfBirth, login, password);
                    System.out.println("Registration successful.");
                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("Error: unidentified error.");
                    break;
                }

                // смена окна на авторизацию
                FXMLLoader loader  =  new FXMLLoader(Main.class.getResource("signIn.fxml"));

                try { loader.load(); }
                catch (IOException ignored) {}

                signInWindowButton.getScene().getWindow().hide();

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();

            }
        });

    }

    public String insertNew = "INSERT INTO " + USERS_TABLE + " (" + USERS_FIRST_NAME +  ", " + USERS_SECOND_NAME +
            ", " + USERS_LAST_NAME + ", " + USERS_GENDER + ", " + USERS_DATE_OF_BIRTH + ", " + USERS_LOGIN + ", " +
            USERS_PASSWORD + ", " + USERS_ACCESS_RIGHTS + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

    public void signUpUser(String firstName, String secondName, String lastName,
                           String gender, LocalDate dateOfBirth, String login, String password) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, secondName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, gender);
        preparedStatement.setDate(5, Date.valueOf(dateOfBirth));
        preparedStatement.setString(6, login);
        preparedStatement.setString(7, password);
        preparedStatement.setInt(8, 0);
        preparedStatement.executeUpdate();
    }

}
