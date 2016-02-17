package md.fusionworks.lifehack.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import md.fusionworks.lifehack.R;

/**
 * Created by ungvas on 2/17/16.
 */
public class TaxiPhoneNumberAdapter
    extends RecyclerView.Adapter<TaxiPhoneNumberAdapter.TaxiPhoneNumberViewHolder> {

  private int[] taxiPhoneNumberList = {};

  public TaxiPhoneNumberAdapter(int[] taxiPhoneNumberList) {
    this.taxiPhoneNumberList = taxiPhoneNumberList;
  }

  @Override public TaxiPhoneNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_taxi_phone_number, parent, false);
    return new TaxiPhoneNumberViewHolder(view);
  }

  @Override public void onBindViewHolder(TaxiPhoneNumberViewHolder holder, int position) {
    holder.phoneNumberField.setText(String.valueOf(taxiPhoneNumberList[position]));
  }

  @Override public int getItemCount() {
    return taxiPhoneNumberList.length;
  }

  public static class TaxiPhoneNumberViewHolder extends BaseViewHolder {

    @Bind(R.id.phoneNumberField) TextView phoneNumberField;

    public TaxiPhoneNumberViewHolder(View itemView) {
      super(itemView);
    }
  }
}
