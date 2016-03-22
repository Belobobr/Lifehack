package md.fusionworks.lifehack.api.cinema.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by ungvas on 3/22/16.
 */
class MovieModel(var id: String, @SerializedName(
    "site_id") var siteId: String, var name: String, var description: String,
    var premere: Date, var language: String, var url: String, @SerializedName(
    "poster_url") var posterUrl: String, var showtimes: List<ShowtimeModel>)
