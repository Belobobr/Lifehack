package md.fusionworks.lifehack.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;

/**
 * Created by ungvas on 2/15/16.
 */
public class DialogUtil {

  public static void showBranchMapInfoWindow(Context context, BranchModel branch) {
    MaterialDialog dialog = new MaterialDialog.Builder(context).title(branch.getName())
        .customView(R.layout.info_window_view, true)
        .positiveText(R.string.field_close)
        .build();

    TextView addressField = (TextView) dialog.getCustomView().findViewById(R.id.addressField);
    TextView scheduleField = (TextView) dialog.getCustomView().findViewById(R.id.scheduleField);
    TextView phoneField = (TextView) dialog.getCustomView().findViewById(R.id.phoneField);

    addressField.setText(BranchUtil.getBranchAddress(branch));

    String sheduleText = BranchUtil.getBranchMondayFridayHours(branch,
        context.getString(R.string.field_monday_friday_hours));
    String mondayFridayscheduleBreak =
        BranchUtil.getBranchMondayFridayScheduleBreak(context, branch);
    String saturdayHours =
        BranchUtil.getBranchSaturdayHours(branch, context.getString(R.string.field_saturday_hours));
    String saturdayScheduleBreak = BranchUtil.getBranchSaturdayScheduleBreak(context, branch);
    if (!BranchUtil.isBranchDetailEmpty(mondayFridayscheduleBreak)) {
      sheduleText += " " + mondayFridayscheduleBreak;
    }
    if (!BranchUtil.isBranchDetailEmpty(saturdayHours)) {
      sheduleText += "\n" + saturdayHours;
    }
    if (!BranchUtil.isBranchDetailEmpty(saturdayScheduleBreak)) {
      sheduleText += " " + saturdayScheduleBreak;
    }
    if (!BranchUtil.isBranchDetailEmpty(sheduleText)) {
      scheduleField.setVisibility(View.VISIBLE);
      scheduleField.setText(sheduleText);
    }

    String phone = branch.getAddress().getPhone();
    if (!BranchUtil.isBranchDetailEmpty(phone)) {
      phoneField.setVisibility(View.VISIBLE);
      phoneField.setText(String.format(context.getString(R.string.field_branch_phone), phone));
    }

    dialog.show();
  }
}
