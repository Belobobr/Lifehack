package md.fusionworks.lifehack.data.repository

import md.fusionworks.lifehack.data.api.BanksService
import md.fusionworks.lifehack.data.api.ServiceFactory
import md.fusionworks.lifehack.data.api.model.exchange_rates.mapper.BankDataMapper
import md.fusionworks.lifehack.data.api.model.exchange_rates.mapper.BranchDataMapper
import md.fusionworks.lifehack.data.api.model.exchange_rates.mapper.CurrencyDataMapper
import md.fusionworks.lifehack.data.api.model.exchange_rates.mapper.RateDataMapper
import md.fusionworks.lifehack.ui.exchange_rates.model.BankModel
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel
import md.fusionworks.lifehack.ui.exchange_rates.model.CurrencyModel
import md.fusionworks.lifehack.ui.exchange_rates.model.RateModel
import md.fusionworks.lifehack.util.rx.ObservableTransformation
import rx.Observable

/**
 * Created by ungvas on 2/10/16.
 */
class ExchangeRatesRepository {

  private val banksService: BanksService
  private val bankDataMapper: BankDataMapper
  private val currencyDataMapper: CurrencyDataMapper
  private val rateDataMapper: RateDataMapper
  private val branchDataMapper: BranchDataMapper

  init {
    banksService = ServiceFactory().buildBanksService()
    bankDataMapper = BankDataMapper()
    currencyDataMapper = CurrencyDataMapper()
    rateDataMapper = RateDataMapper(bankDataMapper, currencyDataMapper)
    branchDataMapper = BranchDataMapper()
  }

  val banks: Observable<List<BankModel>>
    get() = banksService.banks().compose(
        ObservableTransformation.applyApiRequestConfiguration())
        .map { banks -> bankDataMapper.transform(banks) }

  val currencies: Observable<List<CurrencyModel>>
    get() = banksService.currencies().compose(
        ObservableTransformation.applyApiRequestConfiguration())
        .map { currencies -> currencyDataMapper.transform(currencies) }

  fun getRates(date: String): Observable<List<RateModel>> = banksService.getRates(date).compose(
      ObservableTransformation.applyApiRequestConfiguration())
      .map { rates -> rateDataMapper.transform(rates) }

  fun getBranches(bankId: Int, active: Boolean): Observable<List<BranchModel>> = banksService.getBankBranches(bankId, active).compose(
      ObservableTransformation.applyApiRequestConfiguration())
      .map { branches -> branchDataMapper.transform(branches) }
}
