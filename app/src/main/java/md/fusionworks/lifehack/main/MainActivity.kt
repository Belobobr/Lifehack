package md.fusionworks.lifehack.main

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.di.HasComponent
import md.fusionworks.lifehack.rx.RxBusDagger
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.view.activity.NavigationDrawerActivity
import javax.inject.Inject

class MainActivity : NavigationDrawerActivity(), HasComponent<MainComponent> {

  override lateinit var component: MainComponent

  @Inject lateinit var rxBus: RxBusDagger
  @Inject lateinit var menuAdapter: MenuAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    initializeDIComponent()
    super.onCreate(savedInstanceState)
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

  override fun initializeDIComponent() {
    component = DaggerMainComponent
        .builder()
        .appComponent(appComponent)
        .mainModule(MainModule(this))
        .build()
    component.inject(this)
  }
}
