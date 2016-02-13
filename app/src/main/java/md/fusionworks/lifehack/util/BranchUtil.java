package md.fusionworks.lifehack.util;

import android.text.TextUtils;
import java.util.Date;
import md.fusionworks.lifehack.ui.model.exchange_rates.BranchModel;

/**
 * Created by ungvas on 2/12/16.
 */
public class BranchUtil {

  public static String getBranchAddress(BranchModel branch) {
    String address = "";
    String locality = branch.getAddress().getDistrict().getLocality().getName();
    String district = branch.getAddress().getDistrict().getName();
    String street = branch.getAddress().getStreet();
    String house = branch.getAddress().getHouse();
    String phone = branch.getAddress().getPhone();

    if (!isBranchDetailEmpty(locality)) address += locality;
    if (!isBranchDetailEmpty(district)) address += " ," + district;
    if (!isBranchDetailEmpty(street) || !isBranchDetailEmpty(house)) {
      address += " ," + street + " " + house;
    }
    if (!isBranchDetailEmpty(phone)) address += "\n" + phone;

    return address;
  }

  public static boolean isBranchDetailEmpty(String detail) {
    if (TextUtils.isEmpty(detail) || detail.equals("-")) return true;
    return false;
  }

  public static String getBranchScheduleBreak(BranchModel branch) {
    String scheduleBreak = "";
    Date breakStart = null;
    Date breakEnd = null;

    if (branch.getSchedule().getMonday().getBreakStart() != null) {
      breakStart = branch.getSchedule().getMonday().getBreakStart();
      breakEnd = branch.getSchedule().getMonday().getBreakEnd();
    } else if (branch.getSchedule().getTuesday().getBreakStart() != null) {
      breakStart = branch.getSchedule().getTuesday().getBreakStart();
      breakEnd = branch.getSchedule().getTuesday().getBreakEnd();
    } else if (branch.getSchedule().getThursday().getBreakStart() != null) {
      breakStart = branch.getSchedule().getThursday().getBreakStart();
      breakEnd = branch.getSchedule().getThursday().getBreakEnd();
    } else if (branch.getSchedule().getWednesday().getBreakStart() != null) {
      breakStart = branch.getSchedule().getWednesday().getBreakStart();
      breakEnd = branch.getSchedule().getWednesday().getBreakEnd();
    } else if (branch.getSchedule().getFriday().getBreakStart() != null) {
      breakStart = branch.getSchedule().getFriday().getBreakStart();
      breakEnd = branch.getSchedule().getFriday().getBreakEnd();
    } else if (branch.getSchedule().getSaturday().getBreakStart() != null) {
      breakStart = branch.getSchedule().getSaturday().getBreakStart();
      breakEnd = branch.getSchedule().getSaturday().getBreakEnd();
    } else if (branch.getSchedule().getSunday().getBreakStart() != null) {
      breakStart = branch.getSchedule().getSunday().getBreakStart();
      breakEnd = branch.getSchedule().getSunday().getBreakEnd();
    }

    if (breakStart != null && breakEnd != null) {
      String start = DateUtil.getBranchScheduleBreakFormat().format(breakStart);
      String end = DateUtil.getBranchScheduleBreakFormat().format(breakEnd);
      scheduleBreak = String.format("(перерыв %s - %s)", start, end);
    }
    return scheduleBreak;
  }
}
