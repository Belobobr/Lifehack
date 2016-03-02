package md.fusionworks.lifehack.ui.base.view;

import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ungvas on 12/30/15.
 */
public abstract class ItemListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<T> itemList;

  public ItemListAdapter() {
    itemList = new ArrayList<>();
  }

  public void addItems(List<T> itemList) {
    this.itemList.addAll(itemList);
    notifyItemRangeInserted(getItemCount() - itemList.size(), itemList.size());
  }

  public void clear() {
    this.itemList.clear();
    notifyDataSetChanged();
  }

  public void swap(List<T> itemList) {
    this.itemList.clear();
    this.itemList.addAll(itemList);
    notifyDataSetChanged();
  }

  public List<T> getItemList() {
    return itemList;
  }

  @Override public int getItemCount() {
    return itemList == null ? 0 : itemList.size();
  }
}
