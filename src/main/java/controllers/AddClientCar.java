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

import static controllers.ClientGarage.now;

public class AddClientCar extends Constants{

    @FXML
    private Button signUpButton;

    @FXML
    private TextField signUpLicensePlate;

    @FXML
    private TextField text_car_make;

    @FXML
    private TextField text_car_model;

    private static String license_plate;

    private static String car_make;

    private static String car_model;

    private boolean flag = true;

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        // сохранение
        signUpButton.setOnAction(actionEvent -> {

            // проверка гос.номера
            license_plate = signUpLicensePlate.getText().trim();
            if (license_plate.equals("")) {
                System.out.println("Error: empty license plate.");
                flag = false;
            }

            // проверка модели машины
            car_model = text_car_model.getText().trim();
            if (car_model.equals("")) {
                System.out.println("Error: empty car model.");
                flag = false;
            }

            // проверка марки машины
            car_make = text_car_make.getText().trim();
            if (car_make.equals("")) {
                System.out.println("Error: empty car make.");
                flag = false;
            }

            if (flag) {
                // проверка пароля
                Stage stage = new Stage();
                MainPass.setId(13);
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

        String insertNew = "INSERT INTO " + CAR_TABLE + " (" + CAR_ID_OWNER + ", " +
                CAR_LICENSE_PLATE + ", " + CAR_MAKE + ", " + CAR_MODEL + ", status" +
                ") VALUES(?, ?, ?, ?, ?)";

        System.out.println(insertNew);

        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
        preparedStatement.setString(1, ClientAcc.getLogin());
        preparedStatement.setString(2, license_plate);
        preparedStatement.setString(3, car_make);
        preparedStatement.setString(4, car_model);
        preparedStatement.setString(5, "1");
        preparedStatement.executeUpdate();

    }

}
