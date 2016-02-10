package md.fusionworks.lifehack.ui.model;

import md.fusionworks.lifehack.data.api.model.Day;

/**
 * Created by ungvas on 11/6/15.
 */
public class ScheduleModel {

    private int id;
    private DayModel monday;
    private DayModel tuesday;
    private DayModel wednesday;
    private DayModel thursday;
    private DayModel friday;
    private DayModel saturday;
    private DayModel sunday;
    private String raw;

    public ScheduleModel(int id, DayModel monday, DayModel tuesday, DayModel wednesday, DayModel thursday, DayModel friday, DayModel saturday, DayModel sunday, String raw) {
        this.id = id;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.raw = raw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DayModel getMonday() {
        return monday;
    }

    public void setMonday(DayModel monday) {
        this.monday = monday;
    }

    public DayModel getTuesday() {
        return tuesday;
    }

    public void setTuesday(DayModel tuesday) {
        this.tuesday = tuesday;
    }

    public DayModel getWednesday() {
        return wednesday;
    }

    public void setWednesday(DayModel wednesday) {
        this.wednesday = wednesday;
    }

    public DayModel getThursday() {
        return thursday;
    }

    public void setThursday(DayModel thursday) {
        this.thursday = thursday;
    }

    public DayModel getFriday() {
        return friday;
    }

    public void setFriday(DayModel friday) {
        this.friday = friday;
    }

    public DayModel getSaturday() {
        return saturday;
    }

    public void setSaturday(DayModel saturday) {
        this.saturday = saturday;
    }

    public DayModel getSunday() {
        return sunday;
    }

    public void setSunday(DayModel sunday) {
        this.sunday = sunday;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
