package md.fusionworks.lifehack.exchange_rates

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_exchange_rates.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.activity.NavigationDrawerActivity
import md.fusionworks.lifehack.exchange_rates.event.ScrollToMapEvent
import md.fusionworks.lifehack.exchange_rates.event.WhereToBuyEvent
import md.fusionworks.lifehack.exchange_rates.fragment.BranchListFragment
import md.fusionworks.lifehack.exchange_rates.fragment.BranchMapFragment
import md.fusionworks.lifehack.exchange_rates.fragment.ExchangeRatesFragment
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.rx.RxBus

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
    RxBus.event(WhereToBuyEvent::class.java).compose(
        bindToLifecycle<WhereToBuyEvent>()).subscribe { whereToBuyEvent ->
      addFragment(R.id.branchMapContainer,
          BranchMapFragment.newInstance(whereToBuyEvent.branchModelList))
      addFragment(R.id.branchListContainer,
          BranchListFragment.newInstance(whereToBuyEvent.branchModelList))
    }

    RxBus.event(ScrollToMapEvent::class.java).compose(
        this.bindToLifecycle<ScrollToMapEvent>()).subscribe { scrollToMapEvent ->
      container.smoothScrollTo(0, exchangeRatesContainer.bottom)
    }
  }
}
