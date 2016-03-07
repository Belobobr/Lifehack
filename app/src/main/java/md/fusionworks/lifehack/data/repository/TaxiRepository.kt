package md.fusionworks.lifehack.data.repository

import io.realm.Realm
import md.fusionworks.lifehack.data.persistence.taxi.TaxiDataStore
import md.fusionworks.lifehack.data.persistence.taxi.TaxiPhoneNumber
import md.fusionworks.lifehack.data.persistence.taxi.TaxiPhoneNumberDataMapper
import md.fusionworks.lifehack.ui.taxi.TaxiPhoneNumberModel
import rx.Observable

/**
 * Created by ungvas on 2/18/16.
 */
class TaxiRepository(private val realm: Realm, private val taxiDataStore: TaxiDataStore) {
  private val taxiPhoneNumberDataMapper: TaxiPhoneNumberDataMapper

  init {
    taxiPhoneNumberDataMapper = TaxiPhoneNumberDataMapper()
  }

  fun hasData(): Boolean {
    val taxiPhoneNumberList = realm.where(TaxiPhoneNumber::class.java).findAll()
    return taxiPhoneNumberList.size > 0
  }

  fun add(phoneNumberList: IntArray) {
    taxiDataStore.add(phoneNumberList)
  }

  val allPhoneNumbers: Observable<List<TaxiPhoneNumberModel>>
    get() = realm.where(
        TaxiPhoneNumber::class.java)
        .findAllAsync()
        .asObservable()
        .filter { taxiPhoneNumbers -> taxiPhoneNumbers.isLoaded }
        .first()
        .map { taxiPhoneNumberDataMapper.transform(it) }

  fun updateLastUsedDate(phoneNumber: Int) {
    taxiDataStore.updateLastUsedDate(phoneNumber)
  }
}
