package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import main.Constants;
import main.DatabaseHandler;
import javafx.scene.text.Text;
import main.Main;

public class AdminServices extends Constants {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button_personal_acc;

    @FXML
    private Button button_personal_edit;

    @FXML
    private Button personal_clients;

    @FXML
    private Button personal_employees;

    @FXML
    private Text text_detail;

    @FXML
    private Text text_mileage;

    @FXML
    private Text text_model;

    @FXML
    private Text text_price;

    @FXML
    private Text text_time;


    @FXML
    private ListView<String> list_view;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации об услугах
        TreeMap<String, Integer> services = new TreeMap<>();
        String query = "SELECT * FROM " + SERVICE_TABLE + " WHERE " + SERVICE_ID_EMPLOYEE + " = '" + SignInController.user.getLogin() + "';";
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
        TreeMap<Integer, String> work_time = new TreeMap<>();
        TreeMap<Integer, String> mileage = new TreeMap<>();
        TreeMap<Integer, String> model = new TreeMap<>();
        TreeMap<Integer, String> detail = new TreeMap<>();
        TreeMap<Integer, Integer> price = new TreeMap<>();
        query = "SELECT " + CAR_TABLE + "." + CAR_MODEL + ", " + DETAILS_TABLE + "." + DETAILS_CATEGORY + ", " +
                DETAILS_TABLE + "." + DETAILS_PRICE + ", " + SERVICE_TABLE + "." + SERVICE_WORK_TIME + ", " +
                SERVICE_TABLE + "." + SERVICE_MILEAGE + " FROM " + SERVICE_TABLE + " INNER JOIN " + CAR_TABLE +
                " ON " + SERVICE_TABLE + "." + SERVICE_LICENSE_PLATE + " = " + CAR_TABLE + "." + CAR_LICENSE_PLATE +
                " INNER JOIN " + DETAILS_TABLE + " ON " + SERVICE_TABLE + "." + SERVICE_DETAIL_SERIAL_NUMBER +
                " = " + DETAILS_TABLE + "." + DETAILS_SERIAL_NUMBER +  " WHERE " + SERVICE_TABLE + "." +
                SERVICE_ID_EMPLOYEE + " = '" + SignInController.user.getLogin() + "';";
        result = statement.executeQuery(query);
        for (i = 0; i < services.size(); i++) {
            if (result.next()) {
                work_time.put(i, result.getString(SERVICE_WORK_TIME) + " ч.");
                mileage.put(i, result.getString(SERVICE_MILEAGE) + " км.");
                model.put(i, result.getString(CAR_MODEL));
                detail.put(i, result.getString(DETAILS_CATEGORY));
                price.put(i, (result.getInt(DETAILS_PRICE)) * 2 / 10);
            }
        }
        list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s1) {
                text_detail.setText(detail.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_mileage.setText(mileage.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_model.setText(model.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_price.setText(String.valueOf(price.get(services.get(list_view.getSelectionModel().getSelectedItem()))));
                text_time.setText(work_time.get(services.get(list_view.getSelectionModel().getSelectedItem())));
            }
        });

        // переход на окно редактирования информации
        button_personal_edit.setOnAction(actionEvent -> {
            button_personal_edit.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit_account_employeers.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("personal_account_employeers.fxml"));
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

    }

}