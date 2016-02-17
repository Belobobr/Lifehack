package md.fusionworks.lifehack.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.adapter.TaxiPhoneNumberAdapter;
import md.fusionworks.lifehack.util.Constant;

public class TaxiActivity extends NavigationDrawerActivity {

  private static final int TAXI_PHONE_NUMBER_LIST[] = {
      14002, 14022, 14111, 14428, 14004, 14040, 14120, 14433, 14006, 14090, 14222, 14442, 14007,
      14098, 14333, 14448, 14008, 14099, 14400, 14474, 14009, 14100, 14415, 14477, 14499, 14555,
      14747, 14911, 14545, 14700, 14777, 14999, 14101, 14000, 14066, 14110, 14112, 14250, 14441,
      14444, 14005, 14554, 14800, 14001
  };

 // @Bind(R.id.taxiPhoneNumberList) RecyclerView taxiPhoneNumberList;

  private TaxiPhoneNumberAdapter taxiPhoneNumberAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_taxi);
    initializeTaxiPhoneNumberList();
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle("Такси");
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_TAXI;
  }

  private void initializeTaxiPhoneNumberList() {
  //  taxiPhoneNumberList.setLayoutManager(new GridLayoutManager(this, 4));
 //   taxiPhoneNumberAdapter = new TaxiPhoneNumberAdapter(TAXI_PHONE_NUMBER_LIST);
  //  taxiPhoneNumberList.setAdapter(taxiPhoneNumberAdapter);
  }
}
