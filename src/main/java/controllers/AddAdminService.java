package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class AddAdminService extends Constants {

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

    private static String serial_number = "";

    private static String employee = "";

    private static String license_plate = "";

    private static String mileage = "";

    private static String work_time = "";

    private static Date start_date= null;

    private static Date final_date = null;

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        // сохранение
        signUpButton.setOnAction(actionEvent -> {

            while (true) {

                // проверка серийного номера
                serial_number = signUpDetailSerialNumber.getText().trim();
                if (serial_number.equals("")) {
                    System.out.println("Error: empty serial number.");
                    break;
                }

                // проверка логина работника
                employee = signUpEmployee.getText().trim();
                if (employee.equals("")) {
                    System.out.println("Error: empty login employee.");
                    break;
                }

                // проверка номера машины
                license_plate = signUpLicensePlate.getText().trim();
                if (license_plate.equals("")) {
                    System.out.println("Error: empty license plate.");
                    break;
                }

                // проверка пробега машины
                mileage = signUpMileage.getText().trim();
                if (mileage.equals("")) {
                    System.out.println("Error: empty mileage.");
                    break;
                }

                // проверка времени работы
                work_time = signUpWorkTime.getText().trim();
                if (work_time.equals("")) {
                    System.out.println("Error: empty work time.");
                    break;
                }

                // проверка даты начала
                try { start_date = Date.valueOf(text_start_date.getValue()); } catch (Exception ignored) { }
                if (start_date == null) {
                    System.out.println("Error: incorrect start date.");
                    break;
                }

                // проверка даты конца
                try { final_date = Date.valueOf(text_final_date.getValue()); } catch (Exception ignored) { }
                if (final_date == null) {
                    System.out.println("Error: incorrect final date.");
                    break;
                }

                // проверка пароля
                Stage stage = new Stage();
                MainPass.setId(3);
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main_pass.fxml"));
                Scene scene = null;
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

    public static void add()
            throws SQLException, ClassNotFoundException {

        String insertNew = "INSERT INTO " + SERVICE_TABLE + " (" + SERVICE_ID_EMPLOYEE + ", " +
                SERVICE_MILEAGE + ", " + SERVICE_WORK_TIME + ", " + SERVICE_DETAIL_SERIAL_NUMBER + ", " +
                SERVICE_START_DATE + ", " + SERVICE_FINAL_DATE + ", " + SERVICE_LICENSE_PLATE +
                ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        System.out.println(insertNew);

        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
        preparedStatement.setString(1, employee);
        preparedStatement.setString(2, mileage);
        preparedStatement.setString(3, work_time);
        preparedStatement.setString(4, serial_number);
        preparedStatement.setDate(5, start_date);
        preparedStatement.setDate(6, final_date);
        preparedStatement.setString(7, license_plate);
        preparedStatement.executeUpdate();

    }

}
