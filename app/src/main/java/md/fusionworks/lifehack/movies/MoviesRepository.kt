package md.fusionworks.lifehack.sales

import md.fusionworks.lifehack.api.banks.CinemaService
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.rx.ObservableTransformation
import md.fusionworks.lifehack.taxi.CinemaDataMapper
import javax.inject.Inject

/**
 * Created by ungvas on 2/10/16.
 */
@PerActivity
class MoviesRepository @Inject constructor(private val cinemaService: CinemaService,
    private val cinemaDataMapper: CinemaDataMapper) {

  fun getAllCinemas(lang: String) = cinemaService.getAllCinemas(lang)
      .compose(ObservableTransformation.applyApiRequestConfiguration())
      .map({ cinemaDataMapper.transform(it) })
}

