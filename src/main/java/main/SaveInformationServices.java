package main;

import controllers.SignInController;

import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class SaveInformationServices extends Constants{

    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private static String detailSerialNumber = "";

    private static String employeeLogin = "";

    private static String licensePlate = "";

    private static LocalDate startDate = null;

    private static LocalDate finalDate = null;

    private static int id;

    public static void setDetailSerialNumber(String detailSerialNumber) {
        SaveInformationServices.detailSerialNumber = detailSerialNumber;
    }

    public static void setEmployeeLogin(String employeeLogin) {
        SaveInformationServices.employeeLogin = employeeLogin;
    }

    public static void setStartDate(LocalDate startDate) {
        SaveInformationServices.startDate = startDate;
    }

    public static void setFinalDate(LocalDate finalDate) {
        SaveInformationServices.finalDate = finalDate;
    }

    public static void setLicensePlate(String licensePlate) {
        SaveInformationServices.licensePlate = licensePlate;
    }

    public static void setId(int id) {
        SaveInformationServices.id = id;
    }

    public static void save() throws SQLException, ClassNotFoundException {

        // логин сотрудников
        if (!Objects.equals(employeeLogin, "")) {
            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_ID_EMPLOYEE + " = '" + employeeLogin
                        + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // серийный номер детали
        if (!Objects.equals(detailSerialNumber, "")) {
            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_DETAIL_SERIAL_NUMBER + " = '" + detailSerialNumber
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // номер машины
        if (!Objects.equals(licensePlate, "")) {
            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_LICENSE_PLATE + " = '" + licensePlate
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }

        // начальная дата
        if (startDate.isBefore(finalDate)) {
            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_START_DATE + " = '" + startDate
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }
        else {
            System.out.println("Дата начала должна быть раньше, чем дата окончания");
        }

        // финальная дата
        if (startDate.isAfter(finalDate)) {
            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_FINAL_DATE + " = '" + finalDate
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            Connection connection = databaseHandler.getDbConnection();
            Statement statement = connection.createStatement();
            System.out.println(sqlAlterTable);
            statement.executeUpdate(sqlAlterTable);
            System.out.println("Success!");
        }
        else {
            System.out.println("Дата начала должна быть раньше, чем дата окончания");
        }
    }
}
