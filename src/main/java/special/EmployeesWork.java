package special;

public class EmployeesWork {

    private String detail_category;

    private String work_time;

    public EmployeesWork(String detail_category, String work_time) {
        this.detail_category = detail_category;
        this.work_time = work_time;
    }

    public String getDetail_category() {
        return detail_category;
    }

    public String getWork_time() {
        return work_time;
    }
}
