package md.fusionworks.lifehack.ui.exchange_rates.model

import com.google.gson.annotations.SerializedName

import java.util.Date

/**
 * Created by ungvas on 10/28/15.
 */
class RateModel(var id: Int, @SerializedName("rate_in") var rateIn: Double, @SerializedName(
    "rate_out") var rateOut: Double, var date: Date?, var currency: CurrencyModel?,
    var bank: BankModel?)
