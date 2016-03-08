package md.fusionworks.lifehack.ui.taxi

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_taxi.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.data.persistence.taxi.TaxiDataStore
import md.fusionworks.lifehack.data.repository.TaxiRepository
import md.fusionworks.lifehack.ui.NavigationDrawerActivity
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.rx.ObservableTransformation
import org.jetbrains.anko.find
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import java.util.*

class TaxiActivity : NavigationDrawerActivity() {

  private var taxiRepository: TaxiRepository? = null
  private var taxiPhoneNumberAdapter: TaxiPhoneNumberAdapter? = null
  private var lastUsedTaxiPhoneNumberAdapter: TaxiPhoneNumberAdapter? = null

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
    rxBus.event(TaxiPhoneNumberClickEvent::class.java).compose(
        bindToLifecycle<TaxiPhoneNumberClickEvent>()).subscribe { taxiPhoneNumberClickEvent ->
      onPhoneNumberClick(taxiPhoneNumberClickEvent.taxiPhoneNumberModel!!.phoneNumber)
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_taxi))
  }

  override fun getSelfDrawerItem(): Int {
    return Constant.DRAWER_ITEM_TAXI
  }

  private fun initializeTaxiPhoneNumberList(taxiPhoneNumberModelList: List<TaxiPhoneNumberModel>) {
    taxiPhoneNumberList.layoutManager = GridLayoutManager(this, 4)
    taxiPhoneNumberAdapter = TaxiPhoneNumberAdapter(taxiPhoneNumberModelList)
    taxiPhoneNumberList.adapter = taxiPhoneNumberAdapter

    getLastUsedPhoneNumbers(Observable.just(taxiPhoneNumberModelList))
  }

  private fun initializeLastUsedTaxiPhoneNumberList(
      taxiPhoneNumberModelList: List<TaxiPhoneNumberModel>) {
    lastUsedTaxiPhoneNumberList.layoutManager = GridLayoutManager(this, 4)
    lastUsedTaxiPhoneNumberAdapter = TaxiPhoneNumberAdapter(taxiPhoneNumberModelList)
    lastUsedTaxiPhoneNumberList.adapter = lastUsedTaxiPhoneNumberAdapter
  }

  private fun getAllPhoneNumbers() {
    taxiRepository!!.allPhoneNumbers
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
    taxiRepository!!.updateLastUsedDate(phoneNumber)
    navigator.navigateToCallActivity(this, phoneNumber)
  }
}
