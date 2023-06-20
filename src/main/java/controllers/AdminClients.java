package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import main.SaveInformationPeople;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

public class AdminClients extends Constants {

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
    private Button button_personal_acc;

    @FXML
    private Button button_personal_service;

    @FXML
    private ListView<String> list_view;

    @FXML
    private Button personal_employees;

    @FXML
    private Text text_address;

    @FXML
    private Text text_cars;

    @FXML
    private Text text_login;

    @FXML
    private Text text_name;

    @FXML
    private Text text_pass;

    @FXML
    private Text text_phone;

    @FXML
    private Text text_post;

    @FXML
    private Text text_services;

    private static String old_login;

    private static String old_post;

    private static int key;


    private TreeMap<Integer, String> name = new TreeMap<>();
    private TreeMap<Integer, String> address = new TreeMap<>();
    private TreeMap<Integer, String> phone_number = new TreeMap<>();
    private TreeMap<Integer, String> login = new TreeMap<>();
    private TreeMap<Integer, String> pass = new TreeMap<>();
    private TreeMap<Integer, String> cars = new TreeMap<>();
    private TreeMap<Integer, String> services = new TreeMap<>();
    private static TreeMap<Integer, String> posts = new TreeMap<>();

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации о клиентах
        TreeMap<String, Integer> clients = new TreeMap<>();
        String query = "SELECT * FROM " + CLIENTS_TABLE + " ORDER BY " + CLIENTS_TABLE + "." + CLIENTS_LAST_NAME + " ASC, " +
                CLIENTS_TABLE + "." + CLIENTS_FIRST_NAME + " ASC, " + CLIENTS_TABLE + "." + CLIENTS_SECOND_NAME + " ASC";
        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery();
        int i = 0;
        while (result.next()) {
            String first_name = " " + result.getString(CLIENTS_FIRST_NAME).charAt(0) + ".";
            String second_name = "";
            second_name = " " + result.getString(CLIENTS_SECOND_NAME).charAt(0) + ".";
            clients.put(result.getString(CLIENTS_LAST_NAME) + first_name + second_name, i);
            i++;
        }
        Set<String> setKeys = clients.keySet();
        for (String k : setKeys) {
            list_view.getItems().add(k);
        }
        System.out.println(clients);

        // добавление в массивы для последующего вывода
        query = "SELECT c." + CLIENTS_LAST_NAME + ", c." + CLIENTS_FIRST_NAME + ", c." + CLIENTS_SECOND_NAME +
                ", c." + CLIENTS_ADDRESS + ", c." + CLIENTS_PHONE_NUMBER + ", c." + CLIENTS_LOGIN + ", c." +
                CLIENTS_PASSWORD +
                ", COUNT(DISTINCT " + CAR_TABLE + "." + CAR_LICENSE_PLATE + ") AS car_count, COUNT(DISTINCT " +
                SERVICE_TABLE + "." + SERVICE_ID + ") as service_count, c.status" +
                " FROM " + CLIENTS_TABLE + " c" +
                " LEFT JOIN " + CAR_TABLE + " ON " + CAR_TABLE + "." + CAR_ID_OWNER + " = c." + CLIENTS_LOGIN +
                " LEFT JOIN " + SERVICE_TABLE + " ON " + SERVICE_TABLE + "." + SERVICE_LICENSE_PLATE + " = " +
                CAR_TABLE + "." + CAR_LICENSE_PLATE +
                " GROUP BY c." + CLIENTS_LAST_NAME + ", c." + CLIENTS_FIRST_NAME + ", c." + CLIENTS_SECOND_NAME +
                ", c." + CLIENTS_ADDRESS + ", c." + CLIENTS_PHONE_NUMBER + ", c." + CLIENTS_LOGIN +
                " ORDER BY c." + CLIENTS_LAST_NAME + " ASC, c." +
                CLIENTS_FIRST_NAME + " ASC, c." + CLIENTS_SECOND_NAME + " ASC";

