package md.fusionworks.lifehack.ui.main

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.NavigationDrawerActivity
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.rx.RxBus

class MainActivity : NavigationDrawerActivity() {

  private lateinit var menuAdapter: MenuAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    menuAdapter = MenuAdapter()
    initializeMenuList()
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_main))
  }

  override fun getSelfDrawerItem() = Constant.DRAWER_ITEM_MAIN

  override fun listenForEvents() {
    super.listenForEvents()
    RxBus.event(MenuItemClickEvent::class.java).compose(
        bindToLifecycle<MenuItemClickEvent>()).subscribe { menuItemClickEvent ->
      onMenuItemClickEvent(menuItemClickEvent.itemId)
    }
  }

  private fun initializeMenuList() {
    menuList.layoutManager = GridLayoutManager(this, 2)
    menuList.addItemDecoration(ItemOffsetDecoration(this, R.dimen.menu_item_offset_size))
    menuList.adapter = menuAdapter
  }

  private fun onMenuItemClickEvent(itemId: Int) {
    when (itemId) {
      Constant.DRAWER_ITEM_EXCHANGE_RATES -> {
        navigator.navigateToExchangeRatesActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_TAXI -> {
        navigator.navigateToTaxiActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_LIFE_HACKS -> {
        navigator.navigateToLifeHacksActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_SALES -> {
        navigator.navigateToSalesActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_ABOUT -> {
        navigator.navigateToAboutActivity(this)
        finish()
      }
    }
  }
}
