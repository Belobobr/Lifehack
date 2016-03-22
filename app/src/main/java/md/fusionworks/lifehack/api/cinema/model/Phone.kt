package md.fusionworks.lifehack.api.cinema.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ungvas on 3/22/16.
 */
class Phone(var id: String, @SerializedName(
    "phone_number") var phoneNumber: String, var description: String) {
}