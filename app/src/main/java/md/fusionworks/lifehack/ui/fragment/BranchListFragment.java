package md.fusionworks.lifehack.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.badoo.mobile.util.WeakHandler;
import java.util.List;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.event.ScrollToMapEvent;
import md.fusionworks.lifehack.ui.event.ShowBranchMapInfoWindowEvent;
import md.fusionworks.lifehack.ui.event.ShowRouteOnMapEvent;
import md.fusionworks.lifehack.ui.model.exchange_rates.BranchModel;
import md.fusionworks.lifehack.util.BranchUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class BranchListFragment extends BaseFragment {

  @Bind(R.id.branchesListLayout) LinearLayout branchesListLayout;

  private List<BranchModel> branchModelList;
  private WeakHandler weakHandler;

  public BranchListFragment() {
  }

  public static BranchListFragment newInstance(List<BranchModel> branchModelList) {
    BranchListFragment branchListFragment = new BranchListFragment();
    branchListFragment.setBranchModelList(branchModelList);
    return branchListFragment;
  }

  private void setBranchModelList(List<BranchModel> branchModelList) {
    this.branchModelList = branchModelList;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    weakHandler = new WeakHandler();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    weakHandler.removeCallbacksAndMessages(null);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflateAndBindViews(inflater, R.layout.fragment_branch_list, container);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    populateBranchesList(branchModelList);
  }

  private void populateBranchesList(List<BranchModel> branchList) {
    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
    branchesListLayout.removeAllViews();

    for (BranchModel branch : branchList) {
      View v = layoutInflater.inflate(R.layout.branches_list_item, null, false);
      TextView nameField = (TextView) v.findViewById(R.id.nameField);
      TextView addressField = (TextView) v.findViewById(R.id.addressField);
      TextView scheduleField = (TextView) v.findViewById(R.id.scheduleField);
      TextView workField = (TextView) v.findViewById(R.id.workField);

      nameField.setText(branch.getName());

      String address = BranchUtil.getBranchAddress(branch);
      String phone = branch.getAddress().getPhone();
      if (!BranchUtil.isBranchDetailEmpty(phone)) address += "\n" + phone;
      addressField.setText(address);

      String sheduleText = BranchUtil.getBranchMondayFridayHours(branch, "(Пн - Пт: %s - %s)");
      String mondayFridayscheduleBreak = BranchUtil.getBranchMondayFridayScheduleBreak(branch);
      String saturdayHours = BranchUtil.getBranchSaturdayHours(branch, "(Сб: %s - %s)");
      String saturdayScheduleBreak = BranchUtil.getBranchSaturdayScheduleBreak(branch);
      if (!BranchUtil.isBranchDetailEmpty(mondayFridayscheduleBreak)) {
        sheduleText += "\n" + mondayFridayscheduleBreak;
      }
      if (!BranchUtil.isBranchDetailEmpty(saturdayHours)) {
        sheduleText += "\n" + saturdayHours;
      }
      if (!BranchUtil.isBranchDetailEmpty(saturdayScheduleBreak)) {
        sheduleText += "\n" + saturdayScheduleBreak;
      }
      scheduleField.setText(sheduleText);

      workField.setText(BranchUtil.getClosingTime(branch));

      nameField.setOnClickListener(v1 -> {
        getRxBus().post(new ScrollToMapEvent());
        getRxBus().post(new ShowBranchMapInfoWindowEvent(branch));
      });
      workField.setOnClickListener(v1 -> {
        getRxBus().post(new ScrollToMapEvent());
        getRxBus().post(new ShowRouteOnMapEvent(branch));
      });
      branchesListLayout.addView(v);
    }
  }
}
