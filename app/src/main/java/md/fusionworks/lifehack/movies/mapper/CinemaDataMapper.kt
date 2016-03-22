package md.fusionworks.lifehack.taxi

import md.fusionworks.lifehack.api.cinema.model.Cinema
import md.fusionworks.lifehack.api.cinema.model.CinemaModel
import md.fusionworks.lifehack.api.cinema.model.Phone
import md.fusionworks.lifehack.api.cinema.model.PhoneModel
import md.fusionworks.lifehack.di.PerActivity
import javax.inject.Inject

/**
 * Created by ungvas on 2/18/16.
 */
@PerActivity
class CinemaDataMapper @Inject constructor() {

  fun transform(cinema: Cinema) = CinemaModel(cinema.id, cinema.siteId, cinema.name,
      transformPhoneList(cinema.phones))

  fun transform(cinemaList: List<Cinema>) = cinemaList.map { transform(it) }

  private fun transformPhone(phone: Phone) = PhoneModel(phone.id, phone.phoneNumber,
      phone.description)

  private fun transformPhoneList(phoneList: List<Phone>) = phoneList.map { transformPhone(it) }
}
