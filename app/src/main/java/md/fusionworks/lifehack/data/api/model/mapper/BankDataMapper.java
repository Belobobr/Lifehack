package md.fusionworks.lifehack.data.api.model.mapper;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.lifehack.data.api.model.Bank;
import md.fusionworks.lifehack.ui.model.BankModel;

/**
 * Created by ungvas on 2/10/16.
 */
public class BankDataMapper {

    public BankModel transform(Bank bank) {
        return new BankModel(bank.getId(), bank.getName());
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