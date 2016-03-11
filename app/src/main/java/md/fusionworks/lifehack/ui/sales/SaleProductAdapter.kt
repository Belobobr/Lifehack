package md.fusionworks.lifehack.ui.sales

import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_sale_product.view.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.base.view.BaseViewHolder
import md.fusionworks.lifehack.ui.base.view.LoadMoreAdapter
import md.fusionworks.lifehack.ui.sales.model.ProductModel
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.rx.RxBusKotlin

/**
 * Created by ungvas on 12/30/15.
 */
class SaleProductAdapter(recyclerView: RecyclerView, private val language: String) : LoadMoreAdapter<ProductModel>(
    recyclerView) {

  private val VIEW_TYPE_ITEM = 0
  private val VIEW_TYPE_LOADING = 1

  override fun getItemViewType(position: Int): Int {
    return if (getItemList()[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
    if (viewType == VIEW_TYPE_ITEM) {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale_product, parent,
          false)
      return SaleProductItemViewHolder(view)
    } else if (viewType == VIEW_TYPE_LOADING) {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale_product_load_more,
          parent, false)
      return LoadingMoreItemsViewHolder(view)
    }
    return null
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder.itemViewType == VIEW_TYPE_ITEM) {
      val saleProductItemViewHolder = holder as SaleProductItemViewHolder
      saleProductItemViewHolder.bind(getItemList()[position]!!)
    }
  }

  private fun initializeThumbnailImage(thumbnailImage: ImageView, url: String) {
    val noPhotoDrawable = ContextCompat.getDrawable(thumbnailImage.context, R.drawable.ic_no)
    DrawableCompat.setTint(noPhotoDrawable,
        ContextCompat.getColor(thumbnailImage.context, R.color.text_disabled))

    Glide.with(thumbnailImage.context).load(String.format("%s_%d.jpg", url,
        Constant.SALE_PRODUCT_THUMBNAIL_IMAGE_SIZE)).crossFade().error(noPhotoDrawable).into(
        thumbnailImage)
  }

  private fun initializePrevPriceField(prevPriceField: TextView, value: Double) {
    prevPriceField.text = String.format("%d MDL", Math.round(value))
    prevPriceField.paintFlags = prevPriceField.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
  }

  inner class SaleProductItemViewHolder(itemView: View) : BaseViewHolder(itemView) {

    fun bind(productModel: ProductModel) {

      initializeThumbnailImage(itemView.thumbnailImage, productModel.productImage)
      if (productModel.categoryId == 0) {
        itemView.categoryField.visibility = View.VISIBLE
        val productName = if (language == Constant.LANG_RU) productModel.categoryNameRu
        else productModel.categoryNameRo
        itemView.categoryField.text = productName
      } else {
        itemView.categoryField.visibility = View.GONE
      }
      itemView.nameField.text = productModel.productName
      initializePrevPriceField(itemView.prevPriceField,
          productModel.productPrevPriceForGraph)
      itemView.minPriceField.text = String.format("%d MDL",
          Math.round(productModel.productMinPriceForGraph))
      itemView.salePercentField.text = "-" + Math.round(productModel.percent) + "%"


      itemView.buyButton.setOnClickListener { v ->
        RxBusKotlin.postIfHasObservers(
            NavigateToUrlEvent(getItemList()[adapterPosition]!!.storeProductUrl))
      }
      itemView.allPricesButton.setOnClickListener { v ->
        RxBusKotlin.postIfHasObservers(
            NavigateToUrlEvent(getItemList()[adapterPosition]!!.jbUrl))
      }
    }
  }

  inner class LoadingMoreItemsViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
