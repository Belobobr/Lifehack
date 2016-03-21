package md.fusionworks.lifehack.taxi.persistence

import io.realm.Realm
import md.fusionworks.lifehack.di.PerActivity
import java.util.*
import javax.inject.Inject

/**
 * Created by ungvas on 2/18/16.
 */
@PerActivity
class TaxiDataStore
@Inject constructor(private val realm: Realm) {

  fun add(phoneNumberList: IntArray) = realm.executeTransaction { realm ->
    for (i in phoneNumberList.indices) {
      val taxiPhoneNumber = realm.createObject(TaxiPhoneNumber::class.java)
      taxiPhoneNumber.phoneNumber = phoneNumberList[i]
      taxiPhoneNumber.lastCallDate = null
    }
  }

  fun updateLastUsedDate(phoneNumber: Int) = realm.executeTransaction { realm ->
    val taxiPhoneNumber = realm.where(TaxiPhoneNumber::class.java).equalTo("phoneNumber",
        phoneNumber).findFirst()
    if (taxiPhoneNumber.isLoaded) taxiPhoneNumber.lastCallDate = Date()
  }
}
