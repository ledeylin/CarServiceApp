package main;

import controllers.SignInController;

import java.sql.*;

public class SaveInformation extends Constants{

    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private static String text_login;
    private static String text_pass;

    public static void setText_pass(String text_pass) {
        SaveInformation.text_pass = text_pass;
    }

    public static void setText_login(String text_login) {
        SaveInformation.text_login = text_login;
    }

    public static void save() throws SQLException, ClassNotFoundException {

        // логин
        if (text_login != "") {
            try {
                String query = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =?";
                PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, text_login);
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    System.out.println("Error: login already exist.");
                }
                else {
                    query = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " =?";
                    preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, text_login);
                    result = preparedStatement.executeQuery();
                    if (result.next()) {
                        System.out.println("Error: login already exist.");
                    }
                    else {
                        String sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_LOGIN + " = '" + text_login
                                + "' WHERE " + EMPLOYEE_LOGIN + " = '" + SignInController.user.getLogin() + "';";
                        Connection connection = databaseHandler.getDbConnection();
                        System.out.println(sqlAlterTable);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(sqlAlterTable);
                        SignInController.user.setLogin(text_login);
                        System.out.println("Success!");
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Error: incorrect login.");
            }
        }

        // пароль
        if (text_pass != "") {
            String sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_PASSWORD + " = '" + text_pass
                    + "' WHERE " + EMPLOYEE_LOGIN + " = '" + SignInController.user.getLogin() + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            SignInController.user.setPassword(text_pass);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

    }
}
