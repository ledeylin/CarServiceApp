package other;

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

            PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            String selectedEmployeeLogin = null;
            int time;
            LocalDate startDate = null;
            LocalDate finalDate = null;

            if (resultSet.next()) {
                selectedEmployeeLogin = resultSet.getString("login");
                time = resultSet.getInt(EMPLOYEES_WORK_WORK_TIME);
                startDate = LocalDate.now().plusDays(2);
                finalDate = startDate.plusDays(time);
            }

            else {
                query = "SELECT e." + EMPLOYEES_LOGIN + ",\n" +
                        "DATE_ADD(MAX(s." + SERVICES_FINAL_DATE +
                        "), INTERVAL 2 DAY) AS start_date, \n" +
                        "DATE_ADD(DATE_ADD(MAX(s." + SERVICES_FINAL_DATE +
                        "), INTERVAL 2 DAY), INTERVAL ew." + EMPLOYEES_WORK_WORK_TIME +
                        " DAY) AS end_date,\n" +
                        "ew." + EMPLOYEES_WORK_WORK_TIME + "\n" +
                        "FROM " + EMPLOYEES_TABLE + " e\n" +
                        "JOIN " + EMPLOYEES_WORK_TABLE + " ew ON e." +
                        EMPLOYEES_LOGIN + " = ew." + EMPLOYEES_WORK_LOGIN + "\n" +
                        "JOIN " + SERVICES_TABLE + " s ON e." +
                        EMPLOYEES_LOGIN + " = s." + SERVICES_ID_EMPLOYEE + "\n" +
                        "JOIN " + DETAILS_TABLE + " d ON s." + SERVICES_DETAIL_SERIAL_NUMBER +
                        " = d." + DETAILS_SERIAL_NUMBER + "\n" +
                        "WHERE ew." + EMPLOYEES_WORK_DETAIL_CATEGORY + " = '" + detailCategory +  "'\n" +
                        "GROUP BY e." + EMPLOYEES_LOGIN + ", ew." + EMPLOYEES_WORK_WORK_TIME + "\n" +
                        "ORDER BY end_date ASC\n" +
                        "LIMIT 1;";
                statement = DatabaseHandler.getInstance().prepareStatement(query);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    selectedEmployeeLogin = resultSet.getString(EMPLOYEES_LOGIN);
                    startDate = resultSet.getDate("start_date").toLocalDate();
                    finalDate = resultSet.getDate("end_date").toLocalDate();
                }
            }

            if (selectedEmployeeLogin != null) {

                sql(String.valueOf(mileage), startDate, finalDate, selectedEmployeeLogin, licensePlate, detailCategory);

                Main.changeScene("client_garage.fxml");
            }

            else {
                System.out.println("Нет свободных сотрудников для выполнения работы");
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("client_message.fxml"));
                Scene scene;
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
            PreparedStatement statement = DatabaseHandler.getInstance().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
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
        try (Connection connection = DatabaseHandler.getInstance()) {
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