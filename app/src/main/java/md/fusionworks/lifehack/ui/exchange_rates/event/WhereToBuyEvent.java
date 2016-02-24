package md.fusionworks.lifehack.ui.exchange_rates.event;

import java.util.List;
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;

/**
 * Created by ungvas on 2/15/16.
 */
public class WhereToBuyEvent {

  private List<BranchModel> branchModelList;

  public WhereToBuyEvent(List<BranchModel> branchModelList) {
    this.branchModelList = branchModelList;
  }

  public List<BranchModel> getBranchModelList() {
    return branchModelList;
  }

  public void setBranchModelList(List<BranchModel> branchModelList) {
    this.branchModelList = branchModelList;
  }
}
