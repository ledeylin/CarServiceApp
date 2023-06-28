package special;

import java.sql.Date;

public class Services {

    private int id;

    private String work_time;

    private String mileage;

    private Date start_date;

    private Date final_date;

    private String id_employee;

    private String license_plate;

    private String detail_serial_number;

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
}
