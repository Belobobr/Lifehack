package md.fusionworks.lifehack.ui.base.view

import android.support.v7.widget.RecyclerView

/**
 * Created by ungvas on 12/30/15.
 */
 abstract class ItemListAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  val items: MutableList<T?>

  init {
    items = arrayListOf<T?>()
  }

  open fun addItems(itemList: List<T>) {
    this.items!!.addAll(itemList)
    notifyItemRangeInserted(itemCount - itemList.size, itemList.size)
  }

  fun clear() {
    this.items!!.clear()
    notifyDataSetChanged()
  }

  fun swap(itemList: List<T>) {
    this.items!!.clear()
    this.items.addAll(itemList)
    notifyDataSetChanged()
  }

  fun getItemList(): List<T?> {
    return items
  }

  override fun getItemCount(): Int {
    return if (items == null) 0 else items.size
  }
}
