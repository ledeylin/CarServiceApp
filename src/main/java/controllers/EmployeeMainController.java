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
import main.Main;
import special.User;

import java.io.IOException;
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

    private boolean pane_flag = false;

    private static User user;

    public static void setUser(User user) {
        EmployeeMainController.user = user;
    }

    public static User getUser() {
        return user;
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        EmployeeMainController.login = user.getLogin();
        PassController.setPassword(user.getPassword());

        text_name.setText(user.getLast_name() + " " +
                user.getFirst_name() + " " +
                user.getSecond_name());
        text_address.setText(user.getAddress());
        text_login.setText(user.getLogin());
        text_pass.setText("*".repeat(user.getPassword().length()));
        text_work_time.setText(user.getWork_time());
        text_salary.setText(user.getSalary());

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
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load(), 400, 250);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно просмотра услуг 1
        personal_service1.setOnAction(actionEvent -> Main.changeScene("employee_services.fxml"));

        // переход на окно просмотра услуг 2
        personal_service2.setOnAction(actionEvent -> Main.changeScene("employee_services.fxml"));

        // переход на окно работы 1
        personal_work_time1.setOnAction(actionEvent -> Main.changeScene("employee_work_time.fxml"));

        // переход на окно работы 2
        personal_work_time2.setOnAction(actionEvent -> Main.changeScene("employee_work_time.fxml"));
    }

    public static String getLogin() {
        return EmployeeMainController.login;
    }

    public static void setLogin(String login) {
        EmployeeMainController.login = login;
    }

}
