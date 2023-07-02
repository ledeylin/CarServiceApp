package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class ClientEditController extends Constants {

    @FXML
    private Button button_save;

    @FXML
    private TextField text_login;

    @FXML
    private Text text_mistake;

    @FXML
    private TextField text_pass;

    private static String old_login;

    private static String login = "";

    private static String pass = "";

    private static boolean flag = true;

    @FXML
    void initialize() {

        ClientEditController.old_login = ClientMainController.getLogin();

        // активация кнопки сохранения
        button_save.setOnAction(actionEvent -> {

            ClientEditController.login = text_login.getText().trim();
            ClientEditController.pass = text_pass.getText().trim();

            // логин
            if (!Objects.equals(ClientEditController.login, "")) {
                try {

                    String query = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =?";
                    PreparedStatement preparedStatement = DatabaseHandler.getInstance().prepareStatement(query);
                    preparedStatement.setString(1, ClientEditController.login);
                    ResultSet result = preparedStatement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                        text_mistake.setText("Логин уже существует!");
                        flag = false;
                    }

                    query = "SELECT * FROM " + EMPLOYEES_TABLE + " WHERE " + EMPLOYEES_LOGIN + " =?";
                    preparedStatement = DatabaseHandler.getInstance().prepareStatement(query);
                    preparedStatement.setString(1, ClientEditController.login);
                    result = preparedStatement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                        text_mistake.setText("Логин уже существует!");
                        flag = false;
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("Error: incorrect login.");
                    text_mistake.setText("Логин введён неправильно!");
                    flag = false;
                }
            }

            if (flag) {
                PassController.setId(11);
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

    public static void save() throws SQLException, ClassNotFoundException {

        // логин
        if (!Objects.equals(login, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " +
                    CLIENTS_LOGIN + " = '" + ClientEditController.login + "' WHERE " +
                    CLIENTS_LOGIN + " = '" + ClientEditController.old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
            ClientMainController.getUser().setLogin(login);
            ClientEditController.old_login = ClientEditController.login;
        }

        // пароль
        if (!Objects.equals(pass, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " +
                    CLIENTS_PASSWORD + " = '" + ClientEditController.pass + "' WHERE " +
                    CLIENTS_LOGIN + " = '" + ClientEditController.old_login + "';";
            Connection connection = DatabaseHandler.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
            ClientMainController.getUser().setPassword(pass);
            PassController.setPassword(pass);
        }

        Main.changeScene("client_main.fxml");

    }

}
