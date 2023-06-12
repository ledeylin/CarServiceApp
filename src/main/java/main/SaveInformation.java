package main;

import controllers.SignInController;

import java.sql.*;
import java.util.Objects;

public class SaveInformation extends Constants{

    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private static String text_login;
    private static String text_old_login;
    private static String text_pass;
    private static String last_name;
    private static String first_name;
    private static String second_name;
    private static String address;
    private static String phone;
    private static int permissions;

    public static void setText_pass(String text_pass) {
        SaveInformation.text_pass = text_pass;
    }

    public static void setText_old_login(String text_old_login) {
        SaveInformation.text_old_login = text_old_login;
    }

    public static void setText_login(String text_login) {
        SaveInformation.text_login = text_login;
    }

    public static void setText_permissions(int permissions) {
        SaveInformation.permissions = permissions;
    }

    public static void setLast_name(String last_name) {
        SaveInformation.last_name = last_name;
    }

    public static void setFirst_name(String first_name) {
        SaveInformation.first_name = first_name;
    }

    public static void setSecond_name(String second_name) {
        SaveInformation.second_name = second_name;
    }

    public static void setAddress(String address) {
        SaveInformation.address = address;
    }

    public static void setPhone(String phone) {
        SaveInformation.phone = phone;
    }

    public static void save() throws SQLException, ClassNotFoundException {

        // логин
        if (!Objects.equals(text_login, "")) {
            try {
                String query = "SELECT * FROM " + CLIENTS_TABLE + " WHERE " + CLIENTS_LOGIN + " =?";
                PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, text_login);
                ResultSet result = preparedStatement.executeQuery();
                String sqlAlterTable = null;
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
                    } else if (permissions == 1) {
                        sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_LOGIN + " = '" + text_login
                                + "' WHERE " + EMPLOYEE_LOGIN + " = '" + text_old_login + "';";
                    } else if (permissions == 0) {
                        sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " + CLIENTS_LOGIN + " = '" + text_login
                                + "' WHERE " + CLIENTS_LOGIN + " = '" + text_old_login + "';";
                    }
                }
                Connection connection = databaseHandler.getDbConnection();
                System.out.println(sqlAlterTable);
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
                text_old_login = text_login;
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Error: incorrect login.");
            }
        }

        // пароль
        if (!Objects.equals(text_pass, "")) {
            String sqlAlterTable = null;
            if (permissions == 1) {
                sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_PASSWORD + " = '" + text_pass
                        + "' WHERE " + EMPLOYEE_LOGIN + " = '" + text_old_login + "';";
            }
            else {
                sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " + CLIENTS_PASSWORD + " = '" + text_pass
                        + "' WHERE " + CLIENTS_LOGIN + " = '" + text_old_login + "';";
            }
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // фамилия
        if (!Objects.equals(last_name, "")) {
            String sqlAlterTable = null;
            if (permissions == 1) {
                sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_LAST_NAME + " = '" + last_name
                        + "' WHERE " + EMPLOYEE_LOGIN + " = '" + text_old_login + "';";
            }
            else {
                sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " + CLIENTS_LAST_NAME + " = '" + last_name
                        + "' WHERE " + CLIENTS_LOGIN + " = '" + text_old_login + "';";
            }
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // имя
        if (!Objects.equals(first_name, "")) {
            String sqlAlterTable = null;
            if (permissions == 1) {
                sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_FIRST_NAME + " = '" + first_name
                        + "' WHERE " + EMPLOYEE_LOGIN + " = '" + text_old_login + "';";
            }
            else {
                sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " + CLIENTS_FIRST_NAME + " = '" + first_name
                        + "' WHERE " + CLIENTS_LOGIN + " = '" + text_old_login + "';";
            }
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // отчество
        if (!Objects.equals(second_name, "")) {
            String sqlAlterTable = null;
            if (permissions == 1) {
                sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_SECOND_NAME + " = '" + second_name
                        + "' WHERE " + EMPLOYEE_LOGIN + " = '" + text_old_login + "';";
            }
            else {
                sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " + CLIENTS_SECOND_NAME + " = '" + second_name
                        + "' WHERE " + CLIENTS_LOGIN + " = '" + text_old_login + "';";
            }
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // адрес
        if (!Objects.equals(address, "")) {
            String sqlAlterTable = null;
            if (permissions == 1) {
                sqlAlterTable = "UPDATE " + EMPLOYEE_TABLE + " SET " + EMPLOYEE_ADDRESS + " = '" + address
                        + "' WHERE " + EMPLOYEE_LOGIN + " = '" + text_old_login + "';";
            }
            else {
                sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " + CLIENTS_ADDRESS + " = '" + address
                        + "' WHERE " + CLIENTS_LOGIN + " = '" + text_old_login + "';";
            }
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // номер телефона
        if (!Objects.equals(phone, "")) {
            String sqlAlterTable = "UPDATE " + CLIENTS_TABLE + " SET " + CLIENTS_PHONE_NUMBER + " = '" + phone
                        + "' WHERE " + CLIENTS_LOGIN + " = '" + text_old_login + "';";
            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

    }
}
