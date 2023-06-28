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
import main.Configs;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class EmployeeServicesController extends Constants {

    @FXML
    private Button button_menu1;

    @FXML
    private Button button_menu2;

    @FXML
    private Button button_menu_close;

    @FXML
    private ListView<String> list_view;

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

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // добавление информации об услугах

        TreeMap<String, Integer> services = new TreeMap<>();

        String query = "SELECT * FROM services WHERE id_employee = '" + EmployeeMainController.getLogin() + "';";
        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        int i = 0;
        while (result.next()) {
            services.put(result.getDate("start_date") + " / " + result.getDate("final_date"), i);
            i++;
        }

        ArrayList<String> all = new ArrayList<>();
        statement = databaseHandler.getDbConnection().prepareStatement(query);
        result = statement.executeQuery();
        while (result.next()) {
            all.add(result.getDate(SERVICE_START_DATE) + " / " + result.getDate(SERVICE_FINAL_DATE));
        }
        list_view.getItems().addAll(all);

        // добавление в массивы для последующего вывода

        TreeMap<Integer, String> work_time = new TreeMap<>();
        TreeMap<Integer, String> mileage = new TreeMap<>();
        TreeMap<Integer, String> model = new TreeMap<>();
        TreeMap<Integer, String> detail = new TreeMap<>();
        TreeMap<Integer, Integer> price = new TreeMap<>();

        query = "SELECT " + CAR_TABLE + "." + CAR_MODEL + ", " + DETAILS_TABLE + "." + DETAILS_CATEGORY + ", " +
                DETAILS_TABLE + "." + DETAILS_PRICE + ", " + SERVICE_TABLE + "." + SERVICE_WORK_TIME + ", " +
                SERVICE_TABLE + "." + SERVICE_MILEAGE + " FROM " + SERVICE_TABLE + " INNER JOIN " + CAR_TABLE +
                " ON " + SERVICE_TABLE + "." + SERVICE_LICENSE_PLATE + " = " + CAR_TABLE + "." + CAR_LICENSE_PLATE +
                " INNER JOIN " + DETAILS_TABLE + " ON " + SERVICE_TABLE + "." + SERVICE_DETAIL_SERIAL_NUMBER +
                " = " + DETAILS_TABLE + "." + DETAILS_SERIAL_NUMBER +  " WHERE " + SERVICE_TABLE + "." +
                SERVICE_ID_EMPLOYEE + " = '" + EmployeeMainController.getLogin() + "';";
        result = statement.executeQuery(query);

        for (i = 0; i < services.size(); i++) {
            if (result.next()) {
                work_time.put(i, result.getString(SERVICE_WORK_TIME) + " ч.");
                mileage.put(i, result.getString(SERVICE_MILEAGE) + " км.");
                model.put(i, result.getString(CAR_MODEL));
                detail.put(i, result.getString(DETAILS_CATEGORY));
                price.put(i, (result.getInt(DETAILS_PRICE)) * 2 / 10);
            }
        }

        // просмотр выбора
        list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s1) {
                text_detail.setText(detail.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_mileage.setText(mileage.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_model.setText(model.get(services.get(list_view.getSelectionModel().getSelectedItem())));
                text_price.setText(String.valueOf(price.get(services.get(list_view.getSelectionModel().getSelectedItem()))));
                text_time.setText(work_time.get(services.get(list_view.getSelectionModel().getSelectedItem())));
            }

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
        personal_work_time1.setOnAction(actionEvent -> {

            personal_work_time1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_work_time.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно работы 2
        personal_work_time2.setOnAction(actionEvent -> {

            personal_work_time2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_work_time.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на main 1
        personal_acc1.setOnAction(actionEvent -> {

            personal_acc1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_main.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на main 2
        personal_acc2.setOnAction(actionEvent -> {

            personal_acc2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_main.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });
    }

}
