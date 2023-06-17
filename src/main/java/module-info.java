module sample.d {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    exports main;
    opens main to javafx.fxml;
    exports controllers;
    opens controllers to javafx.fxml;
}