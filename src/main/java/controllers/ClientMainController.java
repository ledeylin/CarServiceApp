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

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        ClientMainController.login = "4";
        PassController.setPassword("4");

        // отображение информации о клиенте

        String query = "SELECT c.*, \n" +
                "       SUM(CASE WHEN cars.status = '1' THEN 1 ELSE 0 END) AS active_cars, \n" +
                "       SUM(CASE WHEN cars.status = '0' THEN 1 ELSE 0 END) AS inactive_cars\n" +
                "FROM clients AS c\n" +
                "LEFT JOIN cars ON c.login = cars.id_owner\n" +
                "WHERE c.login = '" + login + "'\n" +
                "GROUP BY c.login\n" +
                "\n";

        PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
        ResultSet result = statement.executeQuery(query);

        if (result.next()) {
            text_name.setText(result.getString(CLIENTS_LAST_NAME) + " " +
                    result.getString(CLIENTS_FIRST_NAME) + " " +
                    result.getString(CLIENTS_SECOND_NAME));
            text_address.setText(result.getString(CLIENTS_ADDRESS));
            text_login.setText(result.getString(CLIENTS_LOGIN));
            text_phone.setText(result.getString(CLIENTS_PHONE_NUMBER));
            text_car_now.setText(result.getString("active_cars"));
            text_car_old.setText(result.getString("inactive_cars"));
            text_pass.setText("*".repeat(result.getString(CLIENTS_PASSWORD).length()));
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

        // редактирование
        button_edit.setOnAction(actionEvent -> {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_edit.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 400, 250);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно гаража 1
        personal_garage1.setOnAction(actionEvent -> {

            personal_garage1.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_garage.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();

        });

        // переход на окно гаража 2
        personal_garage2.setOnAction(actionEvent -> {

            personal_garage2.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_garage.fxml"));
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
        return ClientMainController.login;
    }

    public static void setLogin(String login) {
        ClientMainController.login = login;
    }

}
