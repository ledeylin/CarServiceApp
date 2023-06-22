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

public class ClientAcc extends Constants {

    @FXML
    private Button personal_acc;

    @FXML
    private Button personal_edit;

    @FXML
    private Button personal_garage;

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

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // отображение информации о сотруднике

        String query = "SELECT c.*, \n" +
                "       SUM(CASE WHEN car.status = '1' THEN 1 ELSE 0 END) AS active_cars, \n" +
                "       SUM(CASE WHEN car.status = '0' THEN 1 ELSE 0 END) AS inactive_cars\n" +
                "FROM clients AS c\n" +
                "LEFT JOIN car ON c.login = car.id_owner\n" +
                "WHERE c.login = '" + login + "'\n" +
                "GROUP BY c.login\n" +
                "\n";

        System.out.println(query);


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

        // переход на окно редактирования информации
        personal_edit.setOnAction(actionEvent -> {

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

        // переход на окно гаража
        personal_garage.setOnAction(actionEvent -> {

            personal_garage.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_garage.fxml"));
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
        ClientAcc.login = login;
    }
}
