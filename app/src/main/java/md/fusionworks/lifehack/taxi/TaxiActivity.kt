package md.fusionworks.lifehack.taxi

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_taxi.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.taxi.persistence.TaxiDataStore
import md.fusionworks.lifehack.taxi.TaxiRepository
import md.fusionworks.lifehack.view.activity.NavigationDrawerActivity
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.rx.ObservableTransformation
import md.fusionworks.lifehack.rx.RxBus
import md.fusionworks.lifehack.extension.toVisible
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class TaxiActivity : NavigationDrawerActivity() {

  private lateinit var taxiRepository: TaxiRepository
  private lateinit var taxiPhoneNumberAdapter: TaxiPhoneNumberAdapter
  private lateinit var lastUsedTaxiPhoneNumberAdapter: TaxiPhoneNumberAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_taxi)
    taxiRepository = TaxiRepository(Realm.getDefaultInstance(),
        TaxiDataStore(Realm.getDefaultInstance()))
  }

  override fun onResume() {
    super.onResume()
    getAllPhoneNumbers()
  }

  override fun listenForEvents() {
    super.listenForEvents()
    RxBus.event(TaxiPhoneNumberClickEvent::class.java)
        .compose(bindToLifecycle<TaxiPhoneNumberClickEvent>())
        .subscribe { taxiPhoneNumberClickEvent ->
          onPhoneNumberClick(taxiPhoneNumberClickEvent.taxiPhoneNumberModel.phoneNumber)
        }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_taxi))
  }

  override fun getSelfDrawerItem() = Constant.DRAWER_ITEM_TAXI

  private fun initializeTaxiPhoneNumberList(taxiPhoneNumberModelList: List<TaxiPhoneNumberModel>) {
    taxiPhoneNumberList.layoutManager = GridLayoutManager(this, 4)
    taxiPhoneNumberAdapter = TaxiPhoneNumberAdapter(taxiPhoneNumberModelList)
    taxiPhoneNumberList.adapter = taxiPhoneNumberAdapter

    getLastUsedPhoneNumbers(Observable.just(taxiPhoneNumberModelList))
  }

  private fun initializeLastUsedTaxiPhoneNumberList(
      taxiPhoneNumberModelList: List<TaxiPhoneNumberModel>) {
    if (taxiPhoneNumberModelList.size == 0) return

    lastUsedTaxiPhoneNumberList.layoutManager = GridLayoutManager(
        this, 4)
    lastUsedTaxiPhoneNumberAdapter = TaxiPhoneNumberAdapter(taxiPhoneNumberModelList)
    lastUsedTaxiPhoneNumberList.adapter = lastUsedTaxiPhoneNumberAdapter
    lastUsedTaxiPhoneNumberList.toVisible()
  }

  private fun getAllPhoneNumbers() {
    taxiRepository.allPhoneNumbers
        .observeOn(AndroidSchedulers.mainThread())
        .compose(bindToLifecycle<List<TaxiPhoneNumberModel>>())
        .map { sortPhoneNumberListByPhoneNUmber(it) }
        .subscribe { initializeTaxiPhoneNumberList(it) }
  }

  private fun getLastUsedPhoneNumbers(observable: Observable<List<TaxiPhoneNumberModel>>) {
    observable.compose(ObservableTransformation
        .applyIOToMainThreadSchedulers<List<TaxiPhoneNumberModel>>())
        .flatMap { Observable.from(it) }
        .filter { it.lastCallDate != null }
        .toList()
        .map { sortLastUsedPhoneNumberListByDateDesc(it) }
        .take(4)
        .toList()
        .map { it[0] }
        .subscribe { initializeLastUsedTaxiPhoneNumberList(it) }
  }

  private fun sortLastUsedPhoneNumberListByDateDesc(
      taxiPhoneNumberModelList: List<TaxiPhoneNumberModel>): List<TaxiPhoneNumberModel> {
    Collections.sort(taxiPhoneNumberModelList
    ) { lhs, rhs -> rhs.lastCallDate!!.compareTo(lhs.lastCallDate) }
    return taxiPhoneNumberModelList
  }

  private fun sortPhoneNumberListByPhoneNUmber(
      taxiPhoneNumberModelList: List<TaxiPhoneNumberModel>): List<TaxiPhoneNumberModel> {
    Collections.sort(taxiPhoneNumberModelList
    ) { lhs, rhs -> lhs.phoneNumber - rhs.phoneNumber }
    return taxiPhoneNumberModelList
  }

  private fun onPhoneNumberClick(phoneNumber: Int) {
    taxiRepository.updateLastUsedDate(phoneNumber)
    navigator.navigateToCallActivity(this, phoneNumber)
  }
}
