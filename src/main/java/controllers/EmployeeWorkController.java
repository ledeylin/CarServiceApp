package controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Constants;
import main.DatabaseHandler;
import main.Main;
import special.EmployeesWork;
import special.Services;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Objects;

public class EmployeeWorkController extends Constants {

    @FXML
    private Button button_add;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_menu1;

    @FXML
    private Button button_menu2;

    @FXML
    private Button button_menu_close;

    @FXML
    private ChoiceBox<String> choice_box_detail;

    @FXML
    private VBox pane_menu;

    @FXML
    private Button personal_acc1;

    @FXML
    private Button personal_acc2;

    @FXML
    private Button personal_service1;

    @FXML
    private Button personal_service2;

    @FXML
    private TableView<Services> table_services;

    @FXML
    private TableColumn<Services, String> services_detail;

    @FXML
    private TableColumn<Services, Date> services_final;

    @FXML
    private TableColumn<Services, Date> services_start;

    @FXML
    private TableView<EmployeesWork> table_work;

    @FXML
    private TableColumn<EmployeesWork, String> details_detail;

    @FXML
    private TableColumn<EmployeesWork, String> details_time;

    @FXML
    private TextField text_time;

    @FXML
    private Text text_mistake;

    private static String time = "";

    private static String detail = "";

    private static String detail_service = "";

    private boolean pane_flag = false;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        // таблица с деталями
        String query = "SELECT * FROM " + EMPLOYEES_WORK_TABLE + " WHERE " + EMPLOYEES_WORK_LOGIN +
                " = '" + EmployeeMainController.getLogin() + "';";
        PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query);
        ResultSet result = statement.executeQuery();

        ObservableList<EmployeesWork> ew1 = FXCollections.observableArrayList();
        details_detail.setCellValueFactory(new PropertyValueFactory<>("detail_category"));
        details_time.setCellValueFactory(new PropertyValueFactory<>("work_time"));

        while(result.next()) {
            String detail_category = result.getString(EMPLOYEES_WORK_DETAIL_CATEGORY);
            String work_time = result.getString(EMPLOYEES_WORK_WORK_TIME);
            ew1.add(new EmployeesWork(detail_category, work_time));
        }
        table_work.setItems(ew1);

        TableView.TableViewSelectionModel<EmployeesWork> selectionModel = table_work.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observableValue, employeesWork, t1) -> detail_service = t1.getDetail_category());

        // таблица с услугами
        query = "SELECT * FROM " + SERVICES_TABLE + " WHERE " + SERVICES_ID_EMPLOYEE +
                " = '" + EmployeeMainController.getLogin() +
                "' AND " + SERVICES_FINAL_DATE + " > CURDATE();";
        statement = DatabaseHandler.getInstance().prepareStatement(query);
        result = statement.executeQuery();

        ObservableList<Services> ew2 = FXCollections.observableArrayList();
        services_detail.setCellValueFactory(
                new PropertyValueFactory<>(SERVICES_DETAIL_SERIAL_NUMBER));
        services_start.setCellValueFactory(
                new PropertyValueFactory<>(SERVICES_START_DATE));
        services_final.setCellValueFactory(
                new PropertyValueFactory<>(SERVICES_FINAL_DATE));

        while(result.next()) {
            String detail_serial_number = result.getString(SERVICES_DETAIL_SERIAL_NUMBER);
            Date start_date = result.getDate(SERVICES_START_DATE);
            Date final_date = result.getDate(SERVICES_FINAL_DATE);
            ew2.add(new Services(start_date, final_date, detail_serial_number));
        }
        table_services.setItems(ew2);


        // choice box
        query = "SELECT * FROM " + DETAILS_TABLE;
        statement = DatabaseHandler.getInstance().prepareStatement(query);
        result = statement.executeQuery();
        HashSet<String> hs = new HashSet<>();

        while(result.next()) {
            String n = result.getString(DETAILS_CATEGORY);
            hs.add(n);
        }
        choice_box_detail.getItems().setAll(hs);

        choice_box_detail.setOnAction(actionEvent -> detail = choice_box_detail.getValue());

        // добавление детали
        button_add.setOnMouseClicked(mouseEvent -> {
            boolean flag = true;
            // проверка на пустоту данных
            if (Objects.equals(detail, "")) {
                flag = false;
                text_mistake.setText("Вы не выбрали деталь");
            }
            time = text_time.getText();
            if (Objects.equals(time, "")) {
                flag = false;
                text_mistake.setText("Вы не ввели время работы");
            }
            // проверка пароля пользователя
            if (flag) {
                PassController.setId(15);
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pass.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 400, 250);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();
            }
        });

        // удаление детали
        button_delete.setOnMouseClicked(mouseEvent -> {
            boolean flag = !Objects.equals(detail_service, "");
            // проверка на пустоту данных

            // проверка пароля пользователя
            if (flag) {
                PassController.setId(16);
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pass.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 400, 250);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();
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

        // переход на окно просмотра услуг 1
        personal_service1.setOnAction(actionEvent -> Main.changeScene("employee_services.fxml"));

        // переход на окно просмотра услуг 2
        personal_service2.setOnAction(actionEvent -> Main.changeScene("employee_services.fxml"));

        // переход на main 1
        personal_acc1.setOnAction(actionEvent -> Main.changeScene("employee_main.fxml"));

        // переход на main 2
        personal_acc2.setOnAction(actionEvent -> Main.changeScene("employee_main.fxml"));
    }

    public static void add() throws SQLException, ClassNotFoundException {
        String insertNew = "INSERT INTO " + EMPLOYEES_WORK_TABLE + " (" + EMPLOYEES_WORK_LOGIN + ", " +
                EMPLOYEES_WORK_DETAIL_CATEGORY + ", " +
                EMPLOYEES_WORK_WORK_TIME + ") " +
                "VALUES(?, ?, ?)";

        PreparedStatement preparedStatement = DatabaseHandler.getInstance().prepareStatement(insertNew);
        preparedStatement.setString(1, EmployeeMainController.getLogin());
        preparedStatement.setString(2, detail);
        preparedStatement.setString(3, time);
        preparedStatement.executeUpdate();

        Main.changeScene("employee_work_time.fxml");

    }

    public static void delete() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getInstance();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + EMPLOYEES_WORK_TABLE + " WHERE " +
                EMPLOYEES_WORK_LOGIN + " = '" + EmployeeMainController.getLogin() +
                "' AND " + EMPLOYEES_WORK_DETAIL_CATEGORY + " = '" + detail_service + "';");

        Main.changeScene("employee_work_time.fxml");

    }

}
