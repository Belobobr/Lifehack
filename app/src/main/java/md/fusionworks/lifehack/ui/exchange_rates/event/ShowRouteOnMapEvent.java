package md.fusionworks.lifehack.ui.exchange_rates.event;

import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;

/**
 * Created by ungvas on 2/15/16.
 */
public class ShowRouteOnMapEvent {

  private BranchModel branchModel;

  public ShowRouteOnMapEvent(BranchModel branchModel) {
    this.branchModel = branchModel;
  }

  public BranchModel getBranchModel() {
    return branchModel;
  }

  public void setBranchModel(BranchModel branchModel) {
    this.branchModel = branchModel;
  }
}
