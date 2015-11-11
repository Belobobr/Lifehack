package md.fusionworks.lifehack.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ungvas on 11/6/15.
 */
public class Day {

    private int id;
    private Date start;
    private Date end;
    @SerializedName("day_off")
    private boolean dayOff;

    public Day(int id, Date start, Date end, boolean dayOff) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.dayOff = dayOff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }
}
