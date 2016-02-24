package md.fusionworks.lifehack.ui.exchange_rates.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ungvas on 11/6/15.
 */
public class AddressModel implements Parcelable {

  private LocationModel location;
  private int id;
  private DistrictModel district;
  private String street;
  private String house;
  private String office;
  private String phone;
  private String raw;

  public AddressModel(LocationModel location, int id, DistrictModel district, String street,
      String house, String office, String phone, String raw) {
    this.location = location;
    this.id = id;
    this.district = district;
    this.street = street;
    this.house = house;
    this.office = office;
    this.phone = phone;
    this.raw = raw;
  }

  public LocationModel getLocation() {
    return location;
  }

  public void setLocation(LocationModel location) {
    this.location = location;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public DistrictModel getDistrict() {
    return district;
  }

  public void setDistrict(DistrictModel district) {
    this.district = district;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getHouse() {
    return house;
  }

  public void setHouse(String house) {
    this.house = house;
  }

  public String getOffice() {
    return office;
  }

  public void setOffice(String office) {
    this.office = office;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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
    dest.writeParcelable(this.location, flags);
    dest.writeInt(this.id);
    dest.writeParcelable(this.district, flags);
    dest.writeString(this.street);
    dest.writeString(this.house);
    dest.writeString(this.office);
    dest.writeString(this.phone);
    dest.writeString(this.raw);
  }

  private AddressModel(Parcel in) {
    this.location = in.readParcelable(LocationModel.class.getClassLoader());
    this.id = in.readInt();
    this.district = in.readParcelable(DistrictModel.class.getClassLoader());
    this.street = in.readString();
    this.house = in.readString();
    this.office = in.readString();
    this.phone = in.readString();
    this.raw = in.readString();
  }

  public static final Parcelable.Creator<AddressModel> CREATOR =
      new Parcelable.Creator<AddressModel>() {
        public AddressModel createFromParcel(Parcel source) {
          return new AddressModel(source);
        }

        public AddressModel[] newArray(int size) {
          return new AddressModel[size];
        }
      };
}
