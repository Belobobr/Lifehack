package md.fusionworks.lifehack.data.api.model;

/**
 * Created by ungvas on 11/6/15.
 */
public class Branch {

    private int id;
    private String name;
    private Address address;
    private Schedule schedule;

    public Branch(int id, String name, Address address, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
