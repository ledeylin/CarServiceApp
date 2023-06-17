package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;

public class EditAdminServices extends Constants {

    @FXML
    private Button signUpButton;

    @FXML
    private TextField signUpDetailSerialNumber;

    @FXML
    private TextField signUpEmployee;

    @FXML
    private TextField signUpLicensePlate;

    @FXML
    private DatePicker text_final_date;

    @FXML
    private DatePicker text_start_date;

    @FXML
    private TextField signUpMileage;

    @FXML
    private TextField signUpWorkTime;

    private static LocalDate startDate;

    private static LocalDate finalDate;

    private static int id;

    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private static String detailSerialNumber = "";

    private static String employeeLogin = "";

    private static String licensePlate = "";

    private static String mileage = "";

    private static String workTime = "";

    public static void setStartDate(LocalDate startDate) {
        EditAdminServices.startDate = startDate;
    }

    public static void setFinalDate(LocalDate finalDate) {
        EditAdminServices.finalDate = finalDate;
    }

    public static void setId(int id) {
        EditAdminServices.id = id;
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        signUpButton.setOnAction(actionEvent -> {

            try {
                detailSerialNumber = signUpDetailSerialNumber.getText().trim();
                employeeLogin = signUpEmployee.getText().trim();
                licensePlate = signUpLicensePlate.getText().trim();
                mileage = signUpMileage.getText().trim();
                workTime = signUpWorkTime.getText().trim();
            }
            catch (Exception ignored) { }

            if (text_start_date.getValue() != null) try { startDate = text_start_date.getValue(); }
            catch (Exception ignored) { }
            if (text_final_date.getValue() != null) try { finalDate = text_final_date.getValue(); }
            catch (Exception ignored) { }

            // проверка пароля
            Stage stage = new Stage();
            MainPass.setId(1);
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

    public static void save() {

        // логин сотрудников
        if (!Objects.equals(employeeLogin, "")) {

            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_ID_EMPLOYEE + " = '" + employeeLogin
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            try {
                Connection connection = null;
                connection = databaseHandler.getDbConnection();
                Statement statement = connection.createStatement();
                System.out.println(sqlAlterTable);
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

        // серийный номер детали
        if (!Objects.equals(detailSerialNumber, "")) {

            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_DETAIL_SERIAL_NUMBER + " = '" + detailSerialNumber
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            try {
                Connection connection = null;
                connection = databaseHandler.getDbConnection();
                Statement statement = connection.createStatement();
                System.out.println(sqlAlterTable);
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

        // номер машины
        if (!Objects.equals(licensePlate, "")) {

            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_LICENSE_PLATE + " = '" + licensePlate
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            try {
                Connection connection = null;
                connection = databaseHandler.getDbConnection();
                Statement statement = connection.createStatement();
                System.out.println(sqlAlterTable);
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

        // пробег машины
        if (!Objects.equals(mileage, "")) {

            String sqlAlterTable = null;
            int end_mileage = 0;
            try {
                end_mileage = Integer.parseInt(mileage);
                sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_MILEAGE + " = '" + end_mileage
                        + "' WHERE " + SERVICE_ID + " = '" + id + "';";

                try {
                    Connection connection = null;
                    connection = databaseHandler.getDbConnection();
                    Statement statement = connection.createStatement();
                    System.out.println(sqlAlterTable);
                    statement.executeUpdate(sqlAlterTable);
                    System.out.println("Success!");
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: incorrect mileage");
            }
        }

        // номер машины
        if (!Objects.equals(workTime, "")) {

            String sqlAlterTable = null;
            int end_workTime = 0;
            try {
                end_workTime = Integer.parseInt(workTime);
                sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_WORK_TIME + " = '" + end_workTime
                        + "' WHERE " + SERVICE_ID + " = '" + id + "';";

                try {
                    Connection connection = null;
                    connection = databaseHandler.getDbConnection();
                    Statement statement = connection.createStatement();
                    System.out.println(sqlAlterTable);
                    statement.executeUpdate(sqlAlterTable);
                    System.out.println("Success!");
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: incorrect work time");
            }
        }

        // начальная дата
        if (startDate.isBefore(finalDate)) {

            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_START_DATE + " = '" + startDate
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            try {
                Connection connection = null;
                connection = databaseHandler.getDbConnection();
                Statement statement = connection.createStatement();
                System.out.println(sqlAlterTable);
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("Дата начала должна быть раньше, чем дата окончания");
        }

        // финальная дата
        if (startDate.isAfter(finalDate)) {

            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + SERVICE_TABLE + " SET " + SERVICE_FINAL_DATE + " = '" + finalDate
                    + "' WHERE " + SERVICE_ID + " = '" + id + "';";

            try {
                Connection connection = databaseHandler.getDbConnection();
                Statement statement = connection.createStatement();
                System.out.println(sqlAlterTable);
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("Дата начала должна быть раньше, чем дата окончания");
        }

    }
}
