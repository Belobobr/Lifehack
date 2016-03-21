package md.fusionworks.lifehack.main

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.navigator.Navigator
import md.fusionworks.lifehack.rx.RxBusDagger
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.view.activity.NavigationDrawerActivity
import javax.inject.Inject

class MainActivity : NavigationDrawerActivity() {

  @Inject lateinit var rxBus: RxBusDagger
  @Inject lateinit var menuAdapter: MenuAdapter
  @Inject lateinit var appNavigator: Navigator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
    setContentView(R.layout.activity_main)

    initializeMenuList()
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_main))
  }

  override fun getSelfDrawerItem() = Constant.DRAWER_ITEM_MAIN

  override fun listenForEvents() {
    super.listenForEvents()
    rxBus.event(MenuItemClickEvent::class.java).compose(
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
        appNavigator.navigateToExchangeRatesActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_TAXI -> {
        appNavigator.navigateToTaxiActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_LIFE_HACKS -> {
        appNavigator.navigateToLifeHacksActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_SALES -> {
        appNavigator.navigateToSalesActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_ABOUT -> {
        appNavigator.navigateToAboutActivity(this)
        finish()
      }
      Constant.DRAWER_ITEM_MOVIES -> {
        appNavigator.navigateToMoviesActivity(this)
        finish()
      }
    }
  }
}
