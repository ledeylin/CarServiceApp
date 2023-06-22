package controllers;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import main.SaveInformationPeople;

public class ClientEdit extends Constants {

    @FXML
    private Button button_save;

    @FXML
    private TextField text_login;

    @FXML
    private TextField text_pass;

    private static String old_login;

    private static String login = "";

    private static String pass = "";

    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private static boolean flag;

    @FXML
    void initialize() {

        ClientEdit.old_login = ClientAcc.getLogin();

        // активация кнопки сохранения
        button_save.setOnAction(actionEvent -> {

            ClientEdit.login = text_login.getText().trim();
            ClientEdit.pass = text_pass.getText().trim();

            // логин
            if (!Objects.equals(ClientEdit.login, "")) {
                try {

                    String query = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =?";
                    PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, ClientEdit.login);
                    ResultSet result = preparedStatement.executeQuery();
                    if (result.next()) System.out.println("Error: login already exist.");

                    query = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " =?";
                    preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, ClientEdit.login);
                    result = preparedStatement.executeQuery();
                    if (result.next()) System.out.println("Error: login already exist.");

                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("Error: incorrect login.");
                }
            }

            MainPass.setId(11);
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

        });
    }

    public static void save() throws SQLException, ClassNotFoundException {

        // логин
        if (!Objects.equals(login, "")) {
            String sqlAlterTable = sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " +
                    CLIENTS_LOGIN + " = '" + ClientEdit.login + "' WHERE " +
                    CLIENTS_LOGIN + " = '" + ClientEdit.old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            System.out.println(sqlAlterTable);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
            ClientEdit.old_login = ClientEdit.login;
        }

        // пароль
        if (!Objects.equals(pass, "")) {
            String sqlAlterTable = sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " +
                    CLIENTS_PASSWORD + " = '" + ClientEdit.pass + "' WHERE " +
                    CLIENTS_LOGIN + " = '" + ClientEdit.old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

    }

}