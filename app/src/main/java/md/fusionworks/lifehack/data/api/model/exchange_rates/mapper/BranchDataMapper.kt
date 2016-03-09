package md.fusionworks.lifehack.data.api.model.exchange_rates.mapper

import java.util.ArrayList
import md.fusionworks.lifehack.data.api.model.exchange_rates.Branch
import md.fusionworks.lifehack.ui.exchange_rates.model.AddressModel
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel
import md.fusionworks.lifehack.ui.exchange_rates.model.DayModel
import md.fusionworks.lifehack.ui.exchange_rates.model.DistrictModel
import md.fusionworks.lifehack.ui.exchange_rates.model.LocalityModel
import md.fusionworks.lifehack.ui.exchange_rates.model.LocationModel
import md.fusionworks.lifehack.ui.exchange_rates.model.ScheduleModel

/**
 * Created by ungvas on 2/10/16.
 */
class BranchDataMapper {

  fun transform(branch: Branch): BranchModel {

    val localityModel = LocalityModel(branch.address!!.district!!.locality!!.id,
        branch.address!!.district!!.locality!!.name,
        branch.address!!.district!!.locality!!.code)
    val districtModel = DistrictModel(branch.address!!.district!!.id,
        branch.address!!.district!!.name, localityModel)
    val addressModel = AddressModel(
        LocationModel(branch.address!!.location!!.lat,
            branch.address!!.location!!.lng), branch.address!!.id, districtModel,
        branch.address!!.street, branch.address!!.house,
        branch.address!!.office, branch.address!!.phone,
        branch.address!!.raw)

    val dayModelMonday = DayModel(branch.schedule!!.monday!!.id,
        branch.schedule!!.monday!!.start, branch.schedule!!.monday!!.end,
        branch.schedule!!.monday!!.breakStart,
        branch.schedule!!.monday!!.breakEnd,
        branch.schedule!!.monday!!.isDayOff)
    val dayModelTuesday = DayModel(branch.schedule!!.tuesday!!.id,
        branch.schedule!!.tuesday!!.start, branch.schedule!!.tuesday!!.end,
        branch.schedule!!.tuesday!!.breakStart,
        branch.schedule!!.tuesday!!.breakEnd,
        branch.schedule!!.tuesday!!.isDayOff)
    val dayModelWednesday = DayModel(branch.schedule!!.wednesday!!.id,
        branch.schedule!!.wednesday!!.start,
        branch.schedule!!.wednesday!!.end,
        branch.schedule!!.wednesday!!.breakStart,
        branch.schedule!!.wednesday!!.breakEnd,
        branch.schedule!!.wednesday!!.isDayOff)
    val dayModelThursday = DayModel(branch.schedule!!.thursday!!.id,
        branch.schedule!!.thursday!!.start, branch.schedule!!.thursday!!.end,
        branch.schedule!!.thursday!!.breakStart,
        branch.schedule!!.thursday!!.breakEnd,
        branch.schedule!!.thursday!!.isDayOff)
    val dayModelFriday = DayModel(branch.schedule!!.friday!!.id,
        branch.schedule!!.friday!!.start, branch.schedule!!.friday!!.end,
        branch.schedule!!.friday!!.breakStart,
        branch.schedule!!.friday!!.breakEnd,
        branch.schedule!!.friday!!.isDayOff)
    val dayModelSaturday = DayModel(branch.schedule!!.saturday!!.id,
        branch.schedule!!.saturday!!.start, branch.schedule!!.saturday!!.end,
        branch.schedule!!.saturday!!.breakStart,
        branch.schedule!!.saturday!!.breakEnd,
        branch.schedule!!.saturday!!.isDayOff)
    val dayModelSunday = DayModel(branch.schedule!!.sunday!!.id,
        branch.schedule!!.sunday!!.start, branch.schedule!!.sunday!!.end,
        branch.schedule!!.sunday!!.breakStart,
        branch.schedule!!.sunday!!.breakEnd,
        branch.schedule!!.sunday!!.isDayOff)

    val scheduleModel = ScheduleModel(branch.schedule!!.id, dayModelMonday, dayModelTuesday,
        dayModelThursday, dayModelWednesday, dayModelFriday, dayModelSaturday, dayModelSunday,
        branch.schedule!!.raw)

    return BranchModel(branch.id, branch.name, addressModel, scheduleModel)
  }

  fun transform(branchList: List<Branch>) = branchList.map { transform(it) }
}
