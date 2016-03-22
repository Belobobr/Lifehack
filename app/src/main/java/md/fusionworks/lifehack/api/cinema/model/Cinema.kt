package md.fusionworks.lifehack.api.cinema.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ungvas on 3/22/16.
 */
class Cinema(var id: String, @SerializedName("site_id") var siteId: Int,var name: String) {
}