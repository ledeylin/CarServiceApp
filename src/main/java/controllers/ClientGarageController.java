package controllers;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import special.Cars;
import special.Services;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Objects;

public class ClientGarageController extends Constants {

    @FXML
    private Button button_add_car;

    @FXML
    private Button button_add_service;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_edit;

    @FXML
    private Button button_menu1;

    @FXML
    private Button button_menu2;

    @FXML
    private Button button_menu_close;

    @FXML
    private Button button_now;

    @FXML
    private Button button_old;

    @FXML
    private TableColumn<special.Cars, String> cars_now;

    @FXML
    private TableColumn<special.Cars, String> cars_old;

    @FXML
    private ChoiceBox<String> choice_box_detail;

    @FXML
    private VBox pane_menu;

    @FXML
    private Button personal_acc1;

    @FXML
    private Button personal_acc2;

    @FXML
    private TableColumn<special.Services, String> services_details;

    @FXML
    private TableColumn<special.Services, Date> services_final_date;

    @FXML
    private TableColumn<special.Services, String> services_mileage;

    @FXML
    private TableColumn<special.Services, Date> services_start_date;

    @FXML
    private TextField text_license_plate;

    @FXML
    private TextField text_make;

    @FXML
    private Text license_plate;

    @FXML
    private Text car_make;

    @FXML
    private Text car_model;

    @FXML
    private TextField text_mileage;

    @FXML
    private Text text_mistake;

    @FXML
    private TextField text_model;

    @FXML
    private TableView<special.Cars> view_table_now;

    @FXML
    private TableView<special.Cars> view_table_old;

    @FXML
    private TableView<special.Services> view_table_services;

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    private boolean pane_flag = false;

    private static String now_license_plate = "";

    private static String licensePlate = "";

    private static String make = "";

    private static String model = "";

    private String now_detail = "";

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        ObservableList<special.Services> s = FXCollections.observableArrayList();
        services_details.setCellValueFactory(new PropertyValueFactory<special.Services, String>("detail"));
        services_mileage.setCellValueFactory(new PropertyValueFactory<special.Services, String>("mileage"));
        services_start_date.setCellValueFactory(new PropertyValueFactory<special.Services, Date>("start_date"));
        services_final_date.setCellValueFactory(new PropertyValueFactory<special.Services, Date>("final_date"));


        // таблица с машинами нынешними

        String query = "SELECT * FROM cars WHERE id_owner = '" + ClientMainController.getLogin() + "' AND status = '1';";
        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        ObservableList<special.Cars> c1 = FXCollections.observableArrayList();
        cars_now.setCellValueFactory(new PropertyValueFactory<special.Cars, String>("license_plate"));

        while(result.next()) {
            String license_plate = result.getString("license_plate");
            String model = result.getString("model");
            String make = result.getString("car_make");
            Cars car = new Cars(license_plate, model, make);
            c1.add(car);
        }
        view_table_now.setItems(c1);

