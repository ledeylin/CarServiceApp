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
import java.sql.*;
import java.util.Set;
import java.util.TreeMap;

public class AdminEmployees extends Constants {

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
    private Button personal_clients;

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
    private Text text_salary;

    @FXML
    private Text text_services_count;

    @FXML
    private Text text_work_time;

    private static String text_old_login;

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации о сотрудниках

        TreeMap<String, Integer> employees = new TreeMap<>();
        String query = "SELECT * FROM " + EMPLOYEE_TABLE + " ORDER BY " + EMPLOYEE_TABLE + "." + EMPLOYEE_LAST_NAME + " ASC, " +
                EMPLOYEE_TABLE + "." + EMPLOYEE_FIRST_NAME + " ASC, " + EMPLOYEE_TABLE + "." + EMPLOYEE_SECOND_NAME + " ASC";
        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        int i = 0;
        while (result.next()) {
            String first_name = " " + result.getString(EMPLOYEE_FIRST_NAME).charAt(0) + ".";
            String second_name = "";
            second_name = " " + result.getString(EMPLOYEE_SECOND_NAME).charAt(0) + ".";
            employees.put(result.getString(EMPLOYEE_LAST_NAME) + first_name + second_name, i);
            i++;
        }

        Set<String> setKeys = employees.keySet();
        for(String k: setKeys){
            list_view.getItems().add(k);
        }
        System.out.println(employees);
        System.out.println(setKeys);

        // добавление в массивы для последующего вывода

        TreeMap<Integer, String> name = new TreeMap<>();
        TreeMap<Integer, String> address = new TreeMap<>();
        TreeMap<Integer, String> login = new TreeMap<>();
        TreeMap<Integer, String> pass = new TreeMap<>();
        TreeMap<Integer, String> services_count = new TreeMap<>();
        TreeMap<Integer, String> work_time = new TreeMap<>();
        TreeMap<Integer, String> salary = new TreeMap<>();

        query = "SELECT " + EMPLOYEE_TABLE + ".*, COUNT(" + SERVICE_TABLE + "." + SERVICE_ID + ") AS services_count, SUM(DATEDIFF(" +
                SERVICE_TABLE + "." + SERVICE_FINAL_DATE + ", " + SERVICE_TABLE + "." + SERVICE_START_DATE + ")) AS days_worked, SUM(" +
                DETAILS_TABLE + "." + DETAILS_PRICE + " * 0.2) AS total_salary FROM " + EMPLOYEE_TABLE + " LEFT JOIN " + SERVICE_TABLE +
                " ON " + EMPLOYEE_TABLE + "." + EMPLOYEE_LOGIN + " = " + SERVICE_TABLE + "." + SERVICE_ID_EMPLOYEE + " LEFT JOIN " +
                DETAILS_TABLE + " ON " + SERVICE_TABLE + "." + SERVICE_DETAIL_SERIAL_NUMBER + " = " + DETAILS_TABLE + "." +
                DETAILS_SERIAL_NUMBER + " GROUP BY " + EMPLOYEE_TABLE + "." + EMPLOYEE_LOGIN +
                " ORDER BY " + EMPLOYEE_TABLE + "." + EMPLOYEE_LAST_NAME + " ASC, " +
                EMPLOYEE_TABLE + "." + EMPLOYEE_FIRST_NAME + " ASC, " + EMPLOYEE_TABLE + "." + EMPLOYEE_SECOND_NAME + " ASC";

        System.out.println(query);
        result = statement.executeQuery(query);
        i = 0;

        while (i < employees.size()) {

            if (result.next()) {

                String second_name = result.getString(EMPLOYEE_SECOND_NAME);
                if (second_name == null) second_name = "";
                String name_str = result.getString(EMPLOYEE_LAST_NAME) + " " + result.getString(EMPLOYEE_FIRST_NAME) +
                        " " + second_name;

                if (i == 0) {
                    name.put(i, name_str);
                    address.put(i, result.getString(EMPLOYEE_ADDRESS));
                    login.put(i, result.getString(EMPLOYEE_LOGIN));
                    pass.put(i, result.getString(EMPLOYEE_PASSWORD));
                    services_count.put(i, result.getString("services_count") + " шт.");
                    work_time.put(i, result.getString("days_worked") + " дней");
                    salary.put(i, result.getString("total_salary") + " р.");
                    i++;
                }

                else if (!name_str.equals(name.get(i - 1))) {
                    name.put(i, name_str);
                    address.put(i, result.getString(EMPLOYEE_ADDRESS));
                    login.put(i, result.getString(EMPLOYEE_LOGIN));
                    pass.put(i, result.getString(EMPLOYEE_PASSWORD));
                    services_count.put(i, result.getString("services_count") + " шт.");
                    work_time.put(i, result.getString("days_worked") + " дней");
                    salary.put(i, result.getString("total_salary") + " р.");
                    i++;
                }

            }

        }

        // просмотр выбора
        list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                text_name.setText(name.get(employees.get(list_view.getSelectionModel().getSelectedItem())));
                text_address.setText(address.get(employees.get(list_view.getSelectionModel().getSelectedItem())));
                text_login.setText(login.get(employees.get(list_view.getSelectionModel().getSelectedItem())));
                text_pass.setText(pass.get(employees.get(list_view.getSelectionModel().getSelectedItem())));
                text_work_time.setText(work_time.get(employees.get(list_view.getSelectionModel().getSelectedItem())));
                text_salary.setText(salary.get(employees.get(list_view.getSelectionModel().getSelectedItem())));
                text_services_count.setText(services_count.get(employees.get(list_view.getSelectionModel().getSelectedItem())));
                text_old_login = login.get(employees.get(list_view.getSelectionModel().getSelectedItem()));
            }

        });

        // редактирование информации о сотруднике
        button_edit.setOnAction(actionEvent -> {

            EditAdminEmployees.setOld_login(text_old_login);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit_admin_employees.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 529, 267);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // добавление нового сотрудника
        button_add.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add_admin_employees.fxml"));
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
            MainPass.setId(4);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main_pass.fxml"));
            Scene scene = null;

            try { scene = new Scene(fxmlLoader.load(), 400, 250); }
            catch (IOException e) { throw new RuntimeException(e); }

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

        // переход на окно клиентов
        personal_clients.setOnAction(actionEvent -> {

            personal_clients.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_clients.fxml"));
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
        statement.executeUpdate("DELETE FROM " + EMPLOYEE_TABLE + " WHERE " + EMPLOYEE_LOGIN + " = '" + text_old_login + "'");

    }
}
