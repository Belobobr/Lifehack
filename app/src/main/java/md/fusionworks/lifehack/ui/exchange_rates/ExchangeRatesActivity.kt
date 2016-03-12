package md.fusionworks.lifehack.ui.exchange_rates

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_exchange_rates.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.NavigationDrawerActivity
import md.fusionworks.lifehack.ui.exchange_rates.event.ScrollToMapEvent
import md.fusionworks.lifehack.ui.exchange_rates.event.WhereToBuyEvent
import md.fusionworks.lifehack.ui.exchange_rates.fragment.BranchListFragment
import md.fusionworks.lifehack.ui.exchange_rates.fragment.BranchMapFragmentKotlin
import md.fusionworks.lifehack.ui.exchange_rates.fragment.ExchangeRatesFragment
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.rx.RxBusKotlin

class ExchangeRatesActivity : NavigationDrawerActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_exchange_rates)

    addFragment(R.id.exchangeRatesContainer, ExchangeRatesFragment.newInstance())
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_exchange_rates))
  }

  override fun getSelfDrawerItem() = Constant.DRAWER_ITEM_EXCHANGE_RATES

  override fun listenForEvents() {
    super.listenForEvents()
    rxBus.event(WhereToBuyEvent::class.java).compose(
        this.bindToLifecycle<WhereToBuyEvent>()).subscribe { whereToBuyEvent ->
      addFragment(R.id.branchMapContainer,
          BranchMapFragmentKotlin.newInstance(whereToBuyEvent.branchModelList))
      addFragment(R.id.branchListContainer,
          BranchListFragment.newInstance(whereToBuyEvent.branchModelList))
    }

    RxBusKotlin.event(ScrollToMapEvent::class.java).compose(
        this.bindToLifecycle<ScrollToMapEvent>()).subscribe { scrollToMapEvent ->
      container.smoothScrollTo(0, exchangeRatesContainer.bottom)
    }
  }
}