        System.out.println(query);
        result = statement.executeQuery(query);
        i = 0;
        while (i < clients.size()) {
            if (result.next()) {
                String name_str = result.getString(CLIENTS_LAST_NAME) + " " + result.getString(CLIENTS_FIRST_NAME) +
                        " " + result.getString(CLIENTS_SECOND_NAME);
                if (i == 0) {
                    name.put(i, name_str);
                    address.put(i, result.getString(CLIENTS_ADDRESS));
                    phone_number.put(i, result.getString(CLIENTS_PHONE_NUMBER));
                    login.put(i, result.getString(CLIENTS_LOGIN));
                    pass.put(i, result.getString(CLIENTS_PASSWORD));
                    cars.put(i, result.getString("car_count") + " шт.");
                    services.put(i, result.getString("service_count") + " шт.");
                    String pp = null;
                    if (result.getInt("status") == 0) {
                        pp = "Клиент";
                    }
                    else if (result.getInt("status") == 1) {
                        pp = "Услуги не предоставляются";
                    }
                    posts.put(i, pp);
                    i++;
                } else if (!name_str.equals(name.get(i - 1))) {
                    name.put(i, name_str);
                    address.put(i, result.getString(CLIENTS_ADDRESS));
                    phone_number.put(i, result.getString(CLIENTS_PHONE_NUMBER));
                    login.put(i, result.getString(CLIENTS_LOGIN));
                    pass.put(i, result.getString(CLIENTS_PASSWORD));
                    cars.put(i, result.getString("car_count") + " шт.");
                    services.put(i, result.getString("service_count") + " шт.");
                    String pp = null;
                    if (result.getInt("status") == 0) {
                        pp = "Активен";
                    }
                    else if (result.getInt("status") == 1) {
                        pp = "Заблокирован";
                    }
                    posts.put(i, pp);
                    i++;
                }
            }
        }
        list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                text_name.setText(name.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_address.setText(address.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_phone.setText(phone_number.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_login.setText(login.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_pass.setText(pass.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_cars.setText(cars.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_services.setText(services.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_post.setText(posts.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                old_login = login.get(clients.get(list_view.getSelectionModel().getSelectedItem()));
                old_post = posts.get(clients.get(list_view.getSelectionModel().getSelectedItem()));
                key = clients.get(list_view.getSelectionModel().getSelectedItem());
            }
        });

        // редактирование информации о клиенте
        button_edit.setOnAction(actionEvent -> {

            EditAdminClients.setOld_login(old_login);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit_admin_clients.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 529, 267);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.setScene(scene);
            stage.show();

        });

        // добавление нового пользователя
        button_add.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add_admin_clients.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 529, 267);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.setScene(scene);
            stage.show();

        });

        // удаление клиента
        button_delete.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            MainPass.setId(10);
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

        // переход на окно личного кабинета
        button_personal_acc.setOnAction(actionEvent -> {

            button_personal_acc.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_acc.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 700, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.setScene(scene);
            stage.show();

        });

        // переход на окно просмотра услуг
        button_personal_service.setOnAction(actionEvent -> {

            button_personal_service.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_services.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_employees.fxml"));
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

    public static void delete() throws SQLException, ClassNotFoundException {
        Connection connection = databaseHandler.getDbConnection();
        Statement statement = connection.createStatement();
        if (old_post == "Заблокирован") {
            statement.executeUpdate("UPDATE " + CLIENTS_TABLE +
                    " SET status = '0' WHERE " +
                    CLIENTS_LOGIN + " = '" + old_login + "';");
            posts.put(key, "Активен");
        }

        else {
            statement.executeUpdate("UPDATE " + CLIENTS_TABLE +
                    " SET status = '1' WHERE " +
                    CLIENTS_LOGIN + " = '" + old_login + "';");
            posts.put(key, "Заблокирован");
        }
    }

}