package md.fusionworks.lifehack.repository

import io.realm.Realm
import md.fusionworks.lifehack.persistence.taxi.TaxiDataStore
import md.fusionworks.lifehack.persistence.taxi.TaxiPhoneNumber
import md.fusionworks.lifehack.persistence.taxi.TaxiPhoneNumberDataMapper
import md.fusionworks.lifehack.taxi.TaxiPhoneNumberModel
import rx.Observable

/**
 * Created by ungvas on 2/18/16.
 */
class TaxiRepository(private val realm: Realm, private val taxiDataStore: TaxiDataStore) {
  private val taxiPhoneNumberDataMapper: TaxiPhoneNumberDataMapper

  init {
    taxiPhoneNumberDataMapper = TaxiPhoneNumberDataMapper()
  }

  val hasData: Boolean
    get() = realm.where(TaxiPhoneNumber::
    class.java).findAll().size > 0

  fun add(phoneNumberList: IntArray) = taxiDataStore.add(phoneNumberList)

  val allPhoneNumbers: Observable<List<TaxiPhoneNumberModel>>
    get() = realm.where(
        TaxiPhoneNumber::class.java)
        .findAllAsync()
        .asObservable()
        .filter { taxiPhoneNumbers -> taxiPhoneNumbers.isLoaded }
        .first()
        .map { taxiPhoneNumberDataMapper.transform(it) }

  fun updateLastUsedDate(phoneNumber: Int) = taxiDataStore.updateLastUsedDate(phoneNumber)
}
