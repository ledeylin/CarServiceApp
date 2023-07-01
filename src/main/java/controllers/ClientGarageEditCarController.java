package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.util.Objects;

public class ClientGarageEditCarController extends Constants {

    @FXML
    private Button button_save;

    @FXML
    private TextField signUpLicensePlate;

    @FXML
    private TextField text_car_make;

    @FXML
    private TextField text_car_model;

    @FXML
    private Text text_mistake;

    private static String old_license_plate;

    private static String license_plate;

    private static String make;

    private static String model;

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    public static void setOld_license_plate(String old_license_plate) {
        ClientGarageEditCarController.old_license_plate = old_license_plate;
    }
    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        button_save.setOnAction(actionEvent -> {

            boolean flag = true;

            try {
                license_plate = signUpLicensePlate.getText().trim();
                make = text_car_make.getText().trim();
                model = text_car_model.getText().trim();
            }
            catch (Exception ignored) { }

//            if (Objects.equals(license_plate, "")) {
//                flag = false;
//                text_mistake.setText("Вы не ввели гос.номер!");
//            }
//
//            if (Objects.equals(make, "")) {
//                flag = false;
//                text_mistake.setText("Вы не ввели марку машины!");
//            }
//
//            if (Objects.equals(model, "")) {
//                flag = false;
//                text_mistake.setText("Вы не ввели модель машины!");
//            }

            // проверка пароля
            if (flag) {
                Stage stage = new Stage();
                PassController.setId(12);
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pass.fxml"));
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

    public static void save() {

        // гос.номер
        if (!Objects.equals(license_plate, "")) {

            String sqlAlterTable = "UPDATE " + CARS_TABLE + " SET " +
                    CARS_LICENSE_PLATE + " = '" +
                    license_plate + "' WHERE " + CARS_LICENSE_PLATE +
                    " = '" + old_license_plate + "';";

            old_license_plate = license_plate;

            try {
                Connection connection = databaseHandler.getDbConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

        // марка
        if (!Objects.equals(make, "")) {

            String sqlAlterTable = "UPDATE " + CARS_TABLE + " SET " +
                    CARS_MAKE + " = '" +
                    make + "' WHERE " + CARS_LICENSE_PLATE +
                    " = '" + old_license_plate + "';";

            try {
                Connection connection = databaseHandler.getDbConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

        // модель
        if (!Objects.equals(model, "")) {

            String sqlAlterTable = "UPDATE " + CARS_TABLE + " SET " +
                    CARS_MODEL + " = '" +
                    model + "' WHERE " + CARS_LICENSE_PLATE +
                    " = '" + old_license_plate + "';";

            try {
                Connection connection = databaseHandler.getDbConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlAlterTable);
                System.out.println("Success!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
