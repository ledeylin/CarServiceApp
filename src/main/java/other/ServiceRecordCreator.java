package other;

import controllers.ClientGarageEditCarController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Constants;
import main.DatabaseHandler;
import main.Main;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class ServiceRecordCreator extends Constants {

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    public static void createServiceRecord(int mileage, String detailCategory, String licensePlate) {

        try {

            String query = "SELECT *\n" +
                    "FROM " + EMPLOYEES_TABLE + " e\n" +
                    "INNER JOIN " + EMPLOYEES_WORK_TABLE + " ew ON ew." +
                    EMPLOYEES_WORK_LOGIN + " = e." + EMPLOYEES_LOGIN + "\n" +
                    "WHERE (ew." + EMPLOYEES_WORK_DETAIL_CATEGORY + " = '" +
                    detailCategory + "') AND (e." + EMPLOYEES_LOGIN +
                    " NOT IN (\n" +
                    "    SELECT " + SERVICES_ID_EMPLOYEE + "\n" +
                    "    FROM " + SERVICES_TABLE + "\n" +
                    "    WHERE " + SERVICES_FINAL_DATE + " > CURDATE()))\n" +
                    "ORDER BY " + EMPLOYEES_WORK_WORK_TIME + " ASC\n" +
                    "LIMIT 1;";
            PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            String selectedEmployeeLogin = null;
            int time = 0;

            if (resultSet.next()) {
                selectedEmployeeLogin = resultSet.getString("login");
                time = resultSet.getInt(EMPLOYEES_WORK_WORK_TIME);
            }

            if (selectedEmployeeLogin != null) {

                LocalDate startDate = LocalDate.now().plusDays(2);
                LocalDate finalDate = startDate.plusDays(time);

                sql(String.valueOf(mileage), startDate, finalDate, selectedEmployeeLogin, licensePlate, detailCategory);
            }

            else {
                System.out.println("Нет свободных сотрудников для выполнения работы");
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_message.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 330, 140);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String getDetailSerialNumber(String detailCategory, String licensePlate) {
        try {
            String query = "SELECT d." +
                    DETAILS_SERIAL_NUMBER + "\n" +
                    "FROM " + DETAILS_TABLE + " d \n" +
                    "INNER JOIN " + DETAILS_COMPATIBILITY_TABLE + " ds ON ds." +
                    DETAILS_COMPATIBILITY_DETAIL_SERIAL_NUMBER + " = d." +
                    DETAILS_SERIAL_NUMBER + "\n" +
                    "INNER JOIN " + CARS_TABLE + " c ON c." + CARS_MODEL + " = ds." +
                    DETAILS_COMPATIBILITY_MODEL + "\n" +
                    "WHERE c." + CARS_LICENSE_PLATE + " = '" +
                    licensePlate +  "' AND d." + DETAILS_CATEGORY +
                    " = '" + detailCategory + "'";
            PreparedStatement statement = databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            System.out.println(query);
            if (resultSet.next()) {
                return resultSet.getString(DETAILS_SERIAL_NUMBER);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void sql(String mileage, LocalDate startDate, LocalDate finalDate,
                            String selectedEmployeeLogin, String licensePlate,
                            String detailCategory) {
        try (Connection connection = databaseHandler.getDbConnection()) {
            Statement statement = connection.createStatement();

        String insertQuery = "INSERT INTO " + SERVICES_TABLE + " (" + SERVICES_WORK_TIME + ", " +
                SERVICES_MILEAGE + ", " + SERVICES_START_DATE + ", " + SERVICES_FINAL_DATE +
                ", " + SERVICES_ID_EMPLOYEE + ", " + SERVICES_LICENSE_PLATE + ", " +
                SERVICES_DETAIL_SERIAL_NUMBER + ") " + "VALUES (" + "0" +
                ", " + mileage + ", '" + startDate + "', '" + finalDate + "', '" +
                selectedEmployeeLogin + "', '" + licensePlate + "', '" +
                getDetailSerialNumber(detailCategory, licensePlate) + "')";

            statement.executeUpdate(insertQuery);
            System.out.println("Запись успешно создана");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

    }

}