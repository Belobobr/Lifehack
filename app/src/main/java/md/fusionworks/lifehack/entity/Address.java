package md.fusionworks.lifehack.entity;

/**
 * Created by ungvas on 11/6/15.
 */
public class Address {

    private Location location;
    private int id;
    private District district;
    private String street;
    private String house;
    private String office;
    private String phone;
    private String raw;

    public Address(Location location, int id, District district, String street, String house, String office, String phone, String raw) {
        this.location = location;
        this.id = id;
        this.district = district;
        this.street = street;
        this.house = house;
        this.office = office;
        this.phone = phone;
        this.raw = raw;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
