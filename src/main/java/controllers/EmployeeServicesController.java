package controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import special.Services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeServicesController extends Constants {

    @FXML
    private Button button_menu1;

    @FXML
    private Button button_menu2;

    @FXML
    private Button button_menu_close;

    @FXML
    private TableView<special.Services> list_view;

    @FXML
    private TableColumn<special.Services, String> table;

    @FXML
    private VBox pane_menu;

    @FXML
    private Button personal_acc1;

    @FXML
    private Button personal_acc2;

    @FXML
    private Button personal_work_time1;

    @FXML
    private Button personal_work_time2;

    @FXML
    private Text text_detail;

    @FXML
    private Text text_mileage;

    @FXML
    private Text text_model;

    @FXML
    private Text text_price;

    @FXML
    private Text text_time;

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации об услугах

        String query = "SELECT " + CARS_TABLE + ".*, " + DETAILS_TABLE + ".*, " + SERVICES_TABLE + ".*" +
                " FROM " + SERVICES_TABLE +
                " INNER JOIN " + CARS_TABLE +
                " ON " + SERVICES_TABLE + "." + SERVICES_LICENSE_PLATE + " = " + CARS_TABLE + "."
                + CARS_LICENSE_PLATE +
                " INNER JOIN " + DETAILS_TABLE +
                " ON " + SERVICES_TABLE + "." + SERVICES_DETAIL_SERIAL_NUMBER +
                " = " + DETAILS_TABLE + "." + DETAILS_SERIAL_NUMBER +
                " WHERE " + SERVICES_TABLE + "." + SERVICES_ID_EMPLOYEE +
                " = '" + EmployeeMainController.getLogin() + "';";
        PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        table.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<special.Services> s = FXCollections.observableArrayList();
        while (result.next()) {
            String s_date = result.getDate("start_date") + " / " + result.getDate("final_date");
            String s_work_time = result.getString(SERVICES_WORK_TIME) + " ч.";
            String s_mileage = result.getString(SERVICES_MILEAGE) + " км.";
            String s_model = result.getString(CARS_MODEL);
            String s_detail = result.getString(DETAILS_CATEGORY);
            int s_price = (result.getInt(DETAILS_PRICE)) * 2 / 10;
            Services services = new Services(s_work_time, s_mileage, s_model, s_date, s_price, s_detail);
            s.add(services);
        }
        list_view.setItems(s);

        TableView.TableViewSelectionModel<special.Services> selectionModel = list_view.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, services, t1) -> {
            text_detail.setText(list_view.getSelectionModel().getSelectedItem().getDetail());
            text_mileage.setText(list_view.getSelectionModel().getSelectedItem().getMileage());
            text_model.setText(list_view.getSelectionModel().getSelectedItem().getModel());
            text_price.setText(String.valueOf(list_view.getSelectionModel().getSelectedItem().getPrice()));
            text_time.setText(list_view.getSelectionModel().getSelectedItem().getWork_time());
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

        // переход на окно работы 1
        personal_work_time1.setOnAction(actionEvent -> Main.changeScene("employee_work_time.fxml"));

        // переход на окно работы 2
        personal_work_time2.setOnAction(actionEvent -> Main.changeScene("employee_work_time.fxml"));

        // переход на main 1
        personal_acc1.setOnAction(actionEvent -> Main.changeScene("employee_main.fxml"));

        // переход на main 2
        personal_acc2.setOnAction(actionEvent -> Main.changeScene("employee_main.fxml"));
    }

}
