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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminServicesAddController extends Constants {

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

    private static String serial_number = "";

    private static String employee = "";

    private static String license_plate = "";

    private static String mileage = "";

    private static String work_time = "";

    private static Date start_date= null;

    private static Date final_date = null;

    private boolean flag = true;

    @FXML
    void initialize() {

        // сохранение
        button_save.setOnAction(actionEvent -> {

            // проверка серийного номера
            serial_number = signUpDetailSerialNumber.getText().trim();
            if (serial_number.equals("")) {
                System.out.println("Error: empty serial number.");
                text_mistake.setText("Вы не ввели серийный номер!");
                flag = false;
            }

            // проверка логина работника
            employee = signUpEmployee.getText().trim();
            if (employee.equals("")) {
                System.out.println("Error: empty login employee.");
                text_mistake.setText("Вы не ввели логин работника!");
                flag = false;
            }

            // проверка номера машины
            license_plate = signUpLicensePlate.getText().trim();
            if (license_plate.equals("")) {
                System.out.println("Error: empty license plate.");
                text_mistake.setText("Вы не ввели гос. номер машины!");
                flag = false;
            }

            // проверка пробега машины
            mileage = signUpMileage.getText().trim();
            if (mileage.equals("")) {
                System.out.println("Error: empty mileage.");
                text_mistake.setText("Вы не ввели пробег машины!");
                flag = false;
            }

            // проверка времени работы
            work_time = signUpWorkTime.getText().trim();
            if (work_time.equals("")) {
                System.out.println("Error: empty work time.");
                text_mistake.setText("Вы не ввели время работы!");
                flag = false;
            }

            // проверка даты начала
            try { start_date = Date.valueOf(text_start_date.getValue()); } catch (Exception ignored) { }
            if (start_date == null) {
                System.out.println("Error: incorrect start date.");
                text_mistake.setText("Некорректная дата начала!");
                flag = false;
            }

            // проверка даты конца
            try { final_date = Date.valueOf(text_final_date.getValue()); } catch (Exception ignored) { }
            if (final_date == null) {
                System.out.println("Error: incorrect final date.");
                text_mistake.setText("Некорректная дата конца!");
                flag = false;
            }

            // проверка пароля
            if (flag) {
                PassController.setId(3);
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

    public static void add()
            throws SQLException, ClassNotFoundException {

        String insertNew = "INSERT INTO " + SERVICES_TABLE + " (" + SERVICES_ID_EMPLOYEE + ", " +
                SERVICES_MILEAGE + ", " + SERVICES_WORK_TIME + ", " + SERVICES_DETAIL_SERIAL_NUMBER + ", " +
                SERVICES_START_DATE + ", " + SERVICES_FINAL_DATE + ", " + SERVICES_LICENSE_PLATE +
                ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        System.out.println(insertNew);

        PreparedStatement preparedStatement = DatabaseHandler.getInstance().prepareStatement(insertNew);
        preparedStatement.setString(1, employee);
        preparedStatement.setString(2, mileage);
        preparedStatement.setString(3, work_time);
        preparedStatement.setString(4, serial_number);
        preparedStatement.setDate(5, start_date);
        preparedStatement.setDate(6, final_date);
        preparedStatement.setString(7, license_plate);
        preparedStatement.executeUpdate();

        Main.changeScene("admin_services.fxml");

    }

}

