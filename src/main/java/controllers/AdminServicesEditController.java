package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Objects;

public class AdminServicesEditController extends Constants {

    @FXML
    private Button button_save;

    @FXML
    private TextField signUpDetailSerialNumber;

    @FXML
    private TextField signUpEmployee;

    @FXML
    private TextField signUpLicensePlate;

    @FXML
    private TextField signUpMileage;

    @FXML
    private TextField signUpWorkTime;

    @FXML
    private DatePicker text_final_date;

    @FXML
    private Text text_mistake;

    @FXML
    private DatePicker text_start_date;

    private static LocalDate startDate;

    private static LocalDate finalDate;

    private static int id;

    private static String detailSerialNumber = "";

    private static String employeeLogin = "";

    private static String licensePlate = "";

    private static String mileage = "";

    private static String workTime = "";

    private boolean flag = true;

    public static void setStartDate(LocalDate startDate) {
        AdminServicesEditController.startDate = startDate;
    }

    public static void setFinalDate(LocalDate finalDate) {
        AdminServicesEditController.finalDate = finalDate;
    }

    public static void setId(int id) {
        AdminServicesEditController.id = id;
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        button_save.setOnAction(actionEvent -> {

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

            // начальная дата
            if (startDate.isAfter(finalDate)) {
                flag = false;
                text_mistake.setText("Дата начала работы не может быть позже конца");
            }

            // проверка пароля
            if (flag) {
                PassController.setId(1);
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

    public static void save() {

        // логин сотрудников
        if (!Objects.equals(employeeLogin, "")) {

            String sqlAlterTable;
            sqlAlterTable = "UPDATE " + SERVICES_TABLE + " SET " + SERVICES_ID_EMPLOYEE + " = '" + employeeLogin
                    + "' WHERE " + SERVICES_ID + " = '" + id + "';";

            try {
                Connection connection;
                connection = DatabaseHandler.getInstance();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

        // серийный номер детали
        if (!Objects.equals(detailSerialNumber, "")) {

            String sqlAlterTable;
            sqlAlterTable = "UPDATE " + SERVICES_TABLE + " SET " + SERVICES_DETAIL_SERIAL_NUMBER + " = '" + detailSerialNumber
                    + "' WHERE " + SERVICES_ID + " = '" + id + "';";

            try {
                Connection connection;
                connection = DatabaseHandler.getInstance();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

        // номер машины
        if (!Objects.equals(licensePlate, "")) {

            String sqlAlterTable;
            sqlAlterTable = "UPDATE " + SERVICES_TABLE + " SET " + SERVICES_LICENSE_PLATE + " = '" + licensePlate
                    + "' WHERE " + SERVICES_ID + " = '" + id + "';";

            try {
                Connection connection;
                connection = DatabaseHandler.getInstance();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

        // пробег машины
        if (!Objects.equals(mileage, "")) {

            String sqlAlterTable;
            int end_mileage;
            try {
                end_mileage = Integer.parseInt(mileage);
                sqlAlterTable = "UPDATE " + SERVICES_TABLE + " SET " + SERVICES_MILEAGE + " = '" + end_mileage
                        + "' WHERE " + SERVICES_ID + " = '" + id + "';";

                try {
                    Connection connection;
                    connection = DatabaseHandler.getInstance();
                    Statement statement = connection.createStatement();
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

            String sqlAlterTable;
            int end_workTime;
            try {
                end_workTime = Integer.parseInt(workTime);
                sqlAlterTable = "UPDATE " + SERVICES_TABLE + " SET " + SERVICES_WORK_TIME + " = '" + end_workTime
                        + "' WHERE " + SERVICES_ID + " = '" + id + "';";

                try {
                    Connection connection;
                    connection = DatabaseHandler.getInstance();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sqlAlterTable);
                    System.out.println("Success!");
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: incorrect work time");
            }
        }

        // дата
        if (startDate.isBefore(finalDate)) {

            String sqlAlterTable;
            sqlAlterTable = "UPDATE " + SERVICES_TABLE + " SET " + SERVICES_START_DATE + " = '" + startDate
                    + "' WHERE " + SERVICES_ID + " = '" + id + "';";

            try {
                Connection connection;
                connection = DatabaseHandler.getInstance();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

            sqlAlterTable = "UPDATE " + SERVICES_TABLE + " SET " + SERVICES_FINAL_DATE + " = '" + finalDate
                    + "' WHERE " + SERVICES_ID + " = '" + id + "';";

            try {
                Connection connection = DatabaseHandler.getInstance();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("Дата начала должна быть раньше, чем дата окончания");
        }

        Main.changeScene("admin_services.fxml");

    }
}