package md.fusionworks.lifehack.exchange_rates.mapper

import md.fusionworks.lifehack.api.banks.model.exchange_rates.Currency
import md.fusionworks.lifehack.exchange_rates.model.CurrencyModel

/**
 * Created by ungvas on 2/10/16.
 */
class CurrencyDataMapper {

  fun transform(currency: Currency) = CurrencyModel(currency.id, currency.name)

  fun transform(currencyList: List<Currency>) = currencyList.map { transform(it) }
}
