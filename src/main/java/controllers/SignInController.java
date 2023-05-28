package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableRow;
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
    private Button signInButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpWindowButton;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        signUpWindowButton.setOnAction(actionEvent -> {
            FXMLLoader  loader  =  new FXMLLoader(Main.class.getResource("signUp.fxml"));

            try {
                loader.load();
            } catch (IOException ignored) {}

            signUpWindowButton.getScene().getWindow().hide();

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });

        signInButton.setOnAction(actionEvent -> {
            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();
            
            if (!login.equals("") && !password.equals("")) {
                try {
                    signInUser(login, password);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else System.out.println("Error: login or password is epmty.");
        });
    }

    private void signInUser(String login, String password) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_LOGIN + " =? AND " + USERS_PASSWORD + " =?";

        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        statement.setString(1, login);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();

        while(result.next()){
            System.out.println(result.getString(USERS_FIRST_NAME));
        }
    }
}
