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

public class EditClientCar extends Constants{

    @FXML
    private Button signUpButton;

    @FXML
    private TextField signUpLicensePlate;

    @FXML
    private TextField text_car_make;

    @FXML
    private TextField text_car_model;

    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private static String license_plate = "";

    private static String car_make = "";

    private static String car_model = "";

    private static String old_license_plate = "";

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        signUpButton.setOnAction(actionEvent -> {

            try {
                license_plate = signUpLicensePlate.getText().trim();
                car_make = text_car_make.getText().trim();
                car_model = text_car_model.getText().trim();
            }
            catch (Exception ignored) { }

            // проверка пароля
            Stage stage = new Stage();
            MainPass.setId(12);
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

        // гос.номер
        if (!Objects.equals(license_plate, "")) {

            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + CAR_TABLE + " SET " + CAR_LICENSE_PLATE + " = '" + license_plate
                    + "' WHERE " + CAR_LICENSE_PLATE + " = '" + old_license_plate + "';";

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

            old_license_plate = license_plate;
        }

        // модель машины
        if (!Objects.equals(car_model, "")) {

            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + CAR_TABLE + " SET " + CAR_MODEL + " = '" + car_model
                    + "' WHERE " + CAR_LICENSE_PLATE + " = '" + old_license_plate + "';";

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

        // марка машины
        if (!Objects.equals(car_make, "")) {

            String sqlAlterTable = null;
            sqlAlterTable = "UPDATE " + CAR_TABLE + " SET " + CAR_MAKE + " = '" + car_make
                    + "' WHERE " + CAR_LICENSE_PLATE + " = '" + old_license_plate + "';";

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

    }

    public static void setOld_license_plate(String old_license_plate) {
        EditClientCar.old_license_plate = old_license_plate;
    }

}
