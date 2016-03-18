package md.fusionworks.lifehack.exchange_rates.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ungvas on 11/6/15.
 */
public class ScheduleModel implements Parcelable {

  private int id;
  private DayModel monday;
  private DayModel tuesday;
  private DayModel wednesday;
  private DayModel thursday;
  private DayModel friday;
  private DayModel saturday;
  private DayModel sunday;
  private String raw;

  public ScheduleModel(int id, DayModel monday, DayModel tuesday, DayModel wednesday,
      DayModel thursday, DayModel friday, DayModel saturday, DayModel sunday, String raw) {
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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeParcelable(this.monday, flags);
    dest.writeParcelable(this.tuesday, flags);
    dest.writeParcelable(this.wednesday, flags);
    dest.writeParcelable(this.thursday, flags);
    dest.writeParcelable(this.friday, flags);
    dest.writeParcelable(this.saturday, flags);
    dest.writeParcelable(this.sunday, flags);
    dest.writeString(this.raw);
  }

  private ScheduleModel(Parcel in) {
    this.id = in.readInt();
    this.monday = in.readParcelable(DayModel.class.getClassLoader());
    this.tuesday = in.readParcelable(DayModel.class.getClassLoader());
    this.wednesday = in.readParcelable(DayModel.class.getClassLoader());
    this.thursday = in.readParcelable(DayModel.class.getClassLoader());
    this.friday = in.readParcelable(DayModel.class.getClassLoader());
    this.saturday = in.readParcelable(DayModel.class.getClassLoader());
    this.sunday = in.readParcelable(DayModel.class.getClassLoader());
    this.raw = in.readString();
  }

  public static final Parcelable.Creator<ScheduleModel> CREATOR =
      new Parcelable.Creator<ScheduleModel>() {
        public ScheduleModel createFromParcel(Parcel source) {
          return new ScheduleModel(source);
        }

        public ScheduleModel[] newArray(int size) {
          return new ScheduleModel[size];
        }
      };
}
