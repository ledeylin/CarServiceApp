package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class SignInController extends Constants {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox clientButton;

    @FXML
    private CheckBox employeeButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpWindowButton;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        // переход на окно регистрации
        signUpWindowButton.setOnAction(actionEvent -> {

            signUpWindowButton.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signUp.fxml"));
            Scene scene = null;
            try { scene = new Scene(fxmlLoader.load(), 700, 400); }
            catch (IOException e) { throw new RuntimeException(e); }
            stage.setScene(scene);
            stage.show();

        });

        // авторизация
        signInButton.setOnAction(actionEvent -> {

            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();
            
            if (!login.equals("") && !password.equals("")) {
                try { signInUser(login, password); }
                catch (SQLException | ClassNotFoundException e) { throw new RuntimeException(e); }
            }
            else System.out.println("Error: login or password is epmty.");

        });

        // реализация возможности выбора только одного варианта
        clientButton.setOnAction(actionEvent ->  {
            if (employeeButton.isSelected()) employeeButton.fire();
        });
        employeeButton.setOnAction(actionEvent ->  {
            if (clientButton.isSelected()) clientButton.fire();
        });
    }

    private void signInUser(String login, String password) throws SQLException, ClassNotFoundException {

        if (clientButton.isSelected()) {
            String query = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =? AND " + CLIENTS_PASSWORD + " =?";

            PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            while(result.next()){
                System.out.println(result.getString(CLIENTS_FIRST_NAME));
            }

        }

        if (employeeButton.isSelected()) {
            String query = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " =? AND " + EMPLOYEE_PASSWORD + " =?";

            PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            while(result.next()){
                System.out.println(result.getString(EMPLOYEE_FIRST_NAME));
            }

        }
    }
}
