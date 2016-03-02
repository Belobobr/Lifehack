package md.fusionworks.lifehack.ui.taxi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import java.util.List;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.base.view.BaseViewHolder;
import md.fusionworks.lifehack.util.rx.RxBus;

/**
 * Created by ungvas on 2/17/16.
 */
public class TaxiPhoneNumberAdapter
    extends RecyclerView.Adapter<TaxiPhoneNumberAdapter.TaxiPhoneNumberViewHolder> {

  private List<TaxiPhoneNumberModel> taxiPhoneNumberList;
  private RxBus rxBus;

  public TaxiPhoneNumberAdapter(List<TaxiPhoneNumberModel> taxiPhoneNumberList) {
    this.taxiPhoneNumberList = taxiPhoneNumberList;
    rxBus = RxBus.getInstance();
  }

  @Override public TaxiPhoneNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_taxi_phone_number, parent, false);
    return new TaxiPhoneNumberViewHolder(view);
  }

  @Override public void onBindViewHolder(TaxiPhoneNumberViewHolder holder, int position) {
    holder.phoneNumberField.setText(
        String.valueOf(taxiPhoneNumberList.get(position).getPhoneNumber()));
  }

  @Override public int getItemCount() {
    return taxiPhoneNumberList.size();
  }

  public class TaxiPhoneNumberViewHolder extends BaseViewHolder {

    @Bind(R.id.phoneNumberField) TextView phoneNumberField;

    public TaxiPhoneNumberViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(v -> rxBus.postIfHasObservers(
          new TaxiPhoneNumberClickEvent(taxiPhoneNumberList.get(getAdapterPosition()))));
    }
  }
}
