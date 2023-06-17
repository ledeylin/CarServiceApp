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

public class EmployeeEdit extends Constants {

    @FXML
    private Button button_save;

    @FXML
    private TextField text_login;

    @FXML
    private TextField text_pass;

    private static String old_login;

    private static String login;

    private static String pass;

    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private static boolean flag;

    @FXML
    void initialize() {

        EmployeeEdit.old_login = EmployeeAcc.getLogin();

        // активация кнопки сохранения
        button_save.setOnAction(actionEvent -> {

            EmployeeEdit.login = text_login.getText().trim();
            EmployeeEdit.pass = text_pass.getText().trim();

           // логин
            if (!Objects.equals(EmployeeEdit.login, "")) {
                try {

                    String query = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =?";
                    PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, EmployeeEdit.login);
                    ResultSet result = preparedStatement.executeQuery();
                    if (result.next()) System.out.println("Error: login already exist.");

                    query = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " =?";
                    preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, EmployeeEdit.login);
                    result = preparedStatement.executeQuery();
                    if (result.next()) System.out.println("Error: login already exist.");

                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("Error: incorrect login.");
                }
            }

            MainPass.setId(7);
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
            String sqlAlterTable = sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_LOGIN + " = '" + EmployeeEdit.login
                    + "' WHERE " + EMPLOYEE_LOGIN + " = '" + EmployeeEdit.old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            System.out.println(sqlAlterTable);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
            EmployeeEdit.old_login = EmployeeEdit.login;
        }

        // пароль
        if (!Objects.equals(pass, "")) {
            String sqlAlterTable = sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_PASSWORD + " = '" + EmployeeEdit.pass
                    + "' WHERE " + EMPLOYEE_LOGIN + " = '" + EmployeeEdit.old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

    }

}
