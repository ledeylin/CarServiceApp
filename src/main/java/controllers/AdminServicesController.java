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
import special.Services;

import java.io.IOException;
import java.sql.*;

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
    private TableView<special.Services> list_view;

    @FXML
    private TableColumn<Services, String> table;

    private static int id;

    private static Date start__date;

    private static Date final__date;

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации об услугах

        String query = "SELECT " + SERVICES_TABLE + ".*, \n" +
                "       " + CARS_TABLE + ".*, \n" +
                "       " + DETAILS_TABLE + ".* \n" +
                "FROM " + SERVICES_TABLE + "\n" +
                "JOIN " + CARS_TABLE + " ON " + SERVICES_TABLE + "." + SERVICES_LICENSE_PLATE +
                " = " + CARS_TABLE + "." + CARS_LICENSE_PLATE + " \n" +
                "JOIN " + DETAILS_TABLE + " ON " + SERVICES_TABLE + "." + SERVICES_DETAIL_SERIAL_NUMBER +
                        " = " + DETAILS_TABLE + "." + DETAILS_SERIAL_NUMBER + ";";
        PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        table.setCellValueFactory(new PropertyValueFactory<>("date"));
        ObservableList<Services> s = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt(SERVICES_ID);
            String model = result.getString(CARS_MODEL);
            int price = (result.getInt(DETAILS_PRICE));
            Date start_date = result.getDate(SERVICES_START_DATE);
            Date final_date = result.getDate(SERVICES_START_DATE);
            String license_plate = result.getString(CARS_LICENSE_PLATE);
            String detail_serial_number = result.getString(SERVICES_DETAIL_SERIAL_NUMBER);
            String detail = result.getString(DETAILS_CATEGORY);
            String id_employee = result.getString(SERVICES_ID_EMPLOYEE);
            String id_client = result.getString(CARS_ID_OWNER);
            String date = result.getDate(SERVICES_START_DATE) + " / " + result.getDate(SERVICES_FINAL_DATE);
            Services services = new Services(id, model, price,
                    start_date, final_date, id_employee, id_client, license_plate,
                    detail_serial_number, detail, date);
            s.add(services);
        }
        list_view.setItems(s);

        TableView.TableViewSelectionModel<special.Services> selectionModel = list_view.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, services, t1) -> {
            text_serial_number.setText(list_view.getSelectionModel().getSelectedItem().getDetail_serial_number());
            text_license_plate.setText(list_view.getSelectionModel().getSelectedItem().getLicense_plate());
            text_client_login.setText(list_view.getSelectionModel().getSelectedItem().getId_client());
            text_car_model.setText(list_view.getSelectionModel().getSelectedItem().getModel());
            text_price.setText(String.valueOf(list_view.getSelectionModel().getSelectedItem().getPrice()));
            text_login_employee.setText(list_view.getSelectionModel().getSelectedItem().getId_employee());
            text_start_date.setText(list_view.getSelectionModel().getSelectedItem().getStart_date().toString());
            text_final_date.setText(list_view.getSelectionModel().getSelectedItem().getFinal_date().toString());
            text_detail.setText(list_view.getSelectionModel().getSelectedItem().getDetail());
            id = list_view.getSelectionModel().getSelectedItem().getId();
            start__date = list_view.getSelectionModel().getSelectedItem().getStart_date();
            final__date = list_view.getSelectionModel().getSelectedItem().getFinal_date();
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

        // переход на окно работников 1
        personal_employees1.setOnAction(actionEvent -> Main.changeScene("admin_employees.fxml"));

        // переход на окно работников 2
        personal_employees2.setOnAction(actionEvent -> Main.changeScene("admin_employees.fxml"));

        // переход на окно клиентов 1
        personal_clients1.setOnAction(actionEvent -> Main.changeScene("admin_clients.fxml"));

        // переход на окно клиентов 2
        personal_clients2.setOnAction(actionEvent -> Main.changeScene("admin_clients.fxml"));

        // редактирование информации об услуге
        button_edit.setOnAction(actionEvent -> {

            AdminServicesEditController.setId(id);
            AdminServicesEditController.setStartDate(start__date.toLocalDate());
            AdminServicesEditController.setFinalDate(final__date.toLocalDate());
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_services_edit.fxml"));
            Scene scene;

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
            Scene scene;

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
            Scene scene;

            try { scene = new Scene(fxmlLoader.load(), 400, 250); }
            catch (IOException e) { throw new RuntimeException(e); }

            stage.setScene(scene);
            stage.show();

        });

    }

    public static void delete() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getInstance();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + SERVICES_TABLE + " WHERE " + SERVICES_ID + " = '" + id + "'");

        Main.changeScene("admin_services.fxml");

    }

}
