package md.fusionworks.lifehack.data.api.model.mapper;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.lifehack.data.api.model.exchange_rates.Bank;
import md.fusionworks.lifehack.ui.model.exchange_rates.BankModel;

/**
 * Created by ungvas on 2/10/16.
 */
public class BankDataMapper {

  public BankModel transform(Bank bank) {
    return new BankModel(bank.id, bank.name);
  }

  public List<BankModel> transform(List<Bank> bankList) {
    List<BankModel> bankModelList = new ArrayList<>(bankList.size());
    BankModel bankModel;
    for (Bank bank : bankList) {
      bankModel = transform(bank);
      if (bankModel != null) {
        bankModelList.add(bankModel);
      }
    }

    return bankModelList;
  }
}
