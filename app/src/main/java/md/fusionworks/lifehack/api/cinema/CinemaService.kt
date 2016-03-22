package md.fusionworks.lifehack.api.banks

import md.fusionworks.lifehack.api.cinema.model.Cinema
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by ungvas on 10/28/15.
 */
interface CinemaService {

  @GET(CINEMAS) fun getAllCinemas(@Path("lang") lang: String): Observable<Response<List<Cinema>>>

  companion object {
    const val ENDPOINT = "http://cinema.digest.md/"
    const val CINEMAS = "cinemas/list/{lang}"
  }
}
