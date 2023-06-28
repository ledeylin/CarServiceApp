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
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMainController extends Constants {

    @FXML
    private Button button_edit;

    @FXML
    private Button button_menu1;

    @FXML
    private Button button_menu2;

    @FXML
    private Button button_menu_close;

    @FXML
    private VBox pane_menu;

    @FXML
    private Button personal_service1;

    @FXML
    private Button personal_service2;

    @FXML
    private Button personal_work_time1;

    @FXML
    private Button personal_work_time2;

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
    private Text text_work_time;

    private static String login;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        EmployeeMainController.login = "1";
        PassController.setPassword("1");

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
            text_work_time.setText(result.getString("total_work_time"));
            text_salary.setText(result.getString("total_salary"));
        }

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

        // окно редактирования информации
        button_edit.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_edit.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 400, 250);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно просмотра услуг 1
        personal_service1.setOnAction(actionEvent -> {

            personal_service1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_services.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно просмотра услуг 2
        personal_service2.setOnAction(actionEvent -> {

            personal_service2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_services.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

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
    }

    public static String getLogin() {
        return EmployeeMainController.login;
    }

    public static void setLogin(String login) {
        EmployeeMainController.login = login;
    }

}
