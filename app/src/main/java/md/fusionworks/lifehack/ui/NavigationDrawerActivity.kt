package md.fusionworks.lifehack.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.base.view.BaseActivity
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.LocaleHelper
import md.fusionworks.lifehack.util.setAccessibilityIgnore
import org.jetbrains.anko.find
import java.util.*

/**
 * Created by admin on 03.09.2015.
 */
open class NavigationDrawerActivity : BaseActivity() {

  private val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
  private val drawerLayout by lazy { find<DrawerLayout>(R.id.drawerLayout) }
  private val drawerItemsListContainer by lazy { find<ViewGroup>(R.id.drawerItemsContainer) }
  private val languageButton by lazy { find<AppCompatButton>(R.id.languageButton) }

  private val drawerItems = ArrayList<Int>()
  private lateinit var drawerItemViews: MutableList<View>
  private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
  private var handler: Handler? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    LocaleHelper.onCreate(this)
    super.onCreate(savedInstanceState)
    handler = Handler()
  }

  public override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    setupDrawerLayout()

    languageButton.setOnClickListener {
      val currentLanguage = LocaleHelper.getLanguage(this)
      val toggleLanguage = if (currentLanguage == Constant.LANG_RU) Constant.LANG_RO else Constant.LANG_RU
      LocaleHelper.setLocale(this, toggleLanguage)

      drawerLayout!!.closeDrawer(GravityCompat.START)
      handler!!.postDelayed({ recreate() }, DRAWER_LAUNCH_DELAY.toLong())
    }
  }

  private fun setupDrawerLayout() {
    if (drawerLayout == null) {
      return
    }

    actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name,
        R.string.app_name)
    actionBarDrawerToggle!!.isDrawerIndicatorEnabled = true

    drawerLayout!!.setDrawerListener(actionBarDrawerToggle)
    toolbar.setNavigationOnClickListener { view -> drawerLayout!!.openDrawer(GravityCompat.START) }
    actionBarDrawerToggle!!.syncState()

    populateDrawerItems()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    actionBarDrawerToggle!!.onConfigurationChanged(newConfig)
  }

  fun setTitle(title: String) {
    supportActionBar!!.title = title
  }

  private fun populateDrawerItems() {
    drawerItems.add(Constant.DRAWER_ITEM_MAIN)
    drawerItems.add(Constant.DRAWER_ITEM_LIFE_HACKS)
    drawerItems.add(Constant.DRAWER_ITEM_EXCHANGE_RATES)
    drawerItems.add(Constant.DRAWER_ITEM_SALES)
    drawerItems.add(Constant.DRAWER_ITEM_TAXI)
    drawerItems.add(Constant.DRAWER_ITEM_MOVIES)
    drawerItems.add(Constant.DRAWER_ITEM_PRICES)
    drawerItems.add(Constant.DRAWER_ITEM_POSTER)
    // drawerItems.add(Constant.DRAWER_ITEM_SETTINGS);

    createDrawerItems()
  }

  private fun createDrawerItems() {

    drawerItemsListContainer.let {
      drawerItemViews = arrayListOf<View>()
      drawerItemsListContainer!!.removeAllViews()
      var i = 0
      for (itemId in drawerItems) {
        drawerItemViews.add(makeDrawerItem(itemId, drawerItemsListContainer))
        drawerItemsListContainer!!.addView(drawerItemViews!![i])
        ++i
      }
    }
  }

  private fun makeDrawerItem(itemId: Int, container: ViewGroup): View {
    val selected = getSelfDrawerItem() == itemId
    var layoutToInflate = 0
    if (itemId == DRAWER_ITEM_SEPARATOR) {
      layoutToInflate = R.layout.navdrawer_separator
    } else {
      layoutToInflate = R.layout.navdrawer_item
    }
    val view = layoutInflater.inflate(layoutToInflate, container, false)

    if (isSeparator(itemId)) {

      view.setAccessibilityIgnore()
      return view
    }

    val iconView = view.findViewById(R.id.icon) as ImageView
    val titleView = view.findViewById(R.id.title) as TextView
    val iconId = if (itemId >= 0 && itemId < DRAWER_ICON_RES_ID.size) DRAWER_ICON_RES_ID[itemId] else 0
    val titleId = if (itemId >= 0 && itemId < DRAWER_TITLE_RES_ID.size) DRAWER_TITLE_RES_ID[itemId] else 0
    val isActive = if (itemId >= 0 && itemId < DRAWER_ITEM_ACTIVE_STATE.size) DRAWER_ITEM_ACTIVE_STATE[itemId]
    else false

    iconView.visibility = if (iconId > 0) View.VISIBLE else View.GONE
    if (iconId > 0) {
      iconView.setImageResource(iconId)
    }
    titleView.text = getString(titleId)

    formatDrawerItem(view, itemId, selected, isActive)

    if (isActive) view.setOnClickListener { v -> onDrawerItemClicked(itemId) }

    return view
  }

  open fun getSelfDrawerItem() = DRAWER_ITEM_INVALID

  private fun isSeparator(itemId: Int): Boolean {
    return itemId == DRAWER_ITEM_SEPARATOR
  }

  private fun isSimpleActivity(itemId: Int): Boolean {
    return itemId == DRAWER_ITEM_INVALID
  }

  private fun formatDrawerItem(view: View, itemId: Int, selected: Boolean, isActive: Boolean) {
    if (isSeparator(itemId)) {
      // not applicable
      return
    }

    val iconView = view.findViewById(R.id.icon) as ImageView
    val titleView = view.findViewById(R.id.title) as TextView

    if (selected) {
      view.setBackgroundResource(R.drawable.selected_navdrawer_item_background)
    }

    val titleColorRes: Int
    val iconColorRes: Int

    if (isActive) {

      titleColorRes = if (selected) R.color.navdrawer_text_color_selected else R.color.navdrawer_text_color
      iconColorRes = if (selected) R.color.navdrawer_icon_tint_selected else R.color.navdrawer_icon_tint
    } else {

      titleColorRes = R.color.navdrawer_text_color_inactive
      iconColorRes = R.color.navdrawer_icon_tint_inactive
    }

    titleView.setTextColor(resources.getColor(titleColorRes))
    iconView.setColorFilter(resources.getColor(iconColorRes))
  }

  private fun onDrawerItemClicked(itemId: Int) {
    if (itemId == getSelfDrawerItem()) {
      drawerLayout!!.closeDrawer(GravityCompat.START)
      return
    }

    if (isSimpleActivity(itemId)) {
      goToDrawerItem(itemId)
    } else {

      handler!!.postDelayed({ goToDrawerItem(itemId) }, DRAWER_LAUNCH_DELAY.toLong())

      setSelectedDrawerItem(itemId)
    }

    drawerLayout!!.closeDrawer(GravityCompat.START)
  }

  private fun setSelectedDrawerItem(itemId: Int) {
    if (drawerItemViews != null) {
      for (i in drawerItemViews!!.indices) {
        if (i < drawerItems.size) {

          val thisItemId = drawerItems[i]
          val isActive = if (itemId >= 0 && itemId < DRAWER_ITEM_ACTIVE_STATE.size) DRAWER_ITEM_ACTIVE_STATE[itemId]
          else false
          formatDrawerItem(drawerItemViews!![i], thisItemId, itemId == thisItemId, isActive)
        }
      }
    }
  }

  fun goToDrawerItem(item: Int) {
    when (item) {
      Constant.DRAWER_ITEM_MAIN -> {
        navigator.navigateToMainActivity(this)
        finish()
      }
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
      Constant.DRAWER_ITEM_MOVIES -> {
        navigator.navigateToMoviesActivity(this)
        finish()
      }
    }
  }

  companion object {

    protected val DRAWER_ITEM_INVALID = -1
    protected val DRAWER_ITEM_SEPARATOR = -2

    private val DRAWER_LAUNCH_DELAY = 250

    private val DRAWER_TITLE_RES_ID = intArrayOf(R.string.drawer_item_main,
        R.string.drawer_item_life_hacks, R.string.drawer_item_exchange_rates,
        R.string.drawer_item_movies, R.string.drawer_item_taxi, R.string.drawer_item_prices,
        R.string.drawer_item_sales, R.string.drawer_item_poster)

    private val DRAWER_ICON_RES_ID = intArrayOf(R.drawable.ic_home, R.drawable.ic_newspaper,
        R.drawable.ic_currency_exchange, R.drawable.ic_movie, R.drawable.ic_taxi,
        R.drawable.ic_price_tag, R.drawable.ic_sale,
        R.drawable.ic_advertising)

    private val DRAWER_ITEM_ACTIVE_STATE = booleanArrayOf(true, true, true, true, true, false, true,
        false)
  }
}
