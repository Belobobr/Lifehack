package md.fusionworks.lifehack.data.repository;

import android.content.Context;

import java.util.List;

import md.fusionworks.lifehack.data.api.LifehackService;
import md.fusionworks.lifehack.data.api.ServiceFactory;
import md.fusionworks.lifehack.data.api.model.mapper.BankDataMapper;
import md.fusionworks.lifehack.data.api.model.mapper.BranchDataMapper;
import md.fusionworks.lifehack.data.api.model.mapper.CurrencyDataMapper;
import md.fusionworks.lifehack.data.api.model.mapper.RateDataMapper;
import md.fusionworks.lifehack.ui.model.BankModel;
import md.fusionworks.lifehack.ui.model.BranchModel;
import md.fusionworks.lifehack.ui.model.CurrencyModel;
import md.fusionworks.lifehack.ui.model.RateModel;
import rx.Observable;

/**
 * Created by ungvas on 2/10/16.
 */
public class NewExchangeRatesRepository {

    private Context context;
    private LifehackService lifehackService;

    private BankDataMapper bankDataMapper;
    private CurrencyDataMapper currencyDataMapper;
    private RateDataMapper rateDataMapper;
    private BranchDataMapper branchDataMapper;

    public NewExchangeRatesRepository(Context context) {
        this.context = context;
        lifehackService = ServiceFactory.makeLifehackService(context);
        bankDataMapper = new BankDataMapper();
        currencyDataMapper = new CurrencyDataMapper();
        rateDataMapper = new RateDataMapper(bankDataMapper, currencyDataMapper);
        branchDataMapper = new BranchDataMapper();
    }

    public Observable<List<BankModel>> getBanks() {
        return lifehackService.getBanks()
                .map(listResponse -> listResponse.body())
                .map(banks -> bankDataMapper.transform(banks));
    }

    public Observable<List<CurrencyModel>> getCurrencies() {
        return lifehackService.getCurrencies()
                .map(listResponse -> listResponse.body())
                .map(currencies -> currencyDataMapper.transform(currencies));
    }

    public Observable<List<RateModel>> getRates(String date) {
        return lifehackService.getRates(date)
                .map(listResponse -> listResponse.body())
                .map(rates -> rateDataMapper.transform(rates));
    }

    public Observable<List<BranchModel>> getBranches(int bankId, boolean active) {
        return lifehackService.getBankBranches(bankId, active)
                .map(listResponse -> listResponse.body())
                .map(branches -> branchDataMapper.transform(branches));
    }
}
