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

public class ClientMainController extends Constants {

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
    private Button personal_garage1;

    @FXML
    private Button personal_garage2;

    @FXML
    private Text text_address;

    @FXML
    private Text text_car_now;

    @FXML
    private Text text_car_old;

    @FXML
    private Text text_login;

    @FXML
    private Text text_name;

    @FXML
    private Text text_pass;

    @FXML
    private Text text_phone;

    private static String login;

    private boolean pane_flag = false;

    static User user;

    public static void setUser(User user) {
        ClientMainController.user = user;
    }

    public static User getUser() {
        return user;
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        ClientMainController.login = user.getLogin();
        PassController.setPassword(user.getPassword());

        // отображение информации о клиенте

        text_name.setText(user.getLast_name() + " " +
                user.getFirst_name() + " " +
                user.getSecond_name());
        text_address.setText(user.getAddress());
        text_login.setText(user.getLogin());
        text_phone.setText(user.getPhone_number());
        text_car_now.setText(user.getCar_now());
        text_car_old.setText(user.getCar_old());
        text_pass.setText("*".repeat(user.getPassword().length()));

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

        // редактирование
        button_edit.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_edit.fxml"));
            Scene scene;

            try {
                scene = new Scene(fxmlLoader.load(), 400, 250);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно гаража 1
        personal_garage1.setOnAction(actionEvent -> Main.changeScene("client_garage.fxml"));

        // переход на окно гаража 2
        personal_garage2.setOnAction(actionEvent -> Main.changeScene("client_garage.fxml"));

    }

    public static String getLogin() {
        return ClientMainController.login;
    }

}
