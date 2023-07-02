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

public class AdminClientsController extends Constants {

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
    private TableColumn<special.User, String> table;

    @FXML
    private VBox pane_menu;

    @FXML
    private Button personal_acc1;

    @FXML
    private Button personal_acc2;

    @FXML
    private Button button_menu_close;

    @FXML
    private Button personal_employees1;

    @FXML
    private Button personal_employees2;

    @FXML
    private Button personal_service1;

    @FXML
    private Button personal_service2;

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

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации о клиентах
        String query = "SELECT c.*" +
                ", COUNT(DISTINCT " + CARS_TABLE + "." + CARS_LICENSE_PLATE + ") AS car_count, COUNT(DISTINCT " +
                SERVICES_TABLE + "." + SERVICES_ID + ") as service_count, c.status" +
                " FROM " + CLIENTS_TABLE + " c" +
                " LEFT JOIN " + CARS_TABLE + " ON " + CARS_TABLE + "." + CARS_ID_OWNER + " = c." + CLIENTS_LOGIN +
                " LEFT JOIN " + SERVICES_TABLE + " ON " + SERVICES_TABLE + "." + SERVICES_LICENSE_PLATE + " = " +
                CARS_TABLE + "." + CARS_LICENSE_PLATE +
                " GROUP BY c." + CLIENTS_LAST_NAME + ", c." + CLIENTS_FIRST_NAME + ", c." + CLIENTS_SECOND_NAME +
                ", c." + CLIENTS_ADDRESS + ", c." + CLIENTS_PHONE_NUMBER + ", c." + CLIENTS_LOGIN +
                " ORDER BY c." + CLIENTS_LAST_NAME + " ASC, c." +
                CLIENTS_FIRST_NAME + " ASC, c." + CLIENTS_SECOND_NAME + " ASC";

        PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        table.setCellValueFactory(new PropertyValueFactory<>("name"));
        ObservableList<special.User> u = FXCollections.observableArrayList();
        while (result.next()) {
            String last_name = result.getString(CLIENTS_LAST_NAME);
            String first_name = result.getString(CLIENTS_FIRST_NAME);
            String second_name = result.getString(CLIENTS_SECOND_NAME);
            String address = result.getString(CLIENTS_ADDRESS);
            String phone = result.getString(CLIENTS_PHONE_NUMBER);
            String login = result.getString(CLIENTS_LOGIN);
            String pass = result.getString(CLIENTS_PASSWORD);
            String cars = result.getString("car_count");
            String services = result.getString("service_count");
            String post = result.getString("status");
            String first_name1 = " " + result.getString(EMPLOYEES_FIRST_NAME).charAt(0) + ".";
            String second_name1 = "";
            if (!Objects.equals(result.getString(EMPLOYEES_SECOND_NAME), "")) {
                second_name1 = " " + result.getString(EMPLOYEES_SECOND_NAME).charAt(0) + ".";
            }
            String name = last_name + " " + first_name1 + " " + second_name1;
            User user = new User(last_name, first_name, second_name,
                    address, phone, login, pass, cars, services, post, name);
            u.add(user);
        }
        list_view.setItems(u);

        TableView.TableViewSelectionModel<special.User> selectionModel = list_view.getSelectionModel();
            selectionModel.selectedItemProperty().addListener((observableValue, user, t1) -> {
                text_name.setText(list_view.getSelectionModel().getSelectedItem().getFirst_name() + " " +
                        list_view.getSelectionModel().getSelectedItem().getFirst_name() + " " +
                        list_view.getSelectionModel().getSelectedItem().getSecond_name());
                text_address.setText(list_view.getSelectionModel().getSelectedItem().getAddress());
                text_phone.setText(list_view.getSelectionModel().getSelectedItem().getPhone_number());
                text_login.setText(list_view.getSelectionModel().getSelectedItem().getLogin());
                text_pass.setText(list_view.getSelectionModel().getSelectedItem().getPassword());
                text_cars.setText(list_view.getSelectionModel().getSelectedItem().getCars());
                text_services.setText(String.valueOf(list_view.getSelectionModel().getSelectedItem().getService_count()));
                if (Objects.equals(list_view.getSelectionModel().getSelectedItem().getPost(), "0")) {
                    text_post.setText("Заблокирован");
                    old_post = "Заблокирован";
                } else if (Objects.equals(list_view.getSelectionModel().getSelectedItem().getPost(), "1")) {
                    text_post.setText("Активен");
                    old_post = "Активен";
                }
                old_login = list_view.getSelectionModel().getSelectedItem().getLogin();
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

        // переход на окно личного кабинета
        personal_acc1.setOnAction(actionEvent -> Main.changeScene("admin_main.fxml"));

        // переход на окно личного кабинета
        personal_acc2.setOnAction(actionEvent -> Main.changeScene("admin_main.fxml"));

        // переход на окно работников 1
        personal_employees1.setOnAction(actionEvent -> Main.changeScene("admin_employees.fxml"));

        // переход на окно работников 2
        personal_employees2.setOnAction(actionEvent -> Main.changeScene("admin_employees.fxml"));

        // переход на окно услуг 1
        personal_service1.setOnAction(actionEvent -> Main.changeScene("admin_services.fxml"));

        // переход на окно услуг 2
        personal_service2.setOnAction(actionEvent -> Main.changeScene("admin_services.fxml"));

        // редактирование информации о клиенте
        button_edit.setOnAction(actionEvent -> {
            AdminClientsEditController.setOld_login(old_login);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_clients_edit.fxml"));
            Scene scene;

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

            try {
                Connection connection = DatabaseHandler.getInstance();
                Statement statement1 = connection.createStatement();

                if (Objects.equals(old_post, "Заблокирован")) {
                    statement1.executeUpdate("UPDATE " + CLIENTS_TABLE +
                            " SET status = '1' WHERE " +
                            CLIENTS_LOGIN + " = '" + old_login + "';");
                }

                else {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_clients_add.fxml"));
                    Scene scene;

                    try {
                        scene = new Scene(fxmlLoader.load(), 529, 267);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    stage.setScene(scene);
                    stage.show();
                }

            } catch (ClassNotFoundException | SQLException e) { throw new RuntimeException(e); }

        });

        // удаление клиента
        button_delete.setOnAction(actionEvent -> {
            PassController.setId(10);
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
        statement.executeUpdate("UPDATE " + CLIENTS_TABLE +
                " SET status = '0' WHERE " +
                CLIENTS_LOGIN + " = '" + old_login + "';");

        Main.changeScene("admin_clients.fxml");
    }

}
