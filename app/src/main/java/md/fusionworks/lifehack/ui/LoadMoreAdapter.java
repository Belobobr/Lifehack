package md.fusionworks.lifehack.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.List;

/**
 * Created by ungvas on 12/30/15.
 */
public abstract class LoadMoreAdapter<T> extends ItemListAdapter<T> {

  private final T ITEM_LOAD_MORE_VIEW = null;

  private OnLoadMoreItemsListener onLoadMoreItemsListener;
  private boolean isLoading;
  private int visibleThreshold = 5;
  private int lastVisibleItem, totalItemCount;

  public LoadMoreAdapter(RecyclerView recyclerView) {
    super();
    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = linearLayoutManager.getItemCount();
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
          if (onLoadMoreItemsListener != null) {
            recyclerView.post(() -> addLoadMoreViewItem());
            onLoadMoreItemsListener.onLoadMoreItems();
          }
          isLoading = true;
        }
      }
    });
  }

  public void setLoaded() {
    isLoading = false;
  }

  /**
   * Add special item (null) that indicate to show load more items view
   */
  private void addLoadMoreViewItem() {
    getItemList().add(ITEM_LOAD_MORE_VIEW);
    notifyItemInserted(getItemCount() - 1);
  }

  /**
   * Remove special item (null) that indicate to show load more items view
   */
  private void removeLoadMoreViewItem() {
    if (getItemList().size() > 0) {
      getItemList().remove(getItemList().size() - 1);
      notifyItemRemoved(getItemCount());
    }
  }

  @Override public void addItems(List<T> itemList) {
    removeLoadMoreViewItem();
    super.addItems(itemList);
    setLoaded();
  }

  public void setOnLoadMoreItemsListener(OnLoadMoreItemsListener onLoadMoreItemsListener) {
    this.onLoadMoreItemsListener = onLoadMoreItemsListener;
  }
}
