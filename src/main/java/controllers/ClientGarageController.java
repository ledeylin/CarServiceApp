package controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import other.CarsBase;
import other.ServiceRecordCreator;
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

    private boolean pane_flag = false;

    private static String now_license_plate = "";

    private static String licensePlate = "";

    private static String make = "";

    private static String model = "";

    private String old_detail = "";

    private String old_mileage = "";

    private boolean car_status = true;

    private boolean flag_service = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        ObservableList<special.Services> s = FXCollections.observableArrayList();
        services_details.setCellValueFactory(new PropertyValueFactory<>("detail"));
        services_mileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        services_start_date.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        services_final_date.setCellValueFactory(new PropertyValueFactory<>("final_date"));


        // таблица с машинами нынешними

        String query = "SELECT * FROM cars WHERE id_owner = '" + ClientMainController.getLogin() + "' AND status = '1';";
        PreparedStatement statement = DatabaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        ObservableList<special.Cars> c1 = FXCollections.observableArrayList();
        cars_now.setCellValueFactory(new PropertyValueFactory<>("license_plate"));

        while(result.next()) {
            String license_plate = result.getString("license_plate");
            String model = result.getString("model");
            String make = result.getString("car_make");
            Cars car = new Cars(license_plate, model, make);
            c1.add(car);
        }
        view_table_now.setItems(c1);

        TableView.TableViewSelectionModel<special.Cars> selectionModel = view_table_now.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, cars, t1) -> {
            license_plate.setText(t1.getLicense_plate());
            car_make.setText(t1.getMake());
            car_model.setText(t1.getModel());
            now_license_plate = t1.getLicense_plate();
            text_mistake.setText("");

            // таблица с услугами

            try {
                view_table_services.getItems().clear();

                String query12 = "SELECT " + SERVICES_TABLE + ".*," + DETAILS_TABLE + ".*" +
                        " FROM " + SERVICES_TABLE +
                        " INNER JOIN " + DETAILS_TABLE + " ON " + SERVICES_TABLE + "." + SERVICES_DETAIL_SERIAL_NUMBER +
                        " = " + DETAILS_TABLE + "." + DETAILS_SERIAL_NUMBER +
                        " WHERE " + SERVICES_LICENSE_PLATE + " = '" + now_license_plate + "';";
                PreparedStatement statement12 = DatabaseHandler.getInstance().prepareStatement(query12);
                ResultSet result12 = statement12.executeQuery();

                s.clear();

                while (result12.next()) {
                    String mileage = result12.getString(SERVICES_MILEAGE);
                    String detail = result12.getString(DETAILS_CATEGORY);
                    Date start_date = result12.getDate(SERVICES_START_DATE);
                    Date final_date = result12.getDate(SERVICES_FINAL_DATE);
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
                String query12 = "SELECT d.*" +
                        " FROM " + DETAILS_TABLE + " AS d" +
                        " INNER JOIN " + DETAILS_COMPATIBILITY_TABLE + " AS ds" +
                        " ON ds." + DETAILS_COMPATIBILITY_DETAIL_SERIAL_NUMBER + " = d." + DETAILS_SERIAL_NUMBER +
                        " INNER JOIN " + CARS_TABLE + " AS c" +
                        " ON c." + CARS_MODEL + " = ds." + DETAILS_COMPATIBILITY_MODEL +
                        " WHERE c." + CARS_LICENSE_PLATE + " = '" + now_license_plate + "';";
                PreparedStatement statement12 = DatabaseHandler.getInstance().prepareStatement(query12);
                ResultSet result12 = statement12.executeQuery();
                HashSet<String> hs = new HashSet<>();

                while (result12.next()) {
                    String n = result12.getString("category");
                    hs.add(n);
                }
                choice_box_detail.getItems().setAll(hs);

                choice_box_detail.setOnAction(actionEvent -> {
                });
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        // таблица с машинами старыми

        query = "SELECT * FROM " + CARS_TABLE + " WHERE " + CARS_ID_OWNER +
                " = '" + ClientMainController.getLogin() + "' AND " + CARS_STATUS + " = '0';";
        statement = DatabaseHandler.getInstance().prepareStatement(query);
        result = statement.executeQuery();

        ObservableList<special.Cars> c2 = FXCollections.observableArrayList();
        cars_old.setCellValueFactory(new PropertyValueFactory<>("license_plate"));

        while(result.next()) {
            String license_plate = result.getString(CARS_LICENSE_PLATE);
            String model = result.getString(CARS_MODEL);
            String make = result.getString(CARS_MAKE);
            Cars car = new Cars(license_plate, model, make);
            c2.add(car);
        }
        view_table_old.setItems(c2);

        TableView.TableViewSelectionModel<special.Cars> selectionModel1 = view_table_old.getSelectionModel();
        selectionModel1.selectedItemProperty().addListener((observableValue, cars, t1) -> {
            license_plate.setText(t1.getLicense_plate());
            car_make.setText(t1.getMake());
            car_model.setText(t1.getModel());
            now_license_plate = t1.getLicense_plate();
            text_mistake.setText("");

            // таблица с услугами

            try {
                view_table_services.getItems().clear();

                String query13 = "SELECT " + SERVICES_TABLE + ".*, " + DETAILS_TABLE + "." + DETAILS_CATEGORY +
                        " FROM " + SERVICES_TABLE +
                        " INNER JOIN " + DETAILS_TABLE + " ON " + SERVICES_TABLE + "." +
                        SERVICES_DETAIL_SERIAL_NUMBER + " = " + DETAILS_TABLE + "." + DETAILS_SERIAL_NUMBER +
                        " WHERE " + SERVICES_LICENSE_PLATE + " = '" + now_license_plate + "';";
                PreparedStatement statement13 = DatabaseHandler.getInstance().prepareStatement(query13);
                ResultSet result13 = statement13.executeQuery();

                s.clear();

                while (result13.next()) {
                    String mileage = result13.getString(SERVICES_MILEAGE);
                    String detail = result13.getString(DETAILS_CATEGORY);
                    Date start_date = result13.getDate(SERVICES_START_DATE);
                    Date final_date = result13.getDate(SERVICES_FINAL_DATE);
                    Services service = new Services(mileage, start_date, final_date, detail);
                    s.add(service);
                }
                view_table_services.setItems(s);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        // добавление машины
        button_add_car.setOnMouseClicked(mouseEvent -> {

            if (car_status) {
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
                // проверка существования марки
                boolean exists = CarsBase.validateCarMake(make);
                if (!exists) {
                    flag = false;
                    text_mistake.setText("Мы не обслуживаем введёную марку автомобилей.");
                }
                // проверка существования модели
                exists = CarsBase.validateCarModel(model);
                if (!exists) {
                    flag = false;
                    text_mistake.setText("Мы не обслуживаем введёную модель автомобилей.");
                }
                // проверка гос.номера
                if (Objects.equals(licensePlate, "")) {
                    String regex = "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}\\d{2,3}\\b|\\b[ABEKMHOPCTYX]\\d{3}[ABEKMHOPCTYX]{2}\\d{2,3}";
                    if (!licensePlate.matches(regex)) {
                        flag = false;
                        text_mistake.setText("Гос.номер введён некорректно!");
                    }
                }
                // проверка пароля пользователя
                if (flag) {
                    PassController.setId(13);
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
                    text_mistake.setText("");
                }
            }


            else if (!Objects.equals(now_license_plate, "")) {
                try {
                    Connection connection = DatabaseHandler.getInstance();
                    Statement statement1 = connection.createStatement();
                    statement1.executeUpdate("UPDATE " + CARS_TABLE +
                            " SET status = '1' WHERE " +
                            CARS_LICENSE_PLATE + " = '" + now_license_plate + "';");
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        // удаление машины
        button_delete.setOnMouseClicked(mouseEvent -> {
            boolean flag = !Objects.equals(now_license_plate, "");
            // проверка на пустоту данных

            // проверка пароля пользователя
            if (flag) {
                PassController.setId(14);
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
                text_mistake.setText("");
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
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 250, 250);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();
                text_mistake.setText("");
            }

        });

        // добавление услуги
        button_add_service.setOnMouseClicked(mouseEvent -> {

            boolean flag1 = true;
            String mileage = text_mileage.getText();
            String detail = choice_box_detail.getValue();

            try {
                int i = Integer.parseInt(mileage);
            } catch (NumberFormatException e) {
                flag1 = false;
                text_mistake.setText("Введите корректный пробег!");
            }

            if (mileage.equals("")) {
                flag1 = false;
                text_mistake.setText("Вы не ввели пробег.");
            }

            if (detail.equals("")) {
                flag1 = false;
                text_mistake.setText("Вы не выбрали деталь.");
            }

            if ((flag_service) && (flag1) &&
                    (mileage.equals(old_mileage)) &&
                    (detail.equals(old_detail))) {
                int mileage1 = Integer.parseInt(mileage);
                ServiceRecordCreator.createServiceRecord(mileage1, detail, now_license_plate);
                flag_service = false;
                text_mistake.setText("");
            }

            else if ((!flag_service) && (flag1)) {

                String price;

                try {
                    String query1 = "SELECT d." + DETAILS_PRICE +
                            " FROM " + DETAILS_TABLE + " AS d" +
                            " INNER JOIN " + DETAILS_COMPATIBILITY_TABLE + " AS ds" +
                            " ON ds." + DETAILS_COMPATIBILITY_DETAIL_SERIAL_NUMBER + " = d." + DETAILS_SERIAL_NUMBER +
                            " INNER JOIN " + CARS_TABLE + " AS c" +
                            " ON c." + CARS_MODEL + " = ds." + DETAILS_COMPATIBILITY_MODEL +
                            " WHERE c." + CARS_LICENSE_PLATE + " = '" + now_license_plate + "';";
                    PreparedStatement statement1 = DatabaseHandler.getInstance().prepareStatement(query1);
                    ResultSet result1 = statement1.executeQuery();
                    if (result1.next()) {
                        price = result1.getString(DETAILS_PRICE);
                        text_mistake.setText("Цена выбранной детали - " + price + ". Если вы согласны, нажмите на кнопку повторно.");
                        old_detail = detail;
                        old_mileage = mileage;
                        flag_service = true;
                    } else {
                        text_mistake.setText("К сожалению, детали закончились.");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
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
            now_license_plate = "";
            car_status = true;

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
            now_license_plate = "";
            car_status = false;

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
        personal_acc1.setOnAction(actionEvent -> Main.changeScene("client_main.fxml"));

        // переход на main 2
        personal_acc2.setOnAction(actionEvent -> Main.changeScene("client_main.fxml"));

    }

    public static void add() throws SQLException, ClassNotFoundException {

            String insertNew = "INSERT INTO " + CARS_TABLE + " (" + CARS_ID_OWNER + ", " +
                    CARS_LICENSE_PLATE + ", " + CARS_MODEL + ", " + CARS_MAKE + ", status" +
                    ") VALUES(?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = DatabaseHandler.getInstance().prepareStatement(insertNew);
            preparedStatement.setString(1, ClientMainController.getLogin());
            preparedStatement.setString(2, licensePlate);
            preparedStatement.setString(3, model);
            preparedStatement.setString(4, make);
            preparedStatement.setString(5, "1");
            preparedStatement.executeUpdate();

            Main.changeScene("client_garage.fxml");
    }

    public static void delete() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getInstance();
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE " + CARS_TABLE +
                " SET status = '0' WHERE " +
                CARS_LICENSE_PLATE + " = '" + now_license_plate + "';");

        Main.changeScene("client_garage.fxml");

    }

}
