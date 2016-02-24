package md.fusionworks.lifehack.ui.exchange_rates.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ungvas on 11/6/15.
 */
public class BranchModel implements Parcelable {

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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
    dest.writeParcelable(this.address, flags);
    dest.writeParcelable(this.schedule, flags);
  }

  private BranchModel(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
    this.address = in.readParcelable(AddressModel.class.getClassLoader());
    this.schedule = in.readParcelable(ScheduleModel.class.getClassLoader());
  }

  public static final Parcelable.Creator<BranchModel> CREATOR =
      new Parcelable.Creator<BranchModel>() {
        public BranchModel createFromParcel(Parcel source) {
          return new BranchModel(source);
        }

        public BranchModel[] newArray(int size) {
          return new BranchModel[size];
        }
      };
}
