package md.fusionworks.lifehack.data.api.model.exchange_rates

import com.google.gson.annotations.SerializedName

import java.util.Date

/**
 * Created by ungvas on 10/28/15.
 */
class Rate {

  var id: Int = 0
  @SerializedName("rate_in") var rateIn: Double = 0.toDouble()
  @SerializedName("rate_out") var rateOut: Double = 0.toDouble()
  var date: Date? = null
  var currency: Currency? = null
  var bank: Bank? = null
}
