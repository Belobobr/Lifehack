package md.fusionworks.lifehack.taxi

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_taxi.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.di.HasComponent
import md.fusionworks.lifehack.extension.toVisible
import md.fusionworks.lifehack.rx.ObservableTransformation
import md.fusionworks.lifehack.rx.RxBusDagger
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.view.activity.NavigationDrawerActivity
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class TaxiActivity : NavigationDrawerActivity(), HasComponent<TaxiComponent> {

  override lateinit var component: TaxiComponent

  @Inject lateinit var taxiRepository: TaxiRepository
  @Inject @Named("a") lateinit var taxiPhoneNumberAdapter: TaxiPhoneNumberAdapter
  @Inject @Named("b") lateinit var lastUsedTaxiPhoneNumberAdapter: TaxiPhoneNumberAdapter
  @Inject lateinit var rxBus: RxBusDagger

  override fun onCreate(savedInstanceState: Bundle?) {
    initializeDIComponent()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_taxi)
  }

  override fun onResume() {
    super.onResume()
    getAllPhoneNumbers()
  }

  override fun listenForEvents() {
    super.listenForEvents()
    rxBus.event(TaxiPhoneNumberClickEvent::class.java)
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
    taxiPhoneNumberAdapter.addItems(taxiPhoneNumberModelList)
    taxiPhoneNumberList.adapter = taxiPhoneNumberAdapter

    getLastUsedPhoneNumbers(Observable.just(taxiPhoneNumberModelList))
  }

  private fun initializeLastUsedTaxiPhoneNumberList(
      taxiPhoneNumberModelList: List<TaxiPhoneNumberModel>) {
    if (taxiPhoneNumberModelList.size == 0) return

    lastUsedTaxiPhoneNumberList.layoutManager = GridLayoutManager(
        this, 4)
    lastUsedTaxiPhoneNumberAdapter.addItems(taxiPhoneNumberModelList)
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

  override fun initializeDIComponent() {
    component = DaggerTaxiComponent
        .builder()
        .appComponent(appComponent)
        .build()
    component.inject(this)
  }
}
