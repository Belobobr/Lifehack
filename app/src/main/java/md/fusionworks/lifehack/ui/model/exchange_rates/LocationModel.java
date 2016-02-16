package md.fusionworks.lifehack.ui.model.exchange_rates;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ungvas on 11/6/15.
 */
public class LocationModel implements Parcelable {

  double lat;
  double lng;

  public LocationModel(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(this.lat);
    dest.writeDouble(this.lng);
  }

  private LocationModel(Parcel in) {
    this.lat = in.readDouble();
    this.lng = in.readDouble();
  }

  public static final Parcelable.Creator<LocationModel> CREATOR =
      new Parcelable.Creator<LocationModel>() {
        public LocationModel createFromParcel(Parcel source) {
          return new LocationModel(source);
        }

        public LocationModel[] newArray(int size) {
          return new LocationModel[size];
        }
      };
}
