package md.fusionworks.lifehack.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_menu.view.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.rx.RxBusDagger
import md.fusionworks.lifehack.util.Constant
import javax.inject.Inject

/**
 * Created by ungvas on 2/17/16.
 */

@PerActivity
class MenuAdapter
@Inject constructor(val rxBus: RxBusDagger) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

  private val itemList = intArrayOf(Constant.DRAWER_ITEM_LIFE_HACKS,
      Constant.DRAWER_ITEM_EXCHANGE_RATES, Constant.DRAWER_ITEM_SALES, Constant.DRAWER_ITEM_TAXI,
      Constant.DRAWER_ITEM_MOVIES, Constant.DRAWER_ITEM_ABOUT)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
    return MenuViewHolder(view)
  }

  override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
    holder.bindMenu(itemList[position], rxBus);
  }

  override fun getItemCount() = itemList.size

  class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindMenu(menuItem: Int, rxBus: RxBusDagger) {
      when (menuItem) {
        Constant.DRAWER_ITEM_LIFE_HACKS -> {
          itemView.titleField.text = itemView.context.getString(
              R.string.drawer_item_life_hacks)
          itemView.logoImage.setImageResource(R.drawable.ic_newspaper)
        }
        Constant.DRAWER_ITEM_EXCHANGE_RATES -> {
          itemView.titleField.text = itemView.context.getString(
              R.string.drawer_item_exchange_rates)
          itemView.logoImage.setImageResource(R.drawable.ic_currency_exchange)
        }
        Constant.DRAWER_ITEM_SALES -> {
          itemView.titleField.text = itemView.context.getString(R.string.drawer_item_sales)
          itemView.logoImage.setImageResource(R.drawable.ic_sale)
        }
        Constant.DRAWER_ITEM_TAXI -> {
          itemView.titleField.text = itemView.context.getString(R.string.drawer_item_taxi)
          itemView.logoImage.setImageResource(R.drawable.ic_taxi)
        }
        Constant.DRAWER_ITEM_MOVIES -> {
          itemView.titleField.text = itemView.context.getString(R.string.drawer_item_movies)
          itemView.logoImage.setImageResource(R.drawable.ic_movie)
        }
        Constant.DRAWER_ITEM_ABOUT -> {
          itemView.titleField.text = itemView.context.getString(R.string.drawer_item_about)
          itemView.logoImage.setImageResource(R.drawable.ic_information)
        }
      }

      itemView.setOnClickListener { v ->
        rxBus.postIfHasObservers(MenuItemClickEvent(menuItem))
      }
    }
  }
}
