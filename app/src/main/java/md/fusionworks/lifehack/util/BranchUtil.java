package md.fusionworks.lifehack.util;

import android.content.Context;
import android.text.TextUtils;
import java.util.Calendar;
import java.util.Date;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;

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

    if (!isBranchDetailEmpty(locality)) address += locality;
    if (!isBranchDetailEmpty(district)) address += ", " + district;
    if (!isBranchDetailEmpty(street) || !isBranchDetailEmpty(house)) {
      address += ", " + street + " " + house;
    }
    return address;
  }

  public static boolean isBranchDetailEmpty(String detail) {
    if (TextUtils.isEmpty(detail) || detail.equals("-")) return true;
    return false;
  }

  public static String getBranchMondayFridayScheduleBreak(Context context, BranchModel branch) {
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
      scheduleBreak =
          String.format(context.getString(R.string.field_branch_schedule_break), start, end);
    }
    return scheduleBreak;
  }

  public static String getBranchSaturdayScheduleBreak(Context context, BranchModel branch) {
    String scheduleBreak = "";
    Date breakStart = null;
    Date breakEnd = null;

    if (branch.getSchedule().getSaturday().getBreakStart() != null) {
      breakStart = branch.getSchedule().getSaturday().getBreakStart();
      breakEnd = branch.getSchedule().getSaturday().getBreakEnd();
    }

    if (breakStart != null && breakEnd != null) {
      String start = DateUtil.getBranchScheduleBreakFormat().format(breakStart);
      String end = DateUtil.getBranchScheduleBreakFormat().format(breakEnd);
      scheduleBreak =
          String.format(context.getString(R.string.field_branch_schedule_break), start, end);
    }
    return scheduleBreak;
  }

  public static String getBranchMondayFridayHours(BranchModel branch, String format) {
    String mondayFridayHours = "";
    Date start = null;
    Date end = null;

    if (branch.getSchedule().getMonday().getStart() != null) {
      start = branch.getSchedule().getMonday().getStart();
      end = branch.getSchedule().getMonday().getEnd();
    } else if (branch.getSchedule().getTuesday().getStart() != null) {
      start = branch.getSchedule().getTuesday().getStart();
      end = branch.getSchedule().getTuesday().getEnd();
    } else if (branch.getSchedule().getThursday().getStart() != null) {
      start = branch.getSchedule().getThursday().getStart();
      end = branch.getSchedule().getThursday().getEnd();
    } else if (branch.getSchedule().getWednesday().getStart() != null) {
      start = branch.getSchedule().getWednesday().getStart();
      end = branch.getSchedule().getWednesday().getEnd();
    } else if (branch.getSchedule().getFriday().getStart() != null) {
      start = branch.getSchedule().getFriday().getStart();
      end = branch.getSchedule().getFriday().getEnd();
    }

    if (start != null && end != null) {
      String startStr = DateUtil.getBranchScheduleBreakFormat().format(start);
      String endStr = DateUtil.getBranchScheduleBreakFormat().format(end);
      mondayFridayHours = String.format(format, startStr, endStr);
    }
    return mondayFridayHours;
  }

  public static String getBranchSaturdayHours(BranchModel branch, String format) {
    String saturdayHours = "";
    Date start = null;
    Date end = null;

    if (branch.getSchedule().getSaturday().getStart() != null) {
      start = branch.getSchedule().getSaturday().getStart();
      end = branch.getSchedule().getSaturday().getEnd();
    }

    if (start != null && end != null) {
      String startStr = DateUtil.getBranchScheduleBreakFormat().format(start);
      String endStr = DateUtil.getBranchScheduleBreakFormat().format(end);
      saturdayHours = String.format(format, startStr, endStr);
    }
    return saturdayHours;
  }

  public static String getClosingTime(Context context, BranchModel branch) {
    Date scheduleDateEnd = null;
    Date scheduleDateStart = null;
    Date currentDate;
    Calendar calendar = Calendar.getInstance();
    currentDate = calendar.getTime();
    int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
    switch (currentDay) {
      case Calendar.MONDAY:
        if (branch.getSchedule().getMonday().getEnd() != null) {
          scheduleDateEnd = branch.getSchedule().getMonday().getEnd();
        }
        if (branch.getSchedule().getMonday().getStart() != null) {
          scheduleDateStart = branch.getSchedule().getMonday().getStart();
        }
        break;
      case Calendar.TUESDAY:
        if (branch.getSchedule().getTuesday().getEnd() != null) {
          scheduleDateEnd = branch.getSchedule().getTuesday().getEnd();
        }
        if (branch.getSchedule().getTuesday().getStart() != null) {
          scheduleDateStart = branch.getSchedule().getTuesday().getStart();
        }
        break;
      case Calendar.THURSDAY:
        if (branch.getSchedule().getThursday().getEnd() != null) {
          scheduleDateEnd = branch.getSchedule().getThursday().getEnd();
        }
        if (branch.getSchedule().getThursday().getStart() != null) {
          scheduleDateStart = branch.getSchedule().getThursday().getStart();
        }
        break;
      case Calendar.WEDNESDAY:
        if (branch.getSchedule().getWednesday().getEnd() != null) {
          scheduleDateEnd = branch.getSchedule().getWednesday().getEnd();
        }
        if (branch.getSchedule().getWednesday().getStart() != null) {
          scheduleDateStart = branch.getSchedule().getWednesday().getStart();
        }
        break;
      case Calendar.FRIDAY:
        if (branch.getSchedule().getFriday().getEnd() != null) {
          scheduleDateEnd = branch.getSchedule().getFriday().getEnd();
        }
        if (branch.getSchedule().getFriday().getStart() != null) {
          scheduleDateStart = branch.getSchedule().getFriday().getStart();
        }
        break;
      case Calendar.SATURDAY:
        if (branch.getSchedule().getSaturday().getEnd() != null) {
          scheduleDateEnd = branch.getSchedule().getSaturday().getEnd();
        }
        if (branch.getSchedule().getSaturday().getStart() != null) {
          scheduleDateStart = branch.getSchedule().getSaturday().getStart();
        }
        break;
      case Calendar.SUNDAY:
        if (branch.getSchedule().getSunday().getEnd() != null) {
          scheduleDateEnd = branch.getSchedule().getSunday().getEnd();
        }
        if (branch.getSchedule().getSunday().getStart() != null) {
          scheduleDateStart = branch.getSchedule().getSunday().getStart();
        }
        break;
    }
    if (scheduleDateEnd == null || scheduleDateStart == null) {
      return context.getString(R.string.field_now_is_closed);
    }
    if (scheduleDateEnd.before(currentDate) || scheduleDateStart.after(currentDate)) {
      return context.getString(R.string.field_now_is_closed);
    }

    long secs = (scheduleDateEnd.getTime() - currentDate.getTime()) / 1000;
    long hours = secs / 3600;
    secs = secs % 3600;
    long mins = secs / 60;
    secs = secs % 60;

    if (hours + mins == 0) return context.getString(R.string.field_now_is_closed);

    return String.format(context.getString(R.string.field_close_until), hours, mins);
  }
}
