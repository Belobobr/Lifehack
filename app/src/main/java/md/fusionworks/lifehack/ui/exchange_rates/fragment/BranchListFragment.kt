package md.fusionworks.lifehack.ui.exchange_rates.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.badoo.mobile.util.WeakHandler
import kotlinx.android.synthetic.main.fragment_branch_list.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.base.view.BaseFragment
import md.fusionworks.lifehack.ui.exchange_rates.event.ScrollToMapEvent
import md.fusionworks.lifehack.ui.exchange_rates.event.ShowBranchMapInfoWindowEvent
import md.fusionworks.lifehack.ui.exchange_rates.event.ShowRouteOnMapEvent
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel
import md.fusionworks.lifehack.util.BranchUtil
import md.fusionworks.lifehack.util.Constant
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BranchListFragment : BaseFragment() {

  private lateinit var branchModelList: List<BranchModel>
  private var weakHandler: WeakHandler? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    branchModelList = arguments.getParcelableArrayList<BranchModel>(
        Constant.EXTRA_PARAM_BRANCH_LIST)
    weakHandler = WeakHandler()
  }

  override fun onDestroy() {
    super.onDestroy()
    weakHandler!!.removeCallbacksAndMessages(null)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflateAndBindViews(inflater, R.layout.fragment_branch_list, container)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    populateBranchesList(branchModelList)
  }

  private fun populateBranchesList(branchList: List<BranchModel>) {
    val layoutInflater = activity.layoutInflater
    branchesListLayout.removeAllViews()

    for (branch in branchList) {
      val v = layoutInflater.inflate(R.layout.branches_list_item, null, false)
      val nameField = v.findViewById(R.id.nameField) as TextView
      val addressField = v.findViewById(R.id.addressField) as TextView
      val scheduleField = v.findViewById(R.id.scheduleField) as TextView
      val workField = v.findViewById(R.id.workField) as TextView

      nameField.text = branch.name

      var address = BranchUtil.getBranchAddress(branch)
      val phone = branch.address.phone
      if (!BranchUtil.isBranchDetailEmpty(phone)) address += "\n" + phone
      addressField.text = address

      var sheduleText = BranchUtil.getBranchMondayFridayHours(branch,
          activity.getString(R.string.format_branch_monday_friday_hours_2))
      val mondayFridayscheduleBreak = BranchUtil.getBranchMondayFridayScheduleBreak(activity,
          branch)
      val saturdayHours = BranchUtil.getBranchSaturdayHours(branch,
          activity.getString(R.string.format_branch_sat_hours_2))
      val saturdayScheduleBreak = BranchUtil.getBranchSaturdayScheduleBreak(activity, branch)
      if (!BranchUtil.isBranchDetailEmpty(mondayFridayscheduleBreak)) {
        sheduleText += "\n" + mondayFridayscheduleBreak
      }
      if (!BranchUtil.isBranchDetailEmpty(saturdayHours)) {
        sheduleText += "\n" + saturdayHours
      }
      if (!BranchUtil.isBranchDetailEmpty(saturdayScheduleBreak)) {
        sheduleText += "\n" + saturdayScheduleBreak
      }
      scheduleField.text = sheduleText

      workField.text = BranchUtil.getClosingTime(activity, branch)

      nameField.setOnClickListener { v1 ->
        rxBus.postIfHasObservers(ScrollToMapEvent())
        rxBus.postIfHasObservers(ShowBranchMapInfoWindowEvent(branch))
      }
      workField.setOnClickListener { v1 ->
        rxBus.postIfHasObservers(ScrollToMapEvent())
        rxBus.postIfHasObservers(ShowRouteOnMapEvent(branch))
      }
      branchesListLayout.addView(v)
    }
  }

  companion object {
    fun newInstance(branchModelList: List<BranchModel>): BranchListFragment {
      val branchListFragment = BranchListFragment()
      val bundle = Bundle()
      bundle.putParcelableArrayList(Constant.EXTRA_PARAM_BRANCH_LIST,
          ArrayList(branchModelList))
      branchListFragment.arguments = bundle
      return branchListFragment
    }
  }
}
