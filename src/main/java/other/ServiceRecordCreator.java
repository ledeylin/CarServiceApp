package other;

import main.DatabaseHandler;

import java.sql.*;
import java.time.LocalDate;

public class ServiceRecordCreator {

    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    public static void createServiceRecord(int mileage, String detailCategory, String licensePlate) {

        try (Connection connection = databaseHandler.getDbConnection()) {
            Statement statement = connection.createStatement();

            // Получаем логины сотрудников, которые могут выполнять работу данной категории детали
            ResultSet resultSet = statement.executeQuery("SELECT login FROM employees_work WHERE detail_category = '" + detailCategory + "'");
            String selectedEmployeeLogin = null;

            // Выбираем сотрудника, который закончит работу раньше остальных и у него нет других работ на выбранные даты
            while (resultSet.next()) {
                String employeeLogin = resultSet.getString("login");
                if (!hasScheduledWorkOnDates(employeeLogin)) {
                    selectedEmployeeLogin = employeeLogin;
                    break;
                }
            }
            if (selectedEmployeeLogin != null) {
                // Получаем данные о выбранном сотруднике
                ResultSet employeeResultSet = statement.executeQuery("SELECT work_time_in_day FROM employees_work WHERE login = '" + selectedEmployeeLogin + "'");
                int employeeWorkTimeInDay = employeeResultSet.getInt("work_time_in_day");

                // Вычисляем даты начала и завершения работы
                LocalDate startDate = getStartDate(selectedEmployeeLogin);
                LocalDate finalDate = startDate.plusDays(employeeWorkTimeInDay);

                // Генерируем SQL-запрос для создания новой записи в таблице services
                String insertQuery = "INSERT INTO services (work_time, mileage, start_date, final_date, id_employee, license_plate, detail_serial_number) " +
                        "VALUES (" + employeeWorkTimeInDay + ", " + mileage + ", '" + startDate + "', '" + finalDate + "', '" + selectedEmployeeLogin + "', '" + licensePlate + "', '" + getDetailSerialNumber(detailCategory, licensePlate) + "')";

                // Выполняем SQL-запрос
                statement.executeUpdate(insertQuery);

                System.out.println("Запись успешно создана");
            } else {
                System.out.println("Нет свободных сотрудников для выполнения работы");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean hasScheduledWorkOnDates(String employeeLogin) {

        LocalDate currentDate = LocalDate.now();
        try (Connection connection = databaseHandler.getDbConnection()) {
            Statement statement = connection.createStatement();

            // Проверяем, есть ли у выбранного сотрудника работы на ближайшие даты
            ResultSet resultSet = statement.executeQuery("SELECT * FROM services WHERE id_employee = '" + employeeLogin + "' AND (start_date <= '" + currentDate + "' OR final_date >= '" + currentDate + "')");
            return resultSet.next();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static LocalDate getStartDate(String employeeLogin) {
        LocalDate currentDate = LocalDate.now().plusDays(1);
        try (Connection connection = databaseHandler.getDbConnection()) {
            Statement statement = connection.createStatement();

            // Получаем дату последней работы выбранного сотрудника
            ResultSet resultSet = statement.executeQuery("SELECT MAX(final_date) AS last_work_date FROM services WHERE id_employee = '" + employeeLogin + "'");
            if (resultSet.next()) {
                LocalDate lastWorkDate = resultSet.getDate("last_work_date").toLocalDate();
                if (lastWorkDate.isAfter(currentDate)) {
                    return lastWorkDate.plusDays(1);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    private static String getDetailSerialNumber(String detailCategory, String licensePlate) {
        try (Connection connection = databaseHandler.getDbConnection()) {
            Statement statement = connection.createStatement();

            // Получаем серийный номер детали, соответствующей выбранной категории и гос. номеру машины
            ResultSet resultSet = statement.executeQuery("SELECT detail_serial_number FROM details_compatibilities WHERE model = '" + licensePlate + "' AND detail_category = '" + detailCategory + "'");
            if (resultSet.next()) {
                return resultSet.getString("detail_serial_number");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}