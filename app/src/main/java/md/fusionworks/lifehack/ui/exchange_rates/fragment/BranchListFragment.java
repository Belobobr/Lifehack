package md.fusionworks.lifehack.ui.exchange_rates.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.badoo.mobile.util.WeakHandler;
import java.util.ArrayList;
import java.util.List;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.exchange_rates.event.ScrollToMapEvent;
import md.fusionworks.lifehack.ui.exchange_rates.event.ShowBranchMapInfoWindowEvent;
import md.fusionworks.lifehack.ui.exchange_rates.event.ShowRouteOnMapEvent;
import md.fusionworks.lifehack.ui.base.view.BaseFragment;
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;
import md.fusionworks.lifehack.util.BranchUtil;
import md.fusionworks.lifehack.util.Constant;

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
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(Constant.EXTRA_PARAM_BRANCH_LIST,
        new ArrayList<>(branchModelList));
    branchListFragment.setArguments(bundle);
    return branchListFragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    branchModelList = getArguments().getParcelableArrayList(Constant.EXTRA_PARAM_BRANCH_LIST);
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

      String sheduleText = BranchUtil.getBranchMondayFridayHours(branch,
          getActivity().getString(R.string.format_branch_monday_friday_hours_2));
      String mondayFridayscheduleBreak =
          BranchUtil.getBranchMondayFridayScheduleBreak(getActivity(), branch);
      String saturdayHours = BranchUtil.getBranchSaturdayHours(branch,
          getActivity().getString(R.string.format_branch_sat_hours_2));
      String saturdayScheduleBreak =
          BranchUtil.getBranchSaturdayScheduleBreak(getActivity(), branch);
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

      workField.setText(BranchUtil.getClosingTime(getActivity(), branch));

      nameField.setOnClickListener(v1 -> {
        getRxBus().postIfHasObservers(new ScrollToMapEvent());
        getRxBus().postIfHasObservers(new ShowBranchMapInfoWindowEvent(branch));
      });
      workField.setOnClickListener(v1 -> {
        getRxBus().postIfHasObservers(new ScrollToMapEvent());
        getRxBus().postIfHasObservers(new ShowRouteOnMapEvent(branch));
      });
      branchesListLayout.addView(v);
    }
  }
}
