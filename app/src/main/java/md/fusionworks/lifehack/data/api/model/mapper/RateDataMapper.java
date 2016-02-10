package md.fusionworks.lifehack.data.api.model.mapper;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.lifehack.data.api.model.exchange_rates.Rate;
import md.fusionworks.lifehack.ui.model.exchange_rates.RateModel;

/**
 * Created by ungvas on 2/10/16.
 */
public class RateDataMapper {

    private BankDataMapper bankDataMapper;
    private CurrencyDataMapper currencyDataMapper;

    public RateDataMapper(BankDataMapper bankDataMapper, CurrencyDataMapper currencyDataMapper) {
        this.bankDataMapper = bankDataMapper;
        this.currencyDataMapper = currencyDataMapper;
    }

    public RateModel transform(Rate rate) {
        return new RateModel(
                rate.getId(),
                rate.getRateIn(),
                rate.getRateOut(),
                rate.getDate(),
                currencyDataMapper.transform(rate.getCurrency()),
                bankDataMapper.transform(rate.getBank()));
    }

    public List<RateModel> transform(List<Rate> rateList) {
        List<RateModel> rateModelList = new ArrayList<>(rateList.size());
        RateModel rateModel;
        for (Rate rate : rateList) {
            rateModel = transform(rate);
            if (rateModel != null) {
                rateModelList.add(rateModel);
            }
        }

        return rateModelList;
    }
}
