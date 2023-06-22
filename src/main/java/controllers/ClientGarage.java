package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

public class ClientGarage extends Constants {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button_add;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_edit;

    @FXML
    private Button button_now;

    @FXML
    private Button button_old;

    @FXML
    private Button button_personal_acc;

    @FXML
    private Button button_personal_service;

    @FXML
    private Button personal_clients;

    @FXML
    private Button personal_employees;

    @FXML
    private Text text_detail;

    @FXML
    private Text text_employee;

    @FXML
    private Text text_final_date;

    @FXML
    private Text text_mileage;

    @FXML
    private Text text_make;

    @FXML
    private Text text_model;

    @FXML
    private Text text_plate;

    @FXML
    private Text text_services;

    @FXML
    private Text text_start_date;

    @FXML
    private ListView<String> view_now;

    @FXML
    private ListView<String> view_old;

    @FXML
    private ChoiceBox<String> choice_box;

    private static String now_license_plate;

    private boolean flag = true;


    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации о машинах

        TreeMap<String, Integer> old = new TreeMap<>();
        TreeMap<String, Integer> now = new TreeMap<>();
        TreeMap<Integer, ArrayList<String>> services_choice_for_box = new TreeMap<>();
        TreeMap<String, Integer> services_choice_with_id = new TreeMap<>();
        TreeMap<String, Integer> services_choice_with_id_car = new TreeMap<>();
        ArrayList<String> all = new ArrayList<>();
        ArrayList<String> services_choice_list = new ArrayList<>();

        TreeMap<Integer, String> license_plate = new TreeMap<>();
        TreeMap<Integer, String> model = new TreeMap<>();
        TreeMap<Integer, String> make = new TreeMap<>();
        TreeMap<Integer, String> services = new TreeMap<>();
        TreeMap<Integer, String> mileage = new TreeMap<>();
        TreeMap<Integer, String> start_date = new TreeMap<>();
        TreeMap<Integer, String> final_date = new TreeMap<>();
        TreeMap<Integer, String> employee = new TreeMap<>();
        TreeMap<Integer, String> detail = new TreeMap<>();

        String query = "SELECT c.*, COUNT(s.id_service) AS total_services\n" +
                "FROM car c\n" +
                "LEFT JOIN service s ON c.license_plate = s.license_plate\n" +
                "WHERE c.status = '1' AND c.id_owner = '" + ClientAcc.getLogin() + "'\n" +
                "GROUP BY c.license_plate\n";
        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery();
        int i = 0;
        int y = 0;

        while (result.next()) {
            now.put(result.getString(CAR_LICENSE_PLATE), i);
            all.add(result.getString(CAR_LICENSE_PLATE));

            license_plate.put(i, result.getString(CAR_LICENSE_PLATE));
            model.put(i, result.getString(CAR_MODEL));
            make.put(i, result.getString(CAR_MAKE));
            services.put(i, result.getString("total_services"));

            query = "SELECT service.*, details.category \n" +
                    "FROM service \n" +
                    "LEFT JOIN details ON service.detail_serial_number = details.serial_number \n" +
                    "WHERE service.license_plate = '" + result.getString(CAR_LICENSE_PLATE) + "'";
            statement = databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet result1 = statement.executeQuery();

            services_choice_list = new ArrayList<>();
            while (true) {
                if (result1.next()) {
                    services_choice_list.add(result1.getDate(SERVICE_START_DATE).toString());
                    services_choice_with_id.put(result1.getDate(SERVICE_START_DATE).toString(), y);

                    mileage.put(y, result1.getString(SERVICE_MILEAGE));
                    start_date.put(y, result1.getDate(SERVICE_START_DATE).toString());
                    final_date.put(y, result1.getDate(SERVICE_FINAL_DATE).toString());
                    employee.put(y, result1.getString(SERVICE_ID_EMPLOYEE));
                    detail.put(y, result1.getString(DETAILS_CATEGORY));
                    y++;
                }
                else break;
            }

            services_choice_for_box.put(i, services_choice_list);
            services_choice_with_id_car.put(result.getString(CAR_LICENSE_PLATE), i);

            i++;
        }
        view_now.getItems().addAll(all);

        query = "SELECT c.*, COUNT(s.id_service) AS total_services\n" +
                "FROM car c\n" +
                "LEFT JOIN service s ON c.license_plate = s.license_plate\n" +
                "WHERE c.status = '0'\n" +
                "GROUP BY c.license_plate\n";
        statement = databaseHandler.getDbConnection().prepareStatement(query);
        result = statement.executeQuery();
        System.out.println(query);
        all = new ArrayList<>();

