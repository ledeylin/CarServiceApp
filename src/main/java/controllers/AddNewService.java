package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class AddNewService extends Constants {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpButton;

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
    private DatePicker text_start_date;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        // сохранение
        signUpButton.setOnAction(actionEvent -> {
            while (true) {

                // проверка серийного номера
                String serial_number = null;
                try {
                    serial_number = signUpDetailSerialNumber.getText().trim();
                } catch (Exception e) {
                    System.out.println("Error: empty serial number.");
                    break;
                }

                // проверка логина работника
                String employee = null;
                try {
                    employee = signUpEmployee.getText().trim();
                } catch (Exception e) {
                    System.out.println("Error: empty login employee.");
                    break;
                }

                // проверка номера машины
                String license_plate = "";
                try {
                    license_plate = signUpLicensePlate.getText().trim();
                } catch (Exception ignored) {
                }

                // проверка пробега машины
                String mileage = null;
                try {
                    mileage = signUpMileage.getText().trim();
                } catch (Exception e) {
                    System.out.println("Error: empty address.");
                    break;
                }

                // проверка времени работы
                String work_time = null;
                try { work_time = signUpWorkTime.getText().trim(); }
                catch (Exception e) {
                    System.out.println("Error: empty phone number.");
                    break;
                }

                // сама регистрация
                try {
                    addService(serial_number, employee, license_plate, mileage, work_time);
                    System.out.println("Successful.");
                    signUpButton.getScene().getWindow().hide();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private String insertNew = "INSERT INTO " + SERVICE_TABLE + " (" + SERVICE_ID_EMPLOYEE +  ", " +
            SERVICE_MILEAGE + ", " + SERVICE_WORK_TIME + ", " + SERVICE_DETAIL_SERIAL_NUMBER + ", " +
            SERVICE_START_DATE + ", " + SERVICE_FINAL_DATE + ", " + SERVICE_LICENSE_PLATE +
            ") VALUES(?, ?, ?, ?, ?, ?, ?)";

    public void addService (String serial_number, String employee, String license_plate,
                            String mileage, String work_time)
            throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
        preparedStatement.setString(1, employee);
        preparedStatement.setString(2, mileage);
        preparedStatement.setString(3, work_time);
        preparedStatement.setString(4, serial_number);
        preparedStatement.setDate(5, Date.valueOf(text_start_date.getValue()));
        preparedStatement.setDate(6, Date.valueOf(text_final_date.getValue()));
        preparedStatement.setString(7, license_plate);
        preparedStatement.executeUpdate();
    }

}
