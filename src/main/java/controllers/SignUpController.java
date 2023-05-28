package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
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
    private DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        signInWindowButton.setOnAction(actionEvent -> {
            FXMLLoader loader  =  new FXMLLoader(Main.class.getResource("signIn.fxml"));

            try { loader.load(); }
            catch (IOException ignored) {}

            signInWindowButton.getScene().getWindow().hide();

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

        });

        signUpMale.setOnAction(actionEvent ->  {
            if (signUpFemale.isSelected()) signUpFemale.fire();
        });

        signUpFemale.setOnAction(actionEvent ->  {
            if (signUpMale.isSelected()) signUpMale.fire();
        });

        signUpButton.setOnAction(actionEvent -> {
            String gender = null;
            if (signUpMale.isSelected()) gender = "Male";
            if (signUpFemale.isSelected()) gender = "Female";
            String firstName = signUpFirstName.getText();
            String secondName = "";
            try { secondName = signUpSecondName.getText(); }
            catch (Exception ignored) {}
            String lastName = signUpLastName.getText();
            LocalDate dateOfBirth = signUpDateOfBirth.getValue();
            String login = signUpLogin.getText();
            String password = signUpPassword.getText();
            try {
                signUpUser(firstName, secondName, lastName, gender, dateOfBirth, login, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
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
