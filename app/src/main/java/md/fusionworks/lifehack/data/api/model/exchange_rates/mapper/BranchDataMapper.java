package md.fusionworks.lifehack.data.api.model.exchange_rates.mapper;

import java.util.ArrayList;
import java.util.List;
import md.fusionworks.lifehack.data.api.model.exchange_rates.Branch;
import md.fusionworks.lifehack.ui.exchange_rates.model.AddressModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.DayModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.DistrictModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.LocalityModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.LocationModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.ScheduleModel;

/**
 * Created by ungvas on 2/10/16.
 */
public class BranchDataMapper {

  public BranchModel transform(Branch branch) {

    LocalityModel localityModel =
        new LocalityModel(branch.getAddress().getDistrict().getLocality().getId(),
            branch.getAddress().getDistrict().getLocality().getName(),
            branch.getAddress().getDistrict().getLocality().getCode());
    DistrictModel districtModel = new DistrictModel(branch.getAddress().getDistrict().getId(),
        branch.getAddress().getDistrict().getName(), localityModel);
    AddressModel addressModel = new AddressModel(
        new LocationModel(branch.getAddress().getLocation().getLat(),
            branch.getAddress().getLocation().getLng()), branch.getAddress().getId(), districtModel,
        branch.getAddress().getStreet(), branch.getAddress().getHouse(),
        branch.getAddress().getOffice(), branch.getAddress().getPhone(),
        branch.getAddress().getRaw());

    DayModel dayModelMonday = new DayModel(branch.getSchedule().getMonday().getId(),
        branch.getSchedule().getMonday().getStart(), branch.getSchedule().getMonday().getEnd(),
        branch.getSchedule().getMonday().getBreakStart(),
        branch.getSchedule().getMonday().getBreakEnd(),
        branch.getSchedule().getMonday().isDayOff());
    DayModel dayModelTuesday = new DayModel(branch.getSchedule().getTuesday().getId(),
        branch.getSchedule().getTuesday().getStart(), branch.getSchedule().getTuesday().getEnd(),
        branch.getSchedule().getTuesday().getBreakStart(),
        branch.getSchedule().getTuesday().getBreakEnd(),
        branch.getSchedule().getTuesday().isDayOff());
    DayModel dayModelWednesday = new DayModel(branch.getSchedule().getWednesday().getId(),
        branch.getSchedule().getWednesday().getStart(),
        branch.getSchedule().getWednesday().getEnd(),
        branch.getSchedule().getWednesday().getBreakStart(),
        branch.getSchedule().getWednesday().getBreakEnd(),
        branch.getSchedule().getWednesday().isDayOff());
    DayModel dayModelThursday = new DayModel(branch.getSchedule().getThursday().getId(),
        branch.getSchedule().getThursday().getStart(), branch.getSchedule().getThursday().getEnd(),
        branch.getSchedule().getThursday().getBreakStart(),
        branch.getSchedule().getThursday().getBreakEnd(),
        branch.getSchedule().getThursday().isDayOff());
    DayModel dayModelFriday = new DayModel(branch.getSchedule().getFriday().getId(),
        branch.getSchedule().getFriday().getStart(), branch.getSchedule().getFriday().getEnd(),
        branch.getSchedule().getFriday().getBreakStart(),
        branch.getSchedule().getFriday().getBreakEnd(),
        branch.getSchedule().getFriday().isDayOff());
    DayModel dayModelSaturday = new DayModel(branch.getSchedule().getSaturday().getId(),
        branch.getSchedule().getSaturday().getStart(), branch.getSchedule().getSaturday().getEnd(),
        branch.getSchedule().getSaturday().getBreakStart(),
        branch.getSchedule().getSaturday().getBreakEnd(),
        branch.getSchedule().getSaturday().isDayOff());
    DayModel dayModelSunday = new DayModel(branch.getSchedule().getSunday().getId(),
        branch.getSchedule().getSunday().getStart(), branch.getSchedule().getSunday().getEnd(),
        branch.getSchedule().getSunday().getBreakStart(),
        branch.getSchedule().getSunday().getBreakEnd(),
        branch.getSchedule().getSunday().isDayOff());

    ScheduleModel scheduleModel =
        new ScheduleModel(branch.getSchedule().getId(), dayModelMonday, dayModelTuesday,
            dayModelThursday, dayModelWednesday, dayModelFriday, dayModelSaturday, dayModelSunday,
            branch.getSchedule().getRaw());

    return new BranchModel(branch.getId(), branch.getName(), addressModel, scheduleModel);
  }

  public List<BranchModel> transform(List<Branch> branchList) {
    List<BranchModel> branchModelList = new ArrayList<>(branchList.size());
    BranchModel branchModel;
    for (Branch branch : branchList) {
      branchModel = transform(branch);
      if (branchModel != null) {
        branchModelList.add(branchModel);
      }
    }

    return branchModelList;
  }
}