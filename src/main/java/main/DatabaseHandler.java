package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler extends Configs{
    private static Connection dbConnection;

    public static Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver" );
        dbConnection = DriverManager.getConnection(
                "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME,
                DB_USER, DB_PASS);
        return dbConnection;
    }

    public static synchronized Connection getInstance() throws SQLException, ClassNotFoundException {
        if (dbConnection == null) {
            dbConnection = getDbConnection();
        }
        return dbConnection;
    }

}