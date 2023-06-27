package controllers;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

public class AdminServicesController extends Constants {

    @FXML
    private Button button_add;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_edit;

    @FXML
    private Button button_menu1;

    @FXML
    private Button button_menu2;

    @FXML
    private VBox pane_menu;

    @FXML
    private Button personal_acc1;

    @FXML
    private Button personal_acc2;

    @FXML
    private Button personal_clients1;

    @FXML
    private Button personal_clients2;

    @FXML
    private Button personal_employees1;

    @FXML
    private Button personal_employees2;

    @FXML
    private Button button_menu_close;

    @FXML
    private Text text_car_model;

    @FXML
    private Text text_client_login;

    @FXML
    private Text text_detail;

    @FXML
    private Text text_license_plate;

    @FXML
    private Text text_final_date;

    @FXML
    private Text text_login_employee;

    @FXML
    private Text text_price;

    @FXML
    private Text text_serial_number;

    @FXML
    private Text text_start_date;

    @FXML
    private ListView<String> list_view;

    private static int id;

    private static LocalDate start__date;

    private static LocalDate final__date;

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // меню

        pane_menu.setVisible(false);
        button_menu_close.setVisible(false);

        button_menu1.setOnMouseClicked(mouseEvent -> {
            if (pane_flag) {
                pane_menu.setVisible(false);
                pane_flag = false;
                button_menu_close.setVisible(false);
            }
            else {
                pane_menu.setVisible(true);
                pane_flag = true;
                button_menu_close.setVisible(true);

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane_menu);
                translateTransition1.play();
            }
        });

        button_menu2.setOnMouseClicked(mouseEvent -> {
            if (pane_flag) {
                pane_menu.setVisible(false);
                pane_flag = false;
                button_menu_close.setVisible(false);
            }
            else {
                pane_menu.setVisible(true);
                pane_flag = true;
                button_menu_close.setVisible(true);

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane_menu);
                translateTransition1.play();
            }
        });

        button_menu_close.setOnMouseClicked(mouseEvent -> {

            pane_menu.setVisible(false);
            pane_flag = false;
            button_menu_close.setVisible(false);

        });

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
        TreeMap<Integer, String> serial_number = new TreeMap<>();
        TreeMap<Integer, String> detail = new TreeMap<>();
        TreeMap<Integer, String> car_owner = new TreeMap<>();
        TreeMap<Integer, String> license_plate = new TreeMap<>();
        TreeMap<Integer, Integer> id_service = new TreeMap<>();

        query = "SELECT service.id_employee, \n" +
                "       service.start_date, \n" +
                "       service.final_date, \n" +
                "       service.id_service, \n" +
                "       car.model, \n" +
                "       car.id_owner, \n" +
                "       car.license_plate, \n" +
                "       details.serial_number, \n" +
                "       details.category,\n" +
                "       details.price\n" +
                "FROM service\n" +
                "JOIN car ON service.license_plate = car.license_plate \n" +
                "JOIN details ON service.detail_serial_number = details.serial_number;";

        i = 0;
        result = statement.executeQuery(query);
        while (result.next()) {
            login_employee.put(i, result.getString("service.id_employee"));
            start_date.put(i, result.getString("service.start_date"));
            final_date.put(i, result.getString("service.final_date"));
            price.put(i, (result.getInt("details.price")));
            car_model.put(i, result.getString("car.model"));
            serial_number.put(i, result.getString("details.serial_number"));
            car_owner.put(i, result.getString("car.id_owner"));
            detail.put(i, result.getString("details.category"));
            license_plate.put(i, result.getString("car.license_plate"));
            id_service.put(i, result.getInt("service.id_service"));
            i++;
        }

        // просмотр выбора
        list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s1) {
                text_serial_number.setText(serial_number.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_license_plate.setText(license_plate.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_client_login.setText(car_owner.get(services.get(list_view.getSelectionModel().getSelectedItem())));
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

        // переход на окно личного кабинета 1
        personal_acc1.setOnAction(actionEvent -> {

            personal_acc1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_main.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно личного кабинета 2
        personal_acc2.setOnAction(actionEvent -> {

            personal_acc2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_main.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно работников 1
        personal_employees1.setOnAction(actionEvent -> {

            personal_employees1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_employees.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно работников 2
        personal_employees2.setOnAction(actionEvent -> {

            personal_employees2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_employees.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно клиентов 1
        personal_clients1.setOnAction(actionEvent -> {

            personal_clients1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_clients.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно клиентов 2
        personal_clients2.setOnAction(actionEvent -> {

            personal_clients2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_clients.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // редактирование информации об услуге
        button_edit.setOnAction(actionEvent -> {

            AdminServicesEditController.setId(id);
            AdminServicesEditController.setStartDate(start__date);
            AdminServicesEditController.setFinalDate(final__date);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_services_edit.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 529, 267);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.setScene(scene);
            stage.show();

        });

        // добавление новой услуги
        button_add.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_services_add.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 529, 267);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.setScene(scene);
            stage.show();

        });

        // удаление услуги
        button_delete.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            PassController.setId(5);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pass.fxml"));
            Scene scene = null;

            try { scene = new Scene(fxmlLoader.load(), 400, 250); }
            catch (IOException e) { throw new RuntimeException(e); }

            stage.setScene(scene);
            stage.show();

        });

    }

    public static void delete() throws SQLException, ClassNotFoundException {
        Connection connection = databaseHandler.getDbConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + SERVICE_TABLE + " WHERE " + SERVICE_ID + " = '" + id + "'");

    }

}
