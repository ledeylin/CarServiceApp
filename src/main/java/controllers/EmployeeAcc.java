package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeAcc extends Constants {

    @FXML
    private Button personal_edit;

    @FXML
    private Button personal_service;

    @FXML
    private Text text_address;

    @FXML
    private Text text_login;

    @FXML
    private Text text_name;

    @FXML
    private Text text_pass;

    @FXML
    private Text text_permissions;

    @FXML
    private Text text_salary;

    @FXML
    private Text text_work_time;

    private static String login;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

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
                "JOIN service s ON e.login = s.id_employee\n" +
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
            text_permissions.setText("Рабочий");
            text_work_time.setText(result.getString("total_work_time"));
            text_salary.setText(result.getString("total_salary"));
        }

        // окно редактирования информации
        personal_edit.setOnAction(actionEvent -> {

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

        // переход на окно просмотра услуг
        personal_service.setOnAction(actionEvent -> {

            personal_service.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_service.fxml"));
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

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        EmployeeAcc.login = login;
    }
}
