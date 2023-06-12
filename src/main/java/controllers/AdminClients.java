package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import main.SaveInformation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeMap;

public class AdminClients extends Constants {

    @FXML
    private Button button_edit;

    @FXML
    private Button button_personal_acc;

    @FXML
    private Button button_personal_edit;

    @FXML
    private Button button_personal_service;

    @FXML
    private ChoiceBox<?> choice_box;

    @FXML
    private ListView<String> list_view;

    @FXML
    private Button personal_employees;

    @FXML
    private Text text_address;

    @FXML
    private Text text_login;

    @FXML
    private Text text_name;

    @FXML
    private Text text_pass;

    @FXML
    private Text text_phone;

    private String text_old_login;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

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
        for(String k: setKeys){
            list_view.getItems().add(k);
        }
        System.out.println(clients);
        System.out.println(setKeys);

        // добавление в массивы для последующего вывода
        TreeMap<Integer, String> name = new TreeMap<>();
        TreeMap<Integer, String> address = new TreeMap<>();
        TreeMap<Integer, String> phone_number = new TreeMap<>();
        TreeMap<Integer, String> login = new TreeMap<>();
        TreeMap<Integer, String> pass = new TreeMap<>();
        query = "SELECT " + CLIENTS_TABLE + "." + CLIENTS_LAST_NAME + ", " + CLIENTS_TABLE + "." + CLIENTS_FIRST_NAME + ", " +
                CLIENTS_TABLE + "." + CLIENTS_SECOND_NAME + ", " + CLIENTS_TABLE + "." + CLIENTS_ADDRESS + ", " +
                CLIENTS_TABLE + "." + CLIENTS_PHONE_NUMBER + ", " + CLIENTS_TABLE + "." + CLIENTS_LOGIN + ", " +
                CLIENTS_TABLE + "." + CLIENTS_PASSWORD + ", " + CLIENTS_TABLE + "." + CLIENTS_PHONE_NUMBER + ", " +
                CAR_TABLE + "." + CAR_LICENSE_PLATE + ", " + CAR_TABLE + "." + CAR_MODEL+ ", " + CAR_TABLE + "." + CAR_MAKE +
                " FROM " + CLIENTS_TABLE + " LEFT JOIN " + CAR_TABLE + " ON " + CLIENTS_TABLE + "." + CLIENTS_LOGIN + " = " +
                CAR_TABLE + "." + CAR_ID_OWNER + " ORDER BY " + CLIENTS_TABLE + "." + CLIENTS_LAST_NAME + " ASC, " +
                CLIENTS_TABLE + "." + CLIENTS_FIRST_NAME + " ASC, " + CLIENTS_TABLE + "." + CLIENTS_SECOND_NAME + " ASC";
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
                    pass.put(i, (result.getString(CLIENTS_PASSWORD)));
                    i++;
                }
                else if (!name_str.equals(name.get(i - 1))) {
                    name.put(i, name_str);
                    address.put(i, result.getString(CLIENTS_ADDRESS));
                    phone_number.put(i, result.getString(CLIENTS_PHONE_NUMBER));
                    login.put(i, result.getString(CLIENTS_LOGIN));
                    pass.put(i, (result.getString(CLIENTS_PASSWORD)));
                    i++;
                }
            }
            System.out.println(name);
        }
        list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String s1 = list_view.getSelectionModel().getSelectedItem();
                Integer s2 = clients.get(s1);
                String s3 = name.get(s2);
                //name.get(clients.get(list_view.getSelectionModel().getSelectedItem()))
                text_name.setText(s3);
                text_address.setText(address.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_phone.setText(phone_number.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_login.setText(login.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_pass.setText(pass.get(clients.get(list_view.getSelectionModel().getSelectedItem())));
                text_old_login = login.get(clients.get(list_view.getSelectionModel().getSelectedItem()));
            }
        });

        // редактирование информации о клиенте
        button_edit.setOnAction(actionEvent -> {
            SaveInformation.setText_old_login(text_old_login);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editClients.fxml"));
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

        // переход на окно просмотра услуг
        button_personal_service.setOnAction(actionEvent -> {
            button_personal_service.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("services_admin.fxml"));
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
