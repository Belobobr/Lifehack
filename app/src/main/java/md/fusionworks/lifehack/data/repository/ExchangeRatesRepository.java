package md.fusionworks.lifehack.data.repository;

import java.util.List;
import md.fusionworks.lifehack.data.api.BanksService;
import md.fusionworks.lifehack.data.api.ServiceFactory;
import md.fusionworks.lifehack.data.api.model.exchange_rates.mapper.BankDataMapper;
import md.fusionworks.lifehack.data.api.model.exchange_rates.mapper.BranchDataMapper;
import md.fusionworks.lifehack.data.api.model.exchange_rates.mapper.CurrencyDataMapper;
import md.fusionworks.lifehack.data.api.model.exchange_rates.mapper.RateDataMapper;
import md.fusionworks.lifehack.ui.exchange_rates.model.BankModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.CurrencyModel;
import md.fusionworks.lifehack.ui.exchange_rates.model.RateModel;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import rx.Observable;

/**
 * Created by ungvas on 2/10/16.
 */
public class ExchangeRatesRepository {

  private BanksService banksService;
  private BankDataMapper bankDataMapper;
  private CurrencyDataMapper currencyDataMapper;
  private RateDataMapper rateDataMapper;
  private BranchDataMapper branchDataMapper;

  public ExchangeRatesRepository() {
    banksService = ServiceFactory.buildBanksService();
    bankDataMapper = new BankDataMapper();
    currencyDataMapper = new CurrencyDataMapper();
    rateDataMapper = new RateDataMapper(bankDataMapper, currencyDataMapper);
    branchDataMapper = new BranchDataMapper();
  }

  public Observable<List<BankModel>> getBanks() {
    return banksService.getBanks()
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(banks -> bankDataMapper.transform(banks));
  }

  public Observable<List<CurrencyModel>> getCurrencies() {
    return banksService.getCurrencies()
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(currencies -> currencyDataMapper.transform(currencies));
  }

  public Observable<List<RateModel>> getRates(String date) {
    return banksService.getRates(date)
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(rates -> rateDataMapper.transform(rates));
  }

  public Observable<List<BranchModel>> getBranches(int bankId, boolean active) {
    return banksService.getBankBranches(bankId, active)
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map(branches -> branchDataMapper.transform(branches));
  }
}
