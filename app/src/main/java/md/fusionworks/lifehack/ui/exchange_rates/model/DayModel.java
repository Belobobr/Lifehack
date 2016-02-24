package md.fusionworks.lifehack.ui.exchange_rates.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ungvas on 11/6/15.
 */
public class DayModel implements Parcelable {

  private int id;
  private Date start;
  private Date end;
  @SerializedName("break_start") private Date breakStart;
  @SerializedName("break_end") private Date breakEnd;
  @SerializedName("day_off") private boolean dayOff;

  public DayModel(int id, Date start, Date end, Date breakStart, Date breakEnd, boolean dayOff) {
    this.id = id;
    this.start = start;
    this.end = end;
    this.breakStart = breakStart;
    this.breakEnd = breakEnd;
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

  public Date getBreakStart() {
    return breakStart;
  }

  public void setBreakStart(Date breakStart) {
    this.breakStart = breakStart;
  }

  public Date getBreakEnd() {
    return breakEnd;
  }

  public void setBreakEnd(Date breakEnd) {
    this.breakEnd = breakEnd;
  }

  public boolean isDayOff() {
    return dayOff;
  }

  public void setDayOff(boolean dayOff) {
    this.dayOff = dayOff;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeLong(start != null ? start.getTime() : -1);
    dest.writeLong(end != null ? end.getTime() : -1);
    dest.writeLong(breakStart != null ? breakStart.getTime() : -1);
    dest.writeLong(breakEnd != null ? breakEnd.getTime() : -1);
    dest.writeByte(dayOff ? (byte) 1 : (byte) 0);
  }

  private DayModel(Parcel in) {
    this.id = in.readInt();
    long tmpStart = in.readLong();
    this.start = tmpStart == -1 ? null : new Date(tmpStart);
    long tmpEnd = in.readLong();
    this.end = tmpEnd == -1 ? null : new Date(tmpEnd);
    long tmpBreakStart = in.readLong();
    this.breakStart = tmpBreakStart == -1 ? null : new Date(tmpBreakStart);
    long tmpBreakEnd = in.readLong();
    this.breakEnd = tmpBreakEnd == -1 ? null : new Date(tmpBreakEnd);
    this.dayOff = in.readByte() != 0;
  }

  public static final Parcelable.Creator<DayModel> CREATOR = new Parcelable.Creator<DayModel>() {
    public DayModel createFromParcel(Parcel source) {
      return new DayModel(source);
    }

    public DayModel[] newArray(int size) {
      return new DayModel[size];
    }
  };
}
