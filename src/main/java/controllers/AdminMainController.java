package controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMainController {

    @FXML
    private Button button_menu1;

    @FXML
    private Button button_menu2;

    @FXML
    private Button button_edit;

    @FXML
    private VBox pane_menu;

    @FXML
    private Button personal_clients1;

    @FXML
    private Button personal_clients2;

    @FXML
    private Button personal_employees1;

    @FXML
    private Button personal_employees2;

    @FXML
    private Button personal_service1;

    @FXML
    private Button personal_service2;

    @FXML
    private Button button_menu_close;

    @FXML
    private Text text_address;

    @FXML
    private Text text_login;

    @FXML
    private Text text_name;

    @FXML
    private Text text_pass;

    private static String login;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();


    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        AdminMainController.login = "2";
        PassController.setPassword("2");

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

        // отображение информации о сотруднике

        String query = "SELECT e.last_name,\n" +
                "    e.first_name,\n" +
                "    e.second_name,\n" +
                "    e.address,\n" +
                "    e.login,\n" +
                "    e.password,\n" +
                "    e.permission,\n" +
                "    SUM(s.work_time) AS total_work_time,\n" +
                "    SUM(d.price) * 0.2 AS total_salary\n" +
                "FROM employees e\n" +
                "JOIN services s ON e.login = s.id_employee\n" +
                "JOIN details d ON s.detail_serial_number = d.serial_number\n" +
                "GROUP BY e.login\n" +
                "HAVING e.login = '" + login + "';";

        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery(query);

        if (result.next()) {
            text_name.setText(result.getString("last_name") + " " +
                    result.getString("first_name") + " " +
                    result.getString("second_name"));
            text_address.setText(result.getString("address"));
            text_login.setText(result.getString("login"));
            text_pass.setText("*".repeat(result.getString("password").length()));
        }

        // редактирование
        button_edit.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_edit.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 400, 250);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно работников 1
        personal_employees1.setOnAction(actionEvent -> {

            personal_employees1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_employees.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно работников 2
        personal_employees2.setOnAction(actionEvent -> {

            personal_employees2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_employees.fxml"));
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

        // переход на окно услуг 1
        personal_service1.setOnAction(actionEvent -> {

            personal_service1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_services.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно услуг 2
        personal_service2.setOnAction(actionEvent -> {

            personal_service2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_services.fxml"));
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

    public static String getLogin() {
        return AdminMainController.login;
    }

    public static void setLogin(String login) {
        AdminMainController.login = login;
    }

}
