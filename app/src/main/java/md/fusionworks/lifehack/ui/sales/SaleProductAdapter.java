package md.fusionworks.lifehack.ui.sales;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.BaseViewHolder;
import md.fusionworks.lifehack.ui.LoadMoreAdapter;
import md.fusionworks.lifehack.ui.sales.model.ProductModel;

/**
 * Created by ungvas on 12/30/15.
 */
public class SaleProductAdapter extends LoadMoreAdapter<ProductModel> {

  private final int VIEW_TYPE_ITEM = 0;
  private final int VIEW_TYPE_LOADING = 1;

  public SaleProductAdapter(RecyclerView recyclerView) {
    super(recyclerView);
  }

  @Override public int getItemViewType(int position) {
    return getItemList().get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == VIEW_TYPE_ITEM) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_sale_product, parent, false);
      return new SaleProductItemViewHolder(view);
    } else if (viewType == VIEW_TYPE_LOADING) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_sale_product_load_more, parent, false);
      return new LoadingMoreItemsViewHolder(view);
    }
    return null;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
    }
  }

  public class SaleProductItemViewHolder extends BaseViewHolder {

    public SaleProductItemViewHolder(@NonNull View view) {
      super(view);
    }
  }

  public class LoadingMoreItemsViewHolder extends BaseViewHolder {

    public LoadingMoreItemsViewHolder(@NonNull View view) {
      super(view);
    }
  }
}

