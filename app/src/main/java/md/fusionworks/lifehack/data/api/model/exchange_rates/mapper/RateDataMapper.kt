package md.fusionworks.lifehack.data.api.model.exchange_rates.mapper

import md.fusionworks.lifehack.data.api.model.exchange_rates.Rate
import md.fusionworks.lifehack.ui.exchange_rates.model.RateModel

/**
 * Created by ungvas on 2/10/16.
 */
class RateDataMapper(private val bankDataMapper: BankDataMapper, private val currencyDataMapper: CurrencyDataMapper) {

  fun transform(rate: Rate): RateModel {
    return RateModel(rate.id, rate.rateIn, rate.rateOut, rate.date,
        currencyDataMapper.transform(rate.currency!!), bankDataMapper.transform(rate.bank!!))
  }

  fun transform(rateList: List<Rate>) = rateList.map { transform(it) }
}
