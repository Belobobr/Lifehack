package md.fusionworks.lifehack.ui.model;

/**
 * Created by ungvas on 11/6/15.
 */
public class BranchModel {

    private int id;
    private String name;
    private AddressModel address;
    private ScheduleModel schedule;

    public BranchModel(int id, String name, AddressModel address, ScheduleModel schedule) {
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

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public ScheduleModel getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleModel schedule) {
        this.schedule = schedule;
    }
}
