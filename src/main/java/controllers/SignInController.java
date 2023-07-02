package controllers;

import graphics.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import special.User;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInController extends Constants {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInWindowButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Text text_mistake;

    @FXML
    void initialize() {

        // переход на окно регистрации
        signUpButton.setOnAction(actionEvent -> Main.changeScene("sign_up.fxml"));

        // авторизация
        signInWindowButton.setOnAction(actionEvent -> {

            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();

            if (login.equals("")) {
                System.out.println("Error: login is empty.");
                text_mistake.setText("Вы не ввели логин!");
                Shake loginAnim = new Shake(loginField);
                loginAnim.playAnim();
            } else if (password.equals("")) {
                System.out.println("Error: password is empty.");
                text_mistake.setText("Вы не ввели пароль!");
                Shake passwordAnim = new Shake(passwordField);
                passwordAnim.playAnim();
            } else signInUser(login, password);

            PassController.setPassword(password);
        });

    }

    // процесс авторизации
    private void signInUser(String login, String password) {

        while (true) {

            try {

                String query_client = "SELECT c.*, \n" +
                        "       SUM(CASE WHEN " + CARS_TABLE + "." + CARS_STATUS + " = '1' THEN 1 ELSE 0 END) AS active_cars, \n" +
                        "       SUM(CASE WHEN " + CARS_TABLE + "." + CARS_STATUS + " = '0' THEN 1 ELSE 0 END) AS inactive_cars\n" +
                        "FROM " + CLIENTS_TABLE + " AS c\n" +
                        "LEFT JOIN " + CARS_TABLE + " ON c." + CLIENTS_LOGIN + " = " + CARS_TABLE + "." + CARS_ID_OWNER + "\n" +
                        "WHERE c." + CLIENTS_LOGIN + " = '" + login + "' AND c." + CLIENTS_PASSWORD  + " = '" +
                        password + "'\n" +
                        "GROUP BY c.login;";

                String query_employee = "SELECT e.*,\n" +
                        "    SUM(s." + SERVICES_WORK_TIME + ") AS total_work_time,\n" +
                        "    SUM(d." + DETAILS_PRICE + ") * 0.2 AS total_salary\n" +
                        "FROM " + EMPLOYEES_TABLE + " e\n" +
                        "JOIN " + SERVICES_TABLE + " s ON e." + EMPLOYEES_LOGIN + " = s." +
                        SERVICES_ID_EMPLOYEE + "\n" +
                        "JOIN " + DETAILS_TABLE + " d ON s." + SERVICES_DETAIL_SERIAL_NUMBER +
                        " = d." + DETAILS_SERIAL_NUMBER + "\n" +
                        "GROUP BY e." + EMPLOYEES_LOGIN + "\n" +
                        "HAVING e." + EMPLOYEES_LOGIN + " = '" + login + "' AND e." + EMPLOYEES_PASSWORD + " = '" +
                        password + "';";

                String query_admin = "SELECT e.*,\n" +
                        "    SUM(s." + SERVICES_WORK_TIME + ") AS total_work_time,\n" +
                        "    SUM(d." + DETAILS_PRICE + ") * 0.2 AS total_salary\n" +
                        "FROM " + EMPLOYEES_TABLE + " e\n" +
                        "JOIN " + SERVICES_TABLE + " s ON e." + EMPLOYEES_LOGIN +
                        " = s." + SERVICES_ID_EMPLOYEE + "\n" +
                        "JOIN " + DETAILS_TABLE + " d ON s." + SERVICES_DETAIL_SERIAL_NUMBER +
                        " = d." + DETAILS_SERIAL_NUMBER + "\n" +
                        "GROUP BY e." + EMPLOYEES_LOGIN + "\n" +
                        "HAVING e." + EMPLOYEES_LOGIN + " = '" + login + "' AND e." +
                        EMPLOYEES_PASSWORD + " = '" + password + "';";

                PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query_client);
                ResultSet result = statement.executeQuery(query_client);

                if (result.next()) {

                    if (result.getInt("status") == 0) {
                        System.out.println("Error:blocked");
                        text_mistake.setText("Вы заблокированны.");
                    } else {
                        String last_name = result.getString(CLIENTS_LAST_NAME);
                        String first_name = result.getString(CLIENTS_FIRST_NAME);
                        String second_name = result.getString(CLIENTS_SECOND_NAME);
                        String address = result.getString(CLIENTS_ADDRESS);
                        String phone_number = result.getString(CLIENTS_PHONE_NUMBER);
                        String car_now = result.getString("active_cars");
                        String car_old = result.getString("inactive_cars");

                        String lowerCase;
                        try {
                            lowerCase = last_name.toLowerCase();
                            last_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        try {
                            lowerCase = first_name.toLowerCase();
                            first_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        try {
                            lowerCase = second_name.toLowerCase();
                            second_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        User user = new User(last_name, first_name,
                                second_name, address,
                                phone_number, login,
                                password, "0", car_now, car_old);
                        ClientMainController.setUser(user);

                        signInWindowButton.getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_main.fxml"));
                        Scene scene;
                        try {
                            scene = new Scene(fxmlLoader.load(), 800, 600);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        stage.setScene(scene);
                        Main.setStage(stage);
                        stage.show();
                    }
                    break;

                }

                statement = DatabaseHandler.getInstance().prepareStatement(query_employee);
                result = statement.executeQuery(query_employee);

                if (result.next()) {

                    if (result.getInt(EMPLOYEES_ACCESS_RIGHTS) == 0) {
                        System.out.println("Error:blocked");
                        text_mistake.setText("Вы заблокированны.");
                    }

                    else  if (result.getInt(EMPLOYEES_ACCESS_RIGHTS) == 1) {
                        String last_name = result.getString(EMPLOYEES_LAST_NAME);
                        String first_name = result.getString(EMPLOYEES_FIRST_NAME);
                        String second_name = result.getString(EMPLOYEES_SECOND_NAME);
                        String address = result.getString(EMPLOYEES_ADDRESS);
                        String work_time = result.getString("total_work_time");
                        String salary = result.getString("total_salary");

                        String lowerCase;
                        try {
                            lowerCase = last_name.toLowerCase();
                            last_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        try {
                            lowerCase = first_name.toLowerCase();
                            first_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        try {
                            lowerCase = second_name.toLowerCase();
                            second_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        User user = new User(last_name, first_name,
                                second_name, address, login,
                                password, "1", work_time, salary);
                        EmployeeMainController.setUser(user);

                        signInWindowButton.getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employee_main.fxml"));
                        Scene scene;
                        try {
                            scene = new Scene(fxmlLoader.load(), 800, 600);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        stage.setScene(scene);
                        Main.setStage(stage);
                        stage.show();
                        break;
                    }
                }

                statement = DatabaseHandler.getInstance().prepareStatement(query_admin);
                result = statement.executeQuery(query_admin);

                if (result.next()) {

                    if (result.getInt(EMPLOYEES_ACCESS_RIGHTS) == 0) {
                        System.out.println("Error:blocked");
                        text_mistake.setText("Вы заблокированны.");
                    }

                    else if (result.getInt(EMPLOYEES_ACCESS_RIGHTS) == 2) {
                        String last_name = result.getString(EMPLOYEES_LAST_NAME);
                        String first_name = result.getString(EMPLOYEES_FIRST_NAME);
                        String second_name = result.getString(EMPLOYEES_SECOND_NAME);
                        String address = result.getString(EMPLOYEES_ADDRESS);
                        String work_time = result.getString("total_work_time");
                        String salary = result.getString("total_salary");

                        String lowerCase;
                        try {
                            lowerCase = last_name.toLowerCase();
                            last_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        try {
                            lowerCase = first_name.toLowerCase();
                            first_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        try {
                            lowerCase = second_name.toLowerCase();
                            second_name = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
                        } catch (Exception ignored) {}

                        User user = new User(last_name, first_name,
                                second_name, address, login,
                                password, "2", work_time, salary);
                        AdminMainController.setUser(user);

                        signInWindowButton.getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin_main.fxml"));
                        Scene scene;
                        try {
                            scene = new Scene(fxmlLoader.load(), 800, 600);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        stage.setScene(scene);
                        Main.setStage(stage);
                        stage.show();
                    }

                }

                else {
                    System.out.println("Error: user is not found");
                    text_mistake.setText("Пользователь не найден.");
                    Shake loginAnim = new Shake(loginField);
                    Shake passwordAnim = new Shake(passwordField);
                    loginAnim.playAnim();
                    passwordAnim.playAnim();
                }
                break;
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
