package md.fusionworks.lifehack.movies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.view.adapter.LoadMoreAdapter
import md.fusionworks.lifehack.sales.model.ProductModel
import md.fusionworks.lifehack.rx.RxBus

/**
 * Created by ungvas on 12/30/15.
 */
class MoviesAdapter(recyclerView: RecyclerView) : LoadMoreAdapter<ProductModel>(recyclerView) {

  private val VIEW_TYPE_ITEM = 0
  private val VIEW_TYPE_LOADING = 1

  override fun getItemViewType(position: Int): Int {
    return 0
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
    if (viewType == VIEW_TYPE_ITEM) {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
      return SaleProductItemViewHolder(view)
    } else if (viewType == VIEW_TYPE_LOADING) {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale_product_load_more,
          parent, false)
      return LoadingMoreItemsViewHolder(view)
    }
    return null
  }

  override fun getItemCount(): Int {
    return 20
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

  }

  inner class SaleProductItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    init {
      view.setOnClickListener { RxBus.postIfHasObservers(MovieClickEvent()) }
    }
  }

  inner class LoadingMoreItemsViewHolder(view: View) : RecyclerView.ViewHolder(view)
}

