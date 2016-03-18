package md.fusionworks.lifehack.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import md.fusionworks.lifehack.LifehacksActivity
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.about.AboutActivity
import md.fusionworks.lifehack.exchange_rates.ExchangeRatesActivity
import md.fusionworks.lifehack.main.MainActivity
import md.fusionworks.lifehack.movies.MovieDetailActivity
import md.fusionworks.lifehack.movies.MoviesActivity
import md.fusionworks.lifehack.sales.SalesActivity
import md.fusionworks.lifehack.taxi.TaxiActivity
import md.fusionworks.lifehack.util.AndroidUtil
import md.fusionworks.lifehack.util.Constant
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

  fun navigateToMovieDetailActivity(context: Context) = context.startActivity<MovieDetailActivity>()

  fun navigateToAboutActivity(context: Context) = context.startActivity<AboutActivity>()

  fun navigateToMail(context: Context, email: String) {
    val intentToLaunch = Intent(Intent.ACTION_SENDTO,
        Uri.fromParts("mailto", email, null))
    context.startActivity(Intent.createChooser(intentToLaunch, null))
  }

  fun navigateToUrl(context: Context, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(
        url))
    val isChromeInstalled = AndroidUtil.isPackageInstalled(Constant.APP_CHROME,
        context)
    if (isChromeInstalled) {
      browserIntent.`package` = Constant.APP_CHROME
      context.startActivity(browserIntent)
    } else {
      val chooserIntent = Intent.createChooser(browserIntent,
          context.getString(R.string.choose_app))
      context.startActivity(chooserIntent)
    }
  }
}
