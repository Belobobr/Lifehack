package md.fusionworks.lifehack.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import md.fusionworks.lifehack.ui.exchange_rates.ExchangeRatesActivity
import md.fusionworks.lifehack.ui.main.MainActivity
import md.fusionworks.lifehack.ui.movies.MoviesActivity
import md.fusionworks.lifehack.ui.sales.SalesActivity
import md.fusionworks.lifehack.ui.taxi.TaxiActivity
import org.jetbrains.anko.startActivity

/**
 * Created by ungvas on 10/19/15.
 */

class Navigator {

  fun navigateToMainActivity(context: Context) = context.startActivity<MainActivity>()

  fun navigateToLifeHacksActivity(context: Context) = context.startActivity<LifehacksActivity>()

  fun navigateToExchangeRatesActivity(
      context: Context) = context.startActivity<ExchangeRatesActivity>()

  fun navigateToTaxiActivity(context: Context) = context.startActivity<TaxiActivity>()

  fun navigateToSalesActivity(context: Context) = context.startActivity<SalesActivity>()

  fun navigateToMoviesActivity(context: Context) = context.startActivity<MoviesActivity>()

  fun navigateToCallActivity(context: Context, phoneNumber: Int) {
    val intentToLaunch = Intent(Intent.ACTION_DIAL)
    intentToLaunch.data = Uri.parse("tel:%d".format(phoneNumber))
    context.startActivity(intentToLaunch)
  }
}
