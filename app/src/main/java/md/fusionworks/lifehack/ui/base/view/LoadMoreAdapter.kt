package md.fusionworks.lifehack.ui.base.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by ungvas on 12/30/15.
 */
abstract class LoadMoreAdapter<T>(recyclerView: RecyclerView) : ItemListAdapter<T>() {

  private val ITEM_LOAD_MORE_VIEW: T? = null

  private var onLoadMoreItemsListener: OnLoadMoreItemsListener? = null
  private var isLoading: Boolean = false
  private val visibleThreshold = 5
  private var lastVisibleItem: Int = 0
  private var totalItemCount: Int = 0

  init {
    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        totalItemCount = linearLayoutManager.itemCount
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
          if (onLoadMoreItemsListener != null) {
            recyclerView!!.post { addLoadMoreViewItem() }
            onLoadMoreItemsListener!!.onLoadMoreItems()
          }
          isLoading = true
        }
      }
    })
  }

  fun setLoaded() {
    isLoading = false
  }

  /**
   * Add special item (null) that indicate to show load more items view
   */
  private fun addLoadMoreViewItem() {
    items.add(ITEM_LOAD_MORE_VIEW)
    notifyItemInserted(itemCount - 1)
  }

  /**
   * Remove special item (null) that indicate to show load more items view
   */
  private fun removeLoadMoreViewItem() {
    if (items.size > 0) {
      items.removeAt(items.size - 1)
      notifyItemRemoved(itemCount)
    }
  }

  override fun addItems(itemList: List<T>) {
    removeLoadMoreViewItem()
    super.addItems(itemList)
    setLoaded()
  }

  fun setOnLoadMoreItemsListener(onLoadMoreItemsListener: OnLoadMoreItemsListener) {
    this.onLoadMoreItemsListener = onLoadMoreItemsListener
  }

  interface OnLoadMoreItemsListener {
    fun onLoadMoreItems()
  }
}
