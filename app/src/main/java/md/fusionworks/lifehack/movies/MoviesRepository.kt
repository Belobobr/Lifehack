package md.fusionworks.lifehack.sales

import md.fusionworks.lifehack.api.banks.CinemaService
import md.fusionworks.lifehack.api.cinema.model.MovieModel
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.movies.mapper.MovieDataMapper
import md.fusionworks.lifehack.rx.ObservableTransformation
import md.fusionworks.lifehack.taxi.CinemaDataMapper
import rx.Observable
import javax.inject.Inject

/**
 * Created by ungvas on 2/10/16.
 */
@PerActivity
class MoviesRepository @Inject constructor(private val cinemaService: CinemaService,
    private val cinemaDataMapper: CinemaDataMapper, private val movieDataMapper: MovieDataMapper) {

  fun getAllCinemas(lang: String) = cinemaService.getAllCinemas(lang)
      .compose(ObservableTransformation.applyApiRequestConfiguration())
      .map({ cinemaDataMapper.transform(it) })

  val allMovies: Observable<List<MovieModel>>
    get() = cinemaService.getAllMovies()
        .compose(ObservableTransformation.applyApiRequestConfiguration())
        .map({ movieDataMapper.transform(it) })

  fun getMoviesByCinema(cinema: String) = cinemaService.getMoviesByCinema(cinema)
      .compose(ObservableTransformation.applyApiRequestConfiguration())
      .map({ movieDataMapper.transform(it) })
}

