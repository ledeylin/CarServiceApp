package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.*;
import javafx.scene.text.Text;

public class AdminServices extends Constants {

    @FXML
    private Button button_edit;

    @FXML
    private Button button_personal_acc;

    @FXML
    private Button button_personal_edit;

    @FXML
    private Button button_personal_service;

    @FXML
    private Button personal_clients;

    @FXML
    private Button personal_employees;

    @FXML
    private Text text_car_model;

    @FXML
    private Text text_car_owner;

    @FXML
    private Text text_detail;

    @FXML
    private Text text_detail_serial_number;

    @FXML
    private Text text_final_date;

    @FXML
    private Text text_login_employee;

    @FXML
    private Text text_pass;

    @FXML
    private Text text_phone;

    @FXML
    private Text text_price;

    @FXML
    private Text text_start_date;

    @FXML
    private ListView<String> list_view;

    private static int id;

    private static LocalDate start__date;

    private static LocalDate final__date;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации об услугах
        TreeMap<String, Integer> services = new TreeMap<>();
        String query = "SELECT * FROM " + SERVICE_TABLE;
        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery();
        int i = 0;
        while (result.next()) {
            services.put(result.getDate(SERVICE_START_DATE) + " / " + result.getDate(SERVICE_FINAL_DATE), i);
            i++;
        }
        ArrayList<String> all = new ArrayList<>();
        statement = databaseHandler.getDbConnection().prepareStatement(query);
        result = statement.executeQuery();
        while (result.next()) {
            all.add(result.getDate(SERVICE_START_DATE) + " / " + result.getDate(SERVICE_FINAL_DATE));
        }
        list_view.getItems().addAll(all);

        // добавление в массивы для последующего вывода
        TreeMap<Integer, String> login_employee = new TreeMap<>();
        TreeMap<Integer, String> start_date = new TreeMap<>();
        TreeMap<Integer, String> final_date = new TreeMap<>();
        TreeMap<Integer, Integer> price = new TreeMap<>();
        TreeMap<Integer, String> car_model = new TreeMap<>();
        TreeMap<Integer, String> detail_serial_number = new TreeMap<>();
        TreeMap<Integer, String> detail = new TreeMap<>();
        TreeMap<Integer, String> car_owner = new TreeMap<>();
        TreeMap<Integer, Integer> id_service = new TreeMap<>();
        query = "SELECT employees.login AS employee_login, service.start_date, service.final_date," +
                "details.price, car.model AS car_model, details.serial_number AS detail_serial_number," +
                "clients.login AS car_owner, details.category AS detail, service.id_service AS id_service\n" +
                "FROM service\n" +
                "INNER JOIN employees ON service.id_employee = employees.login\n" +
                "INNER JOIN car ON service.license_plate = car.license_plate\n" +
                "INNER JOIN clients ON car.id_owner = clients.login\n" +
                "INNER JOIN details_compatibility ON car.model = details_compatibility.model\n" +
                "INNER JOIN details ON details_compatibility.detail_serial_number = details.serial_number AND service.detail_serial_number = details.serial_number;";
        result = statement.executeQuery(query);
        for (i = 0; i < services.size(); i++) {
            if (result.next()) {
                login_employee.put(i, result.getString("employee_login"));
                start_date.put(i, result.getString("start_date"));
                final_date.put(i, result.getString("final_date"));
                price.put(i, (result.getInt(DETAILS_PRICE)));
                car_model.put(i, result.getString("car_model"));
                detail_serial_number.put(i, result.getString("detail_serial_number"));
                car_owner.put(i, result.getString("car_owner"));
                detail.put(i, result.getString("detail"));
                id_service.put(i, result.getInt("id_service"));
            }
        }
        list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s1) {
                text_detail_serial_number.setText(detail_serial_number.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_car_owner.setText(car_owner.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_car_model.setText(car_model.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_price.setText(String.valueOf(price.get(services.get(list_view.getSelectionModel().getSelectedItem()))));
                text_login_employee.setText(login_employee.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_start_date.setText(start_date.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_final_date.setText(final_date.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_detail.setText(detail.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                id = id_service.get(services.get(list_view.getSelectionModel().getSelectedItem()));
                start__date = LocalDate.parse(start_date.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                final__date = LocalDate.parse(final_date.get(services.get(list_view.getSelectionModel().getSelectedItem())));
            }
        });

        // редактирование информации об услуге
        button_edit.setOnAction(actionEvent -> {
            SaveInformationServices.setId(id);
            EditServices.setStartDate(start__date);
            EditServices.setFinalDate(final__date);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editServices.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 529, 267);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

        // переход на окно редактирования информации
        button_personal_edit.setOnAction(actionEvent -> {
            button_personal_edit.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit_account_admin.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

        // переход на окно личного кабинета
        button_personal_acc.setOnAction(actionEvent -> {
            button_personal_acc.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("personal_account_admin.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

        // переход на окно клиентов
        personal_clients.setOnAction(actionEvent -> {
            personal_clients.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("clients_admin.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

        // переход на окно работников
        personal_employees.setOnAction(actionEvent -> {
            personal_employees.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_admin.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

    }

}