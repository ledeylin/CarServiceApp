package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import main.SaveInformation;
import javafx.scene.control.DatePicker;
import main.SaveInformationServices;

public class EditServices {

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

    public static void setStartDate(LocalDate startDate) {
        EditServices.startDate = startDate;
    }

    public static void setFinalDate(LocalDate finalDate) {
        EditServices.finalDate = finalDate;
    }

    @FXML
    void initialize() {
        signUpButton.setOnAction(actionEvent -> {
            String detailSerialNumber = "";
            String employeeLogin = "";
            String licensePlate = "";
            String mileage = "";
            String workTime = "";

            try { detailSerialNumber = signUpDetailSerialNumber.getText().trim(); } catch (Exception ignored) { }
            try { employeeLogin = signUpEmployee.getText().trim(); } catch (Exception ignored) { }
            try { licensePlate = signUpLicensePlate.getText().trim(); } catch (Exception ignored) { }
            try { mileage = signUpMileage.getText().trim(); } catch (Exception ignored) { }
            try { workTime = signUpWorkTime.getText().trim(); } catch (Exception ignored) { }
            if (text_start_date.getValue() != null) try { startDate = text_start_date.getValue(); } catch (Exception ignored) { }
            if (text_final_date.getValue() != null ) try { finalDate = text_final_date.getValue(); } catch (Exception ignored) { }

            SaveInformationServices.setEmployeeLogin(employeeLogin);
            SaveInformationServices.setDetailSerialNumber(detailSerialNumber);
            SaveInformationServices.setStartDate(startDate);
            SaveInformationServices.setFinalDate(finalDate);
            SaveInformationServices.setLicensePlate(licensePlate);
            SaveInformationServices.setMileage(mileage);
            SaveInformationServices.setWorkTime(workTime);

            Stage stage = new Stage();
            PassController.setId(1);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pass.fxml"));
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
}
