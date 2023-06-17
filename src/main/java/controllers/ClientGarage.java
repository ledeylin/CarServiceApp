package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

public class ClientGarage {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button_add_car;

    @FXML
    private Button button_add_service;

    @FXML
    private Button button_edit;

    @FXML
    private Button button_personal_acc;

    @FXML
    private Button button_personal_edit;

    @FXML
    private Button button_personal_service;

    @FXML
    private ChoiceBox<?> choice_box_car;

    @FXML
    private ChoiceBox<?> choice_box_service;

    @FXML
    private Text text_detail;

    @FXML
    private Text text_final_date;

    @FXML
    private Text text_license_plate;

    @FXML
    private Text text_mark;

    @FXML
    private Text text_model;

    @FXML
    private Text text_price;

    @FXML
    private Text text_services;

    @FXML
    private Text text_start_date;

    @FXML
    void initialize() {
        assert button_add_car != null : "fx:id=\"button_add_car\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert button_add_service != null : "fx:id=\"button_add_service\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert button_edit != null : "fx:id=\"button_edit\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert button_personal_acc != null : "fx:id=\"button_personal_acc\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert button_personal_edit != null : "fx:id=\"button_personal_edit\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert button_personal_service != null : "fx:id=\"button_personal_service\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert choice_box_car != null : "fx:id=\"choice_box_car\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert choice_box_service != null : "fx:id=\"choice_box_service\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert text_detail != null : "fx:id=\"text_detail\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert text_final_date != null : "fx:id=\"text_final_date\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert text_license_plate != null : "fx:id=\"text_license_plate\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert text_mark != null : "fx:id=\"text_mark\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert text_model != null : "fx:id=\"text_model\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert text_price != null : "fx:id=\"text_price\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert text_services != null : "fx:id=\"text_services\" was not injected: check your FXML file 'client_garage.fxml'.";
        assert text_start_date != null : "fx:id=\"text_start_date\" was not injected: check your FXML file 'client_garage.fxml'.";

    }

}
