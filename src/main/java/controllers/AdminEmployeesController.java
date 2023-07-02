package controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import special.User;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

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
    private TableView<special.User> list_view;

    @FXML
    private TableColumn<User, String> table;

    @FXML
    private VBox pane_menu;

    @FXML
    private Button personal_acc1;

    @FXML
    private Button personal_acc2;

    @FXML
    private Button button_menu_close;

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

    private static String post;

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации о клиентах
        String query = "SELECT " + EMPLOYEES_TABLE + ".*, COUNT(" +
                SERVICES_TABLE + "." + SERVICES_ID + ") AS services_count, SUM(DATEDIFF(" +
                SERVICES_TABLE + "." + SERVICES_FINAL_DATE + ", " + SERVICES_TABLE + "." +
                SERVICES_START_DATE + ")) AS days_worked, SUM(" +
                DETAILS_TABLE + "." + DETAILS_PRICE + " * 0.2) AS total_salary FROM " +
                EMPLOYEES_TABLE + " LEFT JOIN " + SERVICES_TABLE +
                " ON " + EMPLOYEES_TABLE + "." + EMPLOYEES_LOGIN + " = " + SERVICES_TABLE +
                "." + SERVICES_ID_EMPLOYEE + " LEFT JOIN " +
                DETAILS_TABLE + " ON " + SERVICES_TABLE + "." + SERVICES_DETAIL_SERIAL_NUMBER +
                " = " + DETAILS_TABLE + "." +
                DETAILS_SERIAL_NUMBER + " GROUP BY " + EMPLOYEES_TABLE + "." + EMPLOYEES_LOGIN +
                " ORDER BY " + EMPLOYEES_TABLE + "." + EMPLOYEES_LAST_NAME + " ASC, " +
                EMPLOYEES_TABLE + "." + EMPLOYEES_FIRST_NAME + " ASC, " + EMPLOYEES_TABLE + "." +
                EMPLOYEES_SECOND_NAME + " ASC";

        PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        table.setCellValueFactory(new PropertyValueFactory<>("name"));
        ObservableList<User> u = FXCollections.observableArrayList();
        while (result.next()) {
            String last_name = result.getString(CLIENTS_LAST_NAME);
            String first_name = result.getString(CLIENTS_FIRST_NAME);
            String second_name = result.getString(CLIENTS_SECOND_NAME);
            String address = result.getString(CLIENTS_ADDRESS);
            String login = result.getString(CLIENTS_LOGIN);
            String pass = result.getString(CLIENTS_PASSWORD);
            String work_time = result.getString("days_worked");
            String salary = result.getString("total_salary");
            String services = result.getString("services_count");
            String post = result.getString(EMPLOYEES_ACCESS_RIGHTS);
            String first_name1 = " " + result.getString(EMPLOYEES_FIRST_NAME).charAt(0) + ".";
            String second_name1 = "";
            if (!Objects.equals(result.getString(EMPLOYEES_SECOND_NAME), "")) {
                second_name1 = " " + result.getString(EMPLOYEES_SECOND_NAME).charAt(0) + ".";
            }
            String name = last_name + " " + first_name1 + " " + second_name1;
            User user = new User(last_name, first_name,
                    second_name, address,
                    login, pass,
                    post, work_time,
                    salary, services, name, 1);
            u.add(user);
        }
        list_view.setItems(u);

        TableView.TableViewSelectionModel<special.User> selectionModel = list_view.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, user, t1) -> {

            text_name.setText(list_view.getSelectionModel().getSelectedItem().getFirst_name() + " " +
                    list_view.getSelectionModel().getSelectedItem().getFirst_name() + " " +
                    list_view.getSelectionModel().getSelectedItem().getSecond_name());
            text_address.setText(list_view.getSelectionModel().getSelectedItem().getAddress());
            text_login.setText(list_view.getSelectionModel().getSelectedItem().getLogin());
            text_pass.setText(list_view.getSelectionModel().getSelectedItem().getPassword());
            if (Objects.equals(list_view.getSelectionModel().getSelectedItem().getPost(), "0")) {
                text_post.setText("Уволен");
                post = "Уволен";
            }
            else if (Objects.equals(list_view.getSelectionModel().getSelectedItem().getPost(), "1")){
                text_post.setText("Работник");
                post = "Работник";
            }
            else if (Objects.equals(list_view.getSelectionModel().getSelectedItem().getPost(), "2")){
                text_post.setText("Администратор");
                post = "Администратор";
            }
            text_work_time.setText(list_view.getSelectionModel().getSelectedItem().getWork_time());
            text_salary.setText(list_view.getSelectionModel().getSelectedItem().getSalary());
            text_services_count.setText(list_view.getSelectionModel().getSelectedItem().getService_count());
            text_old_login = list_view.getSelectionModel().getSelectedItem().getLogin();

        });

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

        // переход на окно личного кабинета 1
        personal_acc1.setOnAction(actionEvent -> Main.changeScene("admin_main.fxml"));

        // переход на окно личного кабинета 2
        personal_acc2.setOnAction(actionEvent -> Main.changeScene("admin_main.fxml"));

        // переход на окно клиентов 1
        personal_clients1.setOnAction(actionEvent -> Main.changeScene("admin_clients.fxml"));

        // переход на окно клиентов 2
        personal_clients2.setOnAction(actionEvent -> Main.changeScene("admin_clients.fxml"));

        // переход на окно услуг 1
        personal_service1.setOnAction(actionEvent -> Main.changeScene("admin_services.fxml"));

        // переход на окно услуг 2
        personal_service2.setOnAction(actionEvent -> Main.changeScene("admin_services.fxml"));

        // редактирование информации о сотруднике
        button_edit.setOnAction(actionEvent -> {

            AdminEmployeesEditController.setOld_login(text_old_login);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_employees_edit.fxml"));
            Scene scene;
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

            if (Objects.equals(post, "Уволен")) {

                try {
                    Connection connection = DatabaseHandler.getInstance();
                    Statement statement1 = connection.createStatement();
                    statement1.executeUpdate("UPDATE " + EMPLOYEES_TABLE +
                            " SET " + EMPLOYEES_ACCESS_RIGHTS + " = '1' WHERE " +
                            EMPLOYEES_LOGIN + " = '" + text_old_login + "';");
                    System.out.println(statement1);
                } catch (SQLException | ClassNotFoundException ignored) {}

            }

            else Main.changeScene("admin_employees_add.fxml");

        });

        // удаление сотрудника
        button_delete.setOnAction(actionEvent -> {

            PassController.setId(4);
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

        });


    }

    public static void delete() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getInstance();
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE " + EMPLOYEES_TABLE +
                " SET " + EMPLOYEES_ACCESS_RIGHTS + " = '0' WHERE " +
                EMPLOYEES_LOGIN + " = '" + text_old_login + "';");
        System.out.println("Success!");

        Main.changeScene("admin_employees.fxml");

    }

}
