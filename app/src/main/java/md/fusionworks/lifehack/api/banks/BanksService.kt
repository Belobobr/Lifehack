package md.fusionworks.lifehack.api.banks

import md.fusionworks.lifehack.api.banks.model.exchange_rates.Bank
import md.fusionworks.lifehack.api.banks.model.exchange_rates.Branch
import md.fusionworks.lifehack.api.banks.model.exchange_rates.Currency
import md.fusionworks.lifehack.api.banks.model.exchange_rates.Rate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by ungvas on 10/28/15.
 */
interface BanksService {

  @GET(BANKS) fun banks(): Observable<Response<List<Bank>>>

  @GET(CURRENCIES) fun currencies(): Observable<Response<List<Currency>>>

  @GET(RATES) fun getRates(@Query("date") date: String): Observable<Response<List<Rate>>>

  @GET(BRANCHES) fun getBankBranches(@Path("bank") bank: Int,
      @Query("active") active: Boolean): Observable<Response<List<Branch>>>

  companion object {
    const val ENDPOINT = "http://banks.digest.md/"
    const val BANKS = "api/banks.json?_format=json"
    const val RATES = "api/rates.json?_format=json"
    const val CURRENCIES = "api/currencies.json?_format=json"
    const val BRANCHES = "api/banks/{bank}/branches.json?_format=json"
  }
}
