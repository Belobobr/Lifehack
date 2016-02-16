package md.fusionworks.lifehack.ui.model.exchange_rates;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ungvas on 11/6/15.
 */
public class LocalityModel implements Parcelable {

  private int id;
  private String name;
  private String code;

  public LocalityModel(int id, String name, String code) {
    this.id = id;
    this.name = name;
    this.code = code;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
    dest.writeString(this.code);
  }

  private LocalityModel(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
    this.code = in.readString();
  }

  public static final Parcelable.Creator<LocalityModel> CREATOR =
      new Parcelable.Creator<LocalityModel>() {
        public LocalityModel createFromParcel(Parcel source) {
          return new LocalityModel(source);
        }

        public LocalityModel[] newArray(int size) {
          return new LocalityModel[size];
        }
      };
}
