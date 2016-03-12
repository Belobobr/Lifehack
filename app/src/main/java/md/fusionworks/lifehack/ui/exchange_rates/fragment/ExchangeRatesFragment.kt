package md.fusionworks.lifehack.ui.exchange_rates.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badoo.mobile.util.WeakHandler
import com.jakewharton.rxbinding.widget.RxAdapterView
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.fragment_exchange_rates.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.data.repository.ExchangeRatesRepository
import md.fusionworks.lifehack.ui.base.view.BaseFragment
import md.fusionworks.lifehack.ui.exchange_rates.BankSpinnerAdapter
import md.fusionworks.lifehack.ui.exchange_rates.event.WhereToBuyEvent
import md.fusionworks.lifehack.ui.exchange_rates.model.*
import md.fusionworks.lifehack.ui.widget.DateView
import md.fusionworks.lifehack.ui.widget.RetryView
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.Converter
import md.fusionworks.lifehack.util.DateUtil
import md.fusionworks.lifehack.util.ExchangeRatesUtil
import md.fusionworks.lifehack.util.rx.ObservableTransformation
import md.fusionworks.lifehack.util.rx.ObserverAdapter
import md.fusionworks.lifehack.util.rx.RxBusKotlin
import rx.Observable
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ExchangeRatesFragment : BaseFragment() {

  private lateinit var rateList: List<RateModel>
  private lateinit var bankList: List<BankModel>
  private lateinit var currencyList: List<CurrencyModel>

  private lateinit var exchangeRatesRepository: ExchangeRatesRepository
  private lateinit var bankSpinnerAdapter: BankSpinnerAdapter
  private lateinit var weakHandler: WeakHandler

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    exchangeRatesRepository = ExchangeRatesRepository()
    bankSpinnerAdapter = BankSpinnerAdapter(activity)
    weakHandler = WeakHandler()
  }

  override fun onDestroy() {
    weakHandler.removeCallbacksAndMessages(null)
    super.onDestroy()
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflateAndBindViews(inflater, R.layout.fragment_exchange_rates, container)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loadInitialData()
  }

  private fun loadInitialData() {
    showLoadingDialog()
    val bankObservable = exchangeRatesRepository.banks
    val currencyObservable = exchangeRatesRepository.currencies
    val today = DateUtil.getRateDateFormat().format(Date())
    val rateObservable = exchangeRatesRepository.getRates(today)

    loadingDialogCancelSubscription.add(
        Observable.zip(bankObservable, currencyObservable, rateObservable
        ) { bankModels, currencyModels, rateModels ->
          InitialDataModel(bankModels,
              currencyModels, rateModels)
        }.compose(
            ObservableTransformation.applyIOToMainThreadSchedulers<InitialDataModel>()).compose(
            this.bindToLifecycle<InitialDataModel>()).subscribe(
            object : ObserverAdapter<InitialDataModel>() {
              override fun onNext(initialDataModel: InitialDataModel) {
                bankList = initialDataModel.bankModelList
                currencyList = initialDataModel.currencyModelList
                rateList = initialDataModel.rateModelList
                onInitialDataLoadedSuccess()
              }

              override fun onError(e: Throwable) {
                onInitialDataLoadedError()
              }
            }))
  }

  private fun loadRates(date: String) {
    showLoadingDialog()
    loadingDialogCancelSubscription.add(exchangeRatesRepository.getRates(date).compose(
        ObservableTransformation.applyIOToMainThreadSchedulers<List<RateModel>>()).compose(
        this.bindToLifecycle<List<RateModel>>()).subscribe(
        object : ObserverAdapter<List<RateModel>>() {
          override fun onNext(rateModels: List<RateModel>) {
            rateList = rateModels
            hideLoadingDialog()
            applyConversion()
          }

          override fun onError(e: Throwable) {
            hideLoadingDialog()
            showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                getString(R.string.error_something_gone_wrong))
          }
        }))
  }

  private fun onInitialDataLoadedSuccess() {
    hideLoadingDialog()
    hideRetryView()
    showExchangeRatesView()
    populateCurrencyInGroup(currencyList)
    populateCurrencyOutGroup(currencyList)
    populateBankSpinner(bankList)
    setupUIWithDefaultValues()
    initializeViewListeners()
  }

  private fun onInitialDataLoadedError() {
    hideLoadingDialog()
    hideExchangeRatesView()
    showRetryView()
  }

  private fun showExchangeRatesView() {
    exchangeRatesView.visibility = View.VISIBLE
  }

  private fun hideExchangeRatesView() {
    exchangeRatesView.visibility = View.GONE
  }

  private fun showRetryView() {
    retryView.show()
    retryView.setOnRetryActionListener(object : RetryView.OnRetryActionListener {
      override fun onRetryAction() {
        retryView.hide()
        loadInitialData()
      }
    })
  }

  private fun hideRetryView() = retryView.hide()

  private fun populateCurrencyInGroup(
      currencyList: List<CurrencyModel>) = currenciesInGroup.addCurrencies(currencyList)

  private fun populateCurrencyOutGroup(
      currencyList: List<CurrencyModel>) = currenciesOutGroup.addCurrencies(currencyList)

  private fun populateBankSpinner(bankList: List<BankModel>) {
    bankSpinnerAdapter.clear()
    bankSpinnerAdapter.addItem(getString(R.string.best_exchange), 0)
    bankSpinnerAdapter.addHeader(getString(R.string.bank_list))
    for (bank in bankList) {
      bankSpinnerAdapter.addItem(bank.name, bank.id)
    }
    bankSpinner.adapter = bankSpinnerAdapter
  }

  private fun setupUIWithDefaultValues() {
    amountInText = Constant.DEFAULT_AMOUNT_IN_VALUE.toString()
    setCurrencyInCheckedById(Constant.DEFAULT_CURRENCY_IN_ID)
    setCurrencyOutCheckedById(Constant.DEFAULT_CURRENCY_OUT_ID)
    setBankSelection(Constant.DEFAULT_BANK_ID)
    initializeViewListeners()
  }

  private var amountInText: String
    get() = amountInField.text.toString()
    set(text) = amountInField.setText(text)

  private fun setAmountOutText(text: String) = amountOutField.setText(text)

  private fun setCurrencyInCheckedById(currencyId: Int) = currenciesInGroup.setCurrencyCheckedById(
      currencyId)

  private fun setCurrencyOutCheckedById(
      currencyId: Int) = currenciesOutGroup.setCurrencyCheckedById(currencyId)

  private fun setBankSelection(position: Int) = bankSpinner.setSelection(position)

  private fun initializeViewListeners() {
    compositeSubscription.add(
        RxTextView.textChanges(amountInField).subscribe { charSequence -> applyConversion() })

    compositeSubscription.add(RxAdapterView.itemSelections(bankSpinner).subscribe { position ->
      if (position !== Constant.BEST_EXCHANGE_BANK_ID) setBestExchangeBankText("")
      applyConversion()
    })

    ratesDateField.setOnDateChangedListener(object : DateView.OnDateChangedListener {
      override fun onDateChanged(date: Date) {
        val dateText = DateUtil.getRateDateFormat().format(date)
        loadRates(dateText)
      }
    })

    currenciesInGroup.setOnCheckedChangeListener { group, checkedId ->
      onCurrencyInChanged(checkedId)
    }

    currenciesOutGroup.setOnCheckedChangeListener { group, checkedId ->
      onCurrencyOutChanged(checkedId)
    }

    whereToBuyButton.setOnClickListener { v ->
      amountInField.clearFocus()
      onWhereToBuyAction()
    }

    onlyActiveNowCheckBox.setOnClickListener { v -> amountInField.clearFocus() }

    bankSpinner.setOnTouchListener { v, event ->
      amountInField.clearFocus()
      false
    }

    ratesDateField.setOnTouchListener { v, event ->
      amountInField.clearFocus()
      false
    }
  }

  private fun onCurrencyInChanged(checkedId: Int) {
    amountInField.clearFocus()
    val currencyInId = checkedId
    val currencyOutId = checkedCurrencyOutId
    if (currencyInId == currencyOutId) {
      currencyOutCheckNext(checkedId)
    }
    applyConversion()
  }

  private fun onCurrencyOutChanged(checkedId: Int) {
    amountInField.clearFocus()
    val currencyOutId = checkedId
    val currencyInId = checkedCurrencyInId
    if (currencyInId == currencyOutId) {
      currencyInCheckNext(checkedId)
    }
    applyConversion()
  }

  private val checkedCurrencyInId: Int
    get() = currenciesInGroup.checkedRadioButtonId

  private val checkedCurrencyOutId: Int
    get() = currenciesOutGroup.checkedRadioButtonId

  private fun currencyInCheckNext(checkedId: Int) = currenciesInGroup.checkNextCurrency(checkedId)

  private fun currencyOutCheckNext(checkedId: Int) = currenciesOutGroup.checkNextCurrency(checkedId)

  private fun notifyNoExchangeRates() {
    showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR, getString(R.string.no_rate))
    setAmountOutText(Constant.NO_EXCHANGE_RATES_OUT_VALUE)
  }

  private fun validateConversionParams(bankRateList: List<RateModel>, currencyInRateValue: Double,
      currencyOutRateValue: Double): Boolean {
    if (bankRateList.size == 0 || currencyInRateValue == 0.0 || currencyOutRateValue == 0.0) {
      notifyNoExchangeRates()
      return false
    }
    return true
  }

  private fun convertBestExchange(): BestExchangeModel {
    var bankRateList: List<RateModel>
    var amountInValue: Double
    var currencyInRateValue: Double
    var currencyOutRateValue: Double
    var amountOutValue = 0.0

    var bestExchangeModel = BestExchangeModel()
    for (bank in bankList) {
      if (bank.id != 1) {
        bankRateList = ExchangeRatesUtil.getBankRates(rateList, bank.id)
        amountInValue = Converter.toDouble(amountInText)
        currencyInRateValue = ExchangeRatesUtil.getCurrencyRateValue(bankRateList,
            checkedCurrencyInId)
        currencyOutRateValue = ExchangeRatesUtil.getCurrencyRateValue(bankRateList,
            checkedCurrencyOutId)
        val bankAmountOutValue = ExchangeRatesUtil.convert(amountInValue, currencyInRateValue,
            currencyOutRateValue)
        if (bankAmountOutValue > amountOutValue) {
          amountOutValue = bankAmountOutValue
          bestExchangeModel = BestExchangeModel(bank, amountOutValue)
        }
      }

      setAmountOutText("%.2f".format(bestExchangeModel.amountOutValue))

      val bestExchangeBankText = if (bestExchangeModel.bank != null) getString(
          R.string.format_exchange_rates_best_bank).format(
          bestExchangeModel.bank!!.name)
      else getString(R.string.bank_not_found)
      setBestExchangeBankText(bestExchangeBankText)
    }

    return bestExchangeModel
  }

  private fun applyConversion() {
    val bankId = selectedBankId
    if (bankId == Constant.BEST_EXCHANGE_BANK_ID) {
      convertBestExchange()
    } else {
      convertBank(bankId)
    }
  }

  private fun convertBank(bankId: Int) {

    val bankRateList: List<RateModel>
    val amountInValue: Double
    val currencyInRateValue: Double
    val currencyOutRateValue: Double

    bankRateList = ExchangeRatesUtil.getBankRates(rateList, bankId)
    amountInValue = Converter.toDouble(amountInText)
    currencyInRateValue = ExchangeRatesUtil.getCurrencyRateValue(bankRateList, checkedCurrencyInId)
    currencyOutRateValue = ExchangeRatesUtil.getCurrencyRateValue(bankRateList,
        checkedCurrencyOutId)

    if (validateConversionParams(bankRateList, currencyInRateValue, currencyOutRateValue)) {
      val amountOutValue = ExchangeRatesUtil.convert(amountInValue, currencyInRateValue,
          currencyOutRateValue)
      setAmountOutText("%.2f".format(amountOutValue))
    }
  }

  private fun setBestExchangeBankText(text: String) {
    bestExchangeBankField.visibility = if (text.length > 0) View.VISIBLE else View.GONE
    bestExchangeBankField.text = text
  }

  private val selectedBankId: Int
    get() {
      val bankSpinnerItemModel = bankSpinner.selectedItem as BankSpinnerItemModel
      return bankSpinnerItemModel.bankId
    }

  private fun onWhereToBuyAction() {
    showLoadingDialog()
    var bankId = selectedBankId
    if (bankId == Constant.BNM_BANK_ID) {
      setBankSelection(Constant.BEST_EXCHANGE_BANK_ID)
      bankId = Constant.BEST_EXCHANGE_BANK_ID
    }
    if (bankId == Constant.BEST_EXCHANGE_BANK_ID) {
      val bestExchangeModel = convertBestExchange()
      if (bestExchangeModel.bank == null) {
        hideLoadingDialog()
        return
      }
      bankId = bestExchangeModel.bank!!.id
    }

    val onlyActive = onlyActiveNow()
    loadBankBranches(bankId, onlyActive)
  }

  private fun onlyActiveNow(): Boolean = onlyActiveNowCheckBox.isChecked

  private fun loadBankBranches(bankId: Int, active: Boolean) {
    showLoadingDialog()
    loadingDialogCancelSubscription.add(
        exchangeRatesRepository.getBranches(bankId, active).compose(
            ObservableTransformation.applyIOToMainThreadSchedulers<List<BranchModel>>()).compose(
            this.bindToLifecycle<List<BranchModel>>()).subscribe(
            object : ObserverAdapter<List<BranchModel>>() {
              override fun onNext(branchModels: List<BranchModel>) {
                hideLoadingDialog()
                RxBusKotlin.postIfHasObservers(WhereToBuyEvent(branchModels))
              }

              override fun onError(e: Throwable) {
                hideLoadingDialog()
                showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                    getString(R.string.error_something_gone_wrong))
              }
            }))
  }

  companion object {

    fun newInstance(): ExchangeRatesFragment {
      return ExchangeRatesFragment()
    }
  }
}
