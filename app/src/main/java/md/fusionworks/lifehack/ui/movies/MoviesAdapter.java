package md.fusionworks.lifehack.ui.movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.base.view.BaseViewHolder;
import md.fusionworks.lifehack.ui.base.view.LoadMoreAdapter;
import md.fusionworks.lifehack.ui.sales.model.ProductModel;

/**
 * Created by ungvas on 12/30/15.
 */
public class MoviesAdapter extends LoadMoreAdapter<ProductModel> {

  private final int VIEW_TYPE_ITEM = 0;
  private final int VIEW_TYPE_LOADING = 1;

  public MoviesAdapter(RecyclerView recyclerView) {
    super(recyclerView);
  }

  @Override public int getItemViewType(int position) {
    return 0;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_ITEM) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
      return new SaleProductItemViewHolder(view);
    } else if (viewType == VIEW_TYPE_LOADING) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_sale_product_load_more, parent, false);
      return new LoadingMoreItemsViewHolder(view);
    }
    return null;
  }

  @Override public int getItemCount() {
    return 20;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

  }

  public class SaleProductItemViewHolder extends BaseViewHolder {

    public SaleProductItemViewHolder(@NonNull View view) {
      super(view);
    }
  }

  public class LoadingMoreItemsViewHolder extends RecyclerView.ViewHolder {

    public LoadingMoreItemsViewHolder(@NonNull View view) {
      super(view);
    }
  }
}

