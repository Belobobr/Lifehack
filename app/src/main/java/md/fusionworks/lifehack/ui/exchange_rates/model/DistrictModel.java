package md.fusionworks.lifehack.ui.exchange_rates.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ungvas on 11/6/15.
 */
public class DistrictModel implements Parcelable {

  private String id;
  private String name;
  private LocalityModel locality;

  public DistrictModel(String id, String name, LocalityModel locality) {
    this.id = id;
    this.name = name;
    this.locality = locality;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalityModel getLocality() {
    return locality;
  }

  public void setLocality(LocalityModel locality) {
    this.locality = locality;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeParcelable(this.locality, flags);
  }

  private DistrictModel(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.locality = in.readParcelable(LocalityModel.class.getClassLoader());
  }

  public static final Parcelable.Creator<DistrictModel> CREATOR =
      new Parcelable.Creator<DistrictModel>() {
        public DistrictModel createFromParcel(Parcel source) {
          return new DistrictModel(source);
        }

        public DistrictModel[] newArray(int size) {
          return new DistrictModel[size];
        }
      };
}
