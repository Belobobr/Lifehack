package md.fusionworks.lifehack.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.rx.RxBus
import org.jetbrains.anko.find

/**
 * Created by ungvas on 2/17/16.
 */
class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

  private val itemList = intArrayOf(Constant.DRAWER_ITEM_LIFE_HACKS,
      Constant.DRAWER_ITEM_EXCHANGE_RATES, Constant.DRAWER_ITEM_SALES, Constant.DRAWER_ITEM_TAXI,
      Constant.DRAWER_ITEM_MOVIES)

  private val rxBus: RxBus

  init {
    rxBus = RxBus.getInstance()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
    return MenuViewHolder(view)
  }

  override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
    when (itemList[position]) {
      Constant.DRAWER_ITEM_LIFE_HACKS -> {
        holder.titleField.text = holder.titleField.context.getString(
            R.string.drawer_item_life_hacks)
        holder.logoImage.setImageResource(R.drawable.ic_newspaper)
      }
      Constant.DRAWER_ITEM_EXCHANGE_RATES -> {
        holder.titleField.text = holder.titleField.context.getString(
            R.string.drawer_item_exchange_rates)
        holder.logoImage.setImageResource(R.drawable.ic_currency_exchange)
      }
      Constant.DRAWER_ITEM_SALES -> {
        holder.titleField.text = holder.titleField.context.getString(R.string.drawer_item_sales)
        holder.logoImage.setImageResource(R.drawable.ic_sale)
      }
      Constant.DRAWER_ITEM_TAXI -> {
        holder.titleField.text = holder.titleField.context.getString(R.string.drawer_item_taxi)
        holder.logoImage.setImageResource(R.drawable.ic_taxi)
      }
      Constant.DRAWER_ITEM_MOVIES -> {
        holder.titleField.text = holder.titleField.context.getString(R.string.drawer_item_movies)
        holder.logoImage.setImageResource(R.drawable.ic_movie)
      }
    }
  }

  override fun getItemCount(): Int {
    return itemList.size
  }

  inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var menuLayout: LinearLayout
    var titleField: TextView
    var logoImage: ImageView

    init {
      menuLayout = itemView.find(R.id.menuLayout)
      titleField = itemView.find(R.id.titleField)
      logoImage = itemView.find(R.id.logoImage)

      itemView.setOnClickListener { v ->
        rxBus.postIfHasObservers(MenuItemClickEvent(itemList[adapterPosition]))
      }
    }
  }
}
