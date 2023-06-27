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
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class AdminEmployeesController extends Constants {

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
    private ListView<String> list_view;

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
    private Button personal_service1;

    @FXML
    private Button personal_service2;

    @FXML
    private Text text_address;

    @FXML
    private Text text_login;

    @FXML
    private Text text_name;

    @FXML
    private Text text_pass;

    @FXML
    private Text text_post;

    @FXML
    private Text text_salary;

    @FXML
    private Text text_services_count;

    @FXML
    private Text text_work_time;

    private static String text_old_login;

    private static int key;

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    private boolean pane_flag = false;

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
            if (!Objects.equals(result.getString(EMPLOYEE_SECOND_NAME), "")) {
                second_name = " " + result.getString(EMPLOYEE_SECOND_NAME).charAt(0) + ".";
            }
            employees.put(result.getString(EMPLOYEE_LAST_NAME) + first_name + second_name, i);
            i++;
        }

        Set<String> setKeys = employees.keySet();
        for(String k: setKeys){
            list_view.getItems().add(k);
        }

        // добавление в массивы для последующего вывода

        TreeMap<Integer, String> name = new TreeMap<>();
        TreeMap<Integer, String> address = new TreeMap<>();
        TreeMap<Integer, String> login = new TreeMap<>();
        TreeMap<Integer, String> pass = new TreeMap<>();
        TreeMap<Integer, String> services_count = new TreeMap<>();
        TreeMap<Integer, String> work_time = new TreeMap<>();
        TreeMap<Integer, String> salary = new TreeMap<>();
        TreeMap<Integer, String> post = new TreeMap<>();

        query = "SELECT " + EMPLOYEE_TABLE + ".*, COUNT(" + SERVICE_TABLE + "." + SERVICE_ID + ") AS services_count, SUM(DATEDIFF(" +
                SERVICE_TABLE + "." + SERVICE_FINAL_DATE + ", " + SERVICE_TABLE + "." + SERVICE_START_DATE + ")) AS days_worked, SUM(" +
                DETAILS_TABLE + "." + DETAILS_PRICE + " * 0.2) AS total_salary FROM " + EMPLOYEE_TABLE + " LEFT JOIN " + SERVICE_TABLE +
                " ON " + EMPLOYEE_TABLE + "." + EMPLOYEE_LOGIN + " = " + SERVICE_TABLE + "." + SERVICE_ID_EMPLOYEE + " LEFT JOIN " +
                DETAILS_TABLE + " ON " + SERVICE_TABLE + "." + SERVICE_DETAIL_SERIAL_NUMBER + " = " + DETAILS_TABLE + "." +
                DETAILS_SERIAL_NUMBER + " GROUP BY " + EMPLOYEE_TABLE + "." + EMPLOYEE_LOGIN +
                " ORDER BY " + EMPLOYEE_TABLE + "." + EMPLOYEE_LAST_NAME + " ASC, " +
                EMPLOYEE_TABLE + "." + EMPLOYEE_FIRST_NAME + " ASC, " + EMPLOYEE_TABLE + "." + EMPLOYEE_SECOND_NAME + " ASC";

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
                    String pp = null;
                    if (result.getInt(EMPLOYEE_ACCESS_RIGHTS) == 0) {
                        pp = "Уволен";
                    }
                    else if (result.getInt(EMPLOYEE_ACCESS_RIGHTS) == 1) {
                        pp = "Рабочий";
                    }
                    else if (result.getInt(EMPLOYEE_ACCESS_RIGHTS) == 2) {
                        pp = "Администратор";
                    }
                    post.put(i, pp);
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
                    String pp = null;
                    int permissions = result.getInt(EMPLOYEE_ACCESS_RIGHTS);
                    if (permissions == 0) {
                        pp = "Уволен";
                    }
                    else if (permissions == 1) {
                        pp = "Рабочий";
                    }
                    else if (permissions == 2) {
                        pp = "Администратор";
                    }
                    post.put(i, pp);
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
                text_post.setText(post.get(employees.get(list_view.getSelectionModel().getSelectedItem())));
                text_old_login = login.get(employees.get(list_view.getSelectionModel().getSelectedItem()));
                key = employees.get(list_view.getSelectionModel().getSelectedItem());
            }

        });

        // меню

        pane_menu.setVisible(false);

        button_menu1.setOnMouseClicked(mouseEvent -> {
            if (pane_flag) {
                pane_menu.setVisible(false);
                pane_flag = false;
            }
            else {
                pane_menu.setVisible(true);
                pane_flag = true;

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane_menu);
                translateTransition1.play();
            }
        });

        button_menu2.setOnMouseClicked(mouseEvent -> {
            if (pane_flag) {
                pane_menu.setVisible(false);
                pane_flag = false;
            }
            else {
                pane_menu.setVisible(true);
                pane_flag = true;

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane_menu);
                translateTransition1.play();
            }
        });

        // переход на окно личного кабинета
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

        // переход на окно личного кабинета
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

        // редактирование информации о сотруднике
        button_edit.setOnAction(actionEvent -> {

            AdminEmployeesEditController.setOld_login(text_old_login);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_employees_edit.fxml"));
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

            if (Objects.equals(post.get(key), "Уволен")) {

                try {
                    Connection connection = databaseHandler.getDbConnection();
                    Statement statement1 = connection.createStatement();
                    statement1.executeUpdate("UPDATE " + EMPLOYEE_TABLE +
                            " SET " + EMPLOYEE_ACCESS_RIGHTS + " = '1' WHERE " +
                            EMPLOYEE_LOGIN + " = '" + text_old_login + "';");
                    System.out.println(statement1);
                } catch (SQLException | ClassNotFoundException ignored) {}

            }

            else {

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_employees_add.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 529, 267);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();

            }

        });

        // удаление сотрудника
        button_delete.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            PassController.setId(4);
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
        statement.executeUpdate("UPDATE " + EMPLOYEE_TABLE +
                " SET " + EMPLOYEE_ACCESS_RIGHTS + " = '0' WHERE " +
                EMPLOYEE_LOGIN + " = '" + text_old_login + "';");
        System.out.println("Success!");

    }

}