        TableView.TableViewSelectionModel<special.Cars> selectionModel = view_table_now.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Cars>() {
            @Override
            public void changed(ObservableValue<? extends Cars> observableValue, Cars cars, Cars t1) {
                license_plate.setText(t1.getLicense_plate());
                car_make.setText(t1.getMake());
                car_model.setText(t1.getModel());
                now_license_plate = t1.getLicense_plate();

                // таблица с услугами

                try {
                    view_table_services.getItems().clear();

                    String query = "SELECT services.*, details.category" +
                            " FROM services" +
                            " INNER JOIN details ON services.detail_serial_number = details.serial_number" +
                            " WHERE license_plate = '" + now_license_plate + "';";
                    PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet result = statement.executeQuery();

                    s.clear();

                    while (result.next()) {
                        String mileage = result.getString("mileage");
                        String detail = result.getString("category");
                        Date start_date = result.getDate("start_date");
                        Date final_date = result.getDate("final_date");
                        Services service = new Services(mileage, start_date, final_date, detail);
                        s.add(service);
                    }
                    view_table_services.setItems(s);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                // choice box

                choice_box_detail.getItems().clear();
                try {
                    String query = "SELECT d.*" +
                            " FROM details AS d" +
                            " INNER JOIN details_compatibilities AS ds" +
                            " ON ds.detail_serial_number = d.serial_number" +
                            " INNER JOIN cars AS c" +
                            " ON c.model = ds.model" +
                            " WHERE c.license_plate = '" + now_license_plate + "';";
                    PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet result = statement.executeQuery();
                    HashSet<String> hs = new HashSet<>();

                    while (result.next()) {
                        String n = result.getString("category");
                        hs.add(n);
                    }
                    choice_box_detail.getItems().setAll(hs);

                    choice_box_detail.setOnAction(actionEvent -> {
                        now_detail = choice_box_detail.getValue();
                    });
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        // таблица с машинами старыми

        query = "SELECT * FROM cars WHERE id_owner = '" + ClientMainController.getLogin() + "' AND status = '0';";
        statement = databaseHandler.getDbConnection().prepareStatement(query);
        result = statement.executeQuery();

        ObservableList<special.Cars> c2 = FXCollections.observableArrayList();
        cars_old.setCellValueFactory(new PropertyValueFactory<special.Cars, String>("license_plate"));

        while(result.next()) {
            String license_plate = result.getString("license_plate");
            String model = result.getString("model");
            String make = result.getString("car_make");
            Cars car = new Cars(license_plate, model, make);
            c2.add(car);
        }
        view_table_old.setItems(c2);

        TableView.TableViewSelectionModel<special.Cars> selectionModel1 = view_table_old.getSelectionModel();
        selectionModel1.selectedItemProperty().addListener(new ChangeListener<Cars>() {
            @Override
            public void changed(ObservableValue<? extends Cars> observableValue, Cars cars, Cars t1) {
                license_plate.setText(t1.getLicense_plate());
                car_make.setText(t1.getMake());
                car_model.setText(t1.getModel());
                now_license_plate = t1.getLicense_plate();

                // таблица с услугами

                try {
                    view_table_services.getItems().clear();

                    String query = "SELECT services.*, details.category" +
                            " FROM services" +
                            " INNER JOIN details ON services.detail_serial_number = details.serial_number" +
                            " WHERE license_plate = '" + now_license_plate + "';";
                    PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet result = statement.executeQuery();

                    s.clear();

                    while (result.next()) {
                        String mileage = result.getString("mileage");
                        String detail = result.getString("category");
                        Date start_date = result.getDate("start_date");
                        Date final_date = result.getDate("final_date");
                        Services service = new Services(mileage, start_date, final_date, detail);
                        s.add(service);
                    }
                    view_table_services.setItems(s);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // добавление машины
        button_add_car.setOnMouseClicked(mouseEvent -> {
            boolean flag = true;
            licensePlate = text_license_plate.getText();
            make = text_make.getText();
            model = text_model.getText();
            // проверка на пустоту данных
            if (Objects.equals(make, "")) {
                flag = false;
                text_mistake.setText("Вы не выбрали марку машины!");
            }
            if (Objects.equals(model, "")) {
                flag = false;
                text_mistake.setText("Вы не выбрали модель машины!");
            }
            if (Objects.equals(licensePlate, "")) {
                flag = false;
                text_mistake.setText("Вы не ввели гос.номер машины!");
            }
            // проверка пароля пользователя
            if (flag) {
                PassController.setId(13);
                Stage stage = new Stage();
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

        // удаление детали
        button_delete.setOnMouseClicked(mouseEvent -> {
            boolean flag = true;
            // проверка на пустоту данных
            if (Objects.equals(now_license_plate, "")) {
                flag = false;
            }

            // проверка пароля пользователя
            if (flag) {
                PassController.setId(14);
                Stage stage = new Stage();
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

        // редактирование информации о машине
        button_edit.setOnAction(actionEvent -> {

            if (Objects.equals(now_license_plate, "")) {
                text_mistake.setText("Вы не выбрали машину!");
            }
            else {
                ClientGarageEditCarController.setOld_license_plate(now_license_plate);
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_garage_edit_car.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 250, 250);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();
            }

        });

        // новые машины
        button_now.setOnAction(actionEvent -> {

            view_table_old.setVisible(false);
            view_table_now.setVisible(true);
            license_plate.setText("");
            car_make.setText("");
            car_model.setText("");
            view_table_services.getItems().clear();
            choice_box_detail.getItems().clear();

        });

        // старые машины
        button_old.setOnAction(actionEvent -> {

            view_table_old.setVisible(true);
            view_table_now.setVisible(false);
            license_plate.setText("");
            car_make.setText("");
            car_model.setText("");
            view_table_services.getItems().clear();
            choice_box_detail.getItems().clear();

        });

        // меню

        pane_menu.setVisible(false);
        button_menu_close.setVisible(false);

        button_menu1.setOnMouseClicked(mouseEvent -> {
            if (pane_flag) {
                pane_menu.setVisible(false);
                pane_flag = false;
                button_menu_close.setVisible(false);
            } else {
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
            } else {
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

        // переход на main 1
        personal_acc1.setOnAction(actionEvent -> {

            personal_acc1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_main.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на main 2
        personal_acc2.setOnAction(actionEvent -> {

            personal_acc2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_main.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

    }

    public static void add() throws SQLException, ClassNotFoundException {

            String insertNew = "INSERT INTO " + CAR_TABLE + " (" + CAR_ID_OWNER + ", " +
                    CAR_LICENSE_PLATE + ", " + CAR_MODEL + ", " + CAR_MAKE + ", status" +
                    ") VALUES(?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(insertNew);
            preparedStatement.setString(1, ClientMainController.getLogin());
            preparedStatement.setString(2, licensePlate);
            preparedStatement.setString(3, model);
            preparedStatement.setString(4, make);
            preparedStatement.setString(5, "1");
            preparedStatement.executeUpdate();
    }

    public static void delete() throws SQLException, ClassNotFoundException {
        Connection connection = databaseHandler.getDbConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE " + CAR_TABLE +
                " SET status = '0' WHERE " +
                CAR_LICENSE_PLATE + " = '" + now_license_plate + "';");
    }

}