package md.fusionworks.lifehack.data.api.banks.model.exchange_rates

import com.google.gson.annotations.SerializedName

import java.util.Date

/**
 * Created by ungvas on 11/6/15.
 */
class Day(var id: Int, var start: Date?, var end: Date?, @SerializedName(
    "day_off") var isDayOff: Boolean) {
  @SerializedName("break_start") var breakStart: Date? = null
  @SerializedName("break_end") var breakEnd: Date? = null
}
