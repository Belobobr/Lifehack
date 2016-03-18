package md.fusionworks.lifehack.exchange_rates.model

/**
 * Created by ungvas on 10/30/15.
 */
class BestExchangeModel {

  var bank: BankModel? = null
  var amountOutValue: Double = 0.toDouble()

  constructor() {
  }

  constructor(bank: BankModel, amountOutvalue: Double) {
    this.bank = bank
    this.amountOutValue = amountOutvalue
  }
}
