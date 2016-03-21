package md.fusionworks.lifehack.taxi

import io.realm.Realm
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.taxi.persistence.TaxiDataStore
import md.fusionworks.lifehack.taxi.persistence.TaxiPhoneNumber
import rx.Observable
import javax.inject.Inject

/**
 * Created by ungvas on 2/18/16.
 */
@PerActivity
class TaxiRepository
@Inject
constructor(
    private val realm: Realm, private val taxiDataStore: TaxiDataStore) {
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
