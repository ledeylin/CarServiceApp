package special;

public class Cars {

    private String license_plate;

    private String model;

    private String make;

    public Cars(String license_plate, String model, String make) {
        this.license_plate = license_plate;
        this.model = model;
        this.make = make;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }
}
