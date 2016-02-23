package md.fusionworks.lifehack.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.event.MenuItemClickEvent;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.rx.RxBus;

/**
 * Created by ungvas on 2/17/16.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

  private int[] itemList = {
      Constant.DRAWER_ITEM_LIFE_HACKS, Constant.DRAWER_ITEM_EXCHANGE_RATES,
      Constant.DRAWER_ITEM_SALES, Constant.DRAWER_ITEM_TAXI
  };

  private RxBus rxBus;

  public MenuAdapter() {
    rxBus = RxBus.getInstance();
  }

  @Override public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
    return new MenuViewHolder(view);
  }

  @Override public void onBindViewHolder(MenuViewHolder holder, int position) {
    switch (itemList[position]) {
      case Constant.DRAWER_ITEM_LIFE_HACKS:
        holder.titleField.setText(
            holder.titleField.getContext().getString(R.string.drawer_item_life_hacks));
        holder.logoImage.setImageResource(R.drawable.ic_newspaper);
        break;
      case Constant.DRAWER_ITEM_EXCHANGE_RATES:
        holder.titleField.setText(
            holder.titleField.getContext().getString(R.string.drawer_item_exchange_rates));
        holder.logoImage.setImageResource(R.drawable.ic_currency_exchange);
        break;
      case Constant.DRAWER_ITEM_SALES:
        holder.titleField.setText(
            holder.titleField.getContext().getString(R.string.drawer_item_sales));
        holder.logoImage.setImageResource(R.drawable.ic_sale);
        break;
      case Constant.DRAWER_ITEM_TAXI:
        holder.titleField.setText(
            holder.titleField.getContext().getString(R.string.drawer_item_life_taxi));
        holder.logoImage.setImageResource(R.drawable.ic_taxi);
        break;
    }
  }

  @Override public int getItemCount() {
    return itemList.length;
  }

  public class MenuViewHolder extends BaseViewHolder {

    @Bind(R.id.menuLayout) LinearLayout menuLayout;
    @Bind(R.id.titleField) TextView titleField;
    @Bind(R.id.logoImage) ImageView logoImage;

    public MenuViewHolder(View itemView) {
      super(itemView);

      itemView.setOnClickListener(
          v -> rxBus.post(new MenuItemClickEvent(itemList[getAdapterPosition()])));
    }
  }
}
