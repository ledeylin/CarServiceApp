package special;

import java.sql.Date;

public class Services {

    private int id;

    private String work_time;

    private String mileage;

    private String model;

    private String date;

    private int price;

    private Date start_date;

    private Date final_date;

    private String id_employee;

    private String id_client;

    private String license_plate;

    private String detail_serial_number;

    private String detail;

    public Services(int id, String work_time, String mileage,
                    Date start_date, Date final_date,
                    String id_employee, String license_plate,
                    String detail_serial_number) {
        this.id = id;
        this.work_time = work_time;
        this.mileage = mileage;
        this.start_date = start_date;
        this.final_date = final_date;
        this.id_employee = id_employee;
        this.license_plate = license_plate;
        this.detail_serial_number = detail_serial_number;
    }

    public Services(Date start_date, Date final_date,
                    String detail_serial_number) {
        this.start_date = start_date;
        this.final_date = final_date;
        this.detail_serial_number = detail_serial_number;
    }

    public Services(String mileage, Date start_date,
                    Date final_date, String detail) {
        this.mileage = mileage;
        this.start_date = start_date;
        this.final_date = final_date;
        this.detail = detail;
    }

    public Services(String work_time, String mileage,
                    String model, String date,
                    int price, String detail) {
        this.work_time = work_time;
        this.mileage = mileage;
        this.model = model;
        this.date = date;
        this.price = price;
        this.detail = detail;
    }

    public Services(int id, String model, int price,
                    Date start_date, Date final_date,
                    String id_employee, String id_client,
                    String license_plate, String detail_serial_number,
                    String detail, String date) {
        this.id = id;
        this.model = model;
        this.price = price;
        this.start_date = start_date;
        this.final_date = final_date;
        this.id_employee = id_employee;
        this.id_client = id_client;
        this.license_plate = license_plate;
        this.detail_serial_number = detail_serial_number;
        this.detail = detail;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getWork_time() {
        return work_time;
    }

    public String getMileage() {
        return mileage;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getFinal_date() {
        return final_date;
    }

    public String getId_employee() {
        return id_employee;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public String getDetail_serial_number() {
        return detail_serial_number;
    }

    public String getDetail() {
        return detail;
    }

    public String getModel() {
        return model;
    }

    public String getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public String getId_client() {
        return id_client;
    }
}
