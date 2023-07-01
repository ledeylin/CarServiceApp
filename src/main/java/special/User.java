package special;

public class User {

    private String last_name;

    private String first_name;

    private String second_name;

    private String address;

    private String phone_number;

    private String login;

    private String password;

    private String car_now;

    private String car_old;

    private String cars;

    private String work_time;

    private String salary;

    private String service_count;

    private String name;

    private String status; // 0 - block 1 - active

    private String post; // 0 - client 1 - employee 2 - admin

    // client
    public User(String last_name, String first_name,
                String second_name, String address,
                String phone_number, String login,
                String password, String post,
                String car_now, String car_old) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.second_name = second_name;
        this.address = address;
        this.phone_number = phone_number;
        this.login = login;
        this.password = password;
        this.post = post;
        this.car_now = car_now;
        this.car_old = car_old;
    }

    // client
    public User(String last_name, String first_name,
                String second_name, String address,
                String phone_number, String login,
                String password, String cars,
                String service_count, String post, String name) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.second_name = second_name;
        this.address = address;
        this.phone_number = phone_number;
        this.login = login;
        this.password = password;
        this.cars = cars;
        this.service_count = service_count;
        this.post = post;
        this.name = name;
    }

    //employee
    public User(String last_name, String first_name,
                String second_name, String address,
                String login, String password,
                String post, String work_time,
                String salary, String service_count,
                String name, int y) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.second_name = second_name;
        this.address = address;
        this.login = login;
        this.password = password;
        this.post = post;
        this.work_time = work_time;
        this.salary = salary;
        this.service_count = service_count;
        this.name = name;
    }

    //employee
    public User(String last_name, String first_name,
                String second_name, String address,
                String login, String password,
                String post, String work_time,
                String salary) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.second_name = second_name;
        this.address = address;
        this.login = login;
        this.password = password;
        this.post = post;
        this.work_time = work_time;
        this.salary = salary;
    }

    // admin


    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPost() {
        return post;
    }

    public String getCar_now() {
        return car_now;
    }

    public String getCar_old() {
        return car_old;
    }

    public String getWork_time() {
        return work_time;
    }

    public String getSalary() {
        return salary;
    }

    public String getStatus() {
        return status;
    }

    public String getService_count() {
        return service_count;
    }

    public String getCars() {
        return cars;
    }

    public String getName() {
        return name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCar_now(String car_now) {
        this.car_now = car_now;
    }

    public void setCar_old(String car_old) {
        this.car_old = car_old;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setService_count(String service_count) {
        this.service_count = service_count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
