package main;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseHandler extends Configs{
    private static Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(
                "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME,
                DB_USER, DB_PASS);
        return dbConnection;
    }
}
