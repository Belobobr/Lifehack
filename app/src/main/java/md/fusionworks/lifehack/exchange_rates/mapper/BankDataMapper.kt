package md.fusionworks.lifehack.exchange_rates.mapper

import md.fusionworks.lifehack.api.banks.model.exchange_rates.Bank
import md.fusionworks.lifehack.exchange_rates.model.BankModel

/**
 * Created by ungvas on 2/10/16.
 */
class BankDataMapper {

  fun transform(bank: Bank) = BankModel(bank.id, bank.name)

  fun transform(bankList: List<Bank>) = bankList.map { transform(it) }
}