        while (result.next()) {
            old.put(result.getString(CAR_LICENSE_PLATE), i);
            all.add(result.getString(CAR_LICENSE_PLATE));

            license_plate.put(i, result.getString(CAR_LICENSE_PLATE));
            model.put(i, result.getString(CAR_MODEL));
            make.put(i, result.getString(CAR_MAKE));
            services.put(i, result.getString("total_services"));

            query = "SELECT service.*, details.category \n" +
                    "FROM service \n" +
                    "LEFT JOIN details ON service.detail_serial_number = details.serial_number \n" +
                    "WHERE service.license_plate = '" + result.getString(CAR_LICENSE_PLATE) + "'";
            statement = databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet result1 = statement.executeQuery();

            services_choice_list = new ArrayList<>();
            while (true) {
                y++;
                if (result1.next()) {
                    services_choice_list.add(result1.getDate(SERVICE_START_DATE).toString());
                    services_choice_with_id.put(result1.getDate(SERVICE_START_DATE).toString(), y);

                    mileage.put(y, result1.getString(SERVICE_MILEAGE));
                    start_date.put(y, result1.getDate(SERVICE_START_DATE).toString());
                    final_date.put(y, result1.getDate(SERVICE_FINAL_DATE).toString());
                    employee.put(y, result1.getString(SERVICE_ID_EMPLOYEE));
                    detail.put(y, result1.getString(DETAILS_CATEGORY));
                }
                else break;
            }

            services_choice_for_box.put(i, services_choice_list);
            services_choice_with_id_car.put(result.getString(CAR_LICENSE_PLATE), i);

            i++;
        }
        view_old.getItems().addAll(all);

        // новые машины
        button_now.setOnAction(actionEvent -> {

            view_old.setVisible(false);
            view_now.setVisible(true);

        });

        // старые машины
        button_old.setOnAction(actionEvent -> {

            view_old.setVisible(true);
            view_now.setVisible(false);

        });

        System.out.println(services_choice_for_box);
        System.out.println(services_choice_list);
        System.out.println(services_choice_with_id_car);
        System.out.println(services_choice_with_id);

        // просмотр выбора
        view_now.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.println(view_now.getSelectionModel().getSelectedItem());
                text_services.setText(services.get(now.get(view_now.getSelectionModel().getSelectedItem())));
                text_plate.setText(license_plate.get(now.get(view_now.getSelectionModel().getSelectedItem())));
                text_model.setText(model.get(now.get(view_now.getSelectionModel().getSelectedItem())));
                text_make.setText(make.get(now.get(view_now.getSelectionModel().getSelectedItem())));
                choice_box.getItems().clear();
                choice_box.getItems().addAll(services_choice_for_box.get(services_choice_with_id_car.get(view_now.getSelectionModel().getSelectedItem())));
                text_start_date.setText("-");
                text_final_date.setText("-");
                text_detail.setText("-");
                text_employee.setText("-");
                text_mileage.setText("-");
                now_license_plate = text_plate.getText();
                flag = true;
            }
        });

        view_old.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.println(view_old.getSelectionModel().getSelectedItem());
                text_mileage.setText(mileage.get(old.get(view_old.getSelectionModel().getSelectedItem())));
                text_plate.setText(license_plate.get(old.get(view_old.getSelectionModel().getSelectedItem())));
                text_model.setText(model.get(old.get(view_old.getSelectionModel().getSelectedItem())));
                text_services.setText(services.get(old.get(view_old.getSelectionModel().getSelectedItem())));
                choice_box.getItems().clear();
                choice_box.getItems().addAll(services_choice_for_box.get(services_choice_with_id_car.get(view_now.getSelectionModel().getSelectedItem())));
                text_start_date.setText("-");
                text_final_date.setText("-");
                text_detail.setText("-");
                text_employee.setText("-");
                text_mileage.setText("-");
                now_license_plate = text_plate.getText();
                flag = false;
            }
        });

        choice_box.setOnAction(actionEvent -> {
            text_start_date.setText(start_date.get(services_choice_with_id.get(choice_box.getValue())));
            text_final_date.setText(final_date.get(services_choice_with_id.get(choice_box.getValue())));
            text_detail.setText(detail.get(services_choice_with_id.get(choice_box.getValue())));
            text_employee.setText(employee.get(services_choice_with_id.get(choice_box.getValue())));
            text_mileage.setText(mileage.get(services_choice_with_id.get(choice_box.getValue())));
        });

        // редактирование информации о машине
        button_edit.setOnAction(actionEvent -> {

//            EditAdminEmployees.setOld_login(text_old_login);
//            Stage stage = new Stage();
//            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit_admin_employees.fxml"));
//            Scene scene = null;
//            try {
//                scene = new Scene(fxmlLoader.load(), 529, 267);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            stage.setScene(scene);
//            stage.show();

        });

        // добавление новой машины
        button_add.setOnAction(actionEvent -> {

            if (flag) {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add_client_car.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 529, 267);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();
                }

            else {
                Connection connection = null;
                try {
                    connection = databaseHandler.getDbConnection();
                    Statement statement1 = connection.createStatement();
                    statement1.executeUpdate("UPDATE " + CAR_TABLE +
                            " SET status = '1' WHERE " +
                            CAR_LICENSE_PLATE + " = '" + now_license_plate + "';");

                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        // удаление машины
        button_delete.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            MainPass.setId(14);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main_pass.fxml"));
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
        statement.executeUpdate("UPDATE " + CAR_TABLE +
                " SET status = '0' WHERE " +
                CAR_LICENSE_PLATE + " = '" + now_license_plate + "';");
    }

}
