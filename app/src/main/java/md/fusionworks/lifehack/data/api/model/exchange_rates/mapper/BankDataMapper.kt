package md.fusionworks.lifehack.data.api.model.exchange_rates.mapper

import md.fusionworks.lifehack.data.api.model.exchange_rates.Bank
import md.fusionworks.lifehack.ui.exchange_rates.model.BankModel

/**
 * Created by ungvas on 2/10/16.
 */
class BankDataMapper {

  fun transform(bank: Bank) = BankModel(bank.id, bank.name)

  fun transform(bankList: List<Bank>) = bankList.map { transform(it) }
}