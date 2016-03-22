package md.fusionworks.lifehack.movies.mapper

import md.fusionworks.lifehack.api.cinema.model.Movie
import md.fusionworks.lifehack.api.cinema.model.MovieModel
import md.fusionworks.lifehack.api.cinema.model.Showtime
import md.fusionworks.lifehack.api.cinema.model.ShowtimeModel
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.taxi.CinemaDataMapper
import javax.inject.Inject

/**
 * Created by ungvas on 3/22/16.
 */
@PerActivity
class MovieDataMapper
@Inject constructor(private var cinemaDataMapper: CinemaDataMapper) {

  fun transform(movie: Movie) = MovieModel(movie.id, movie.siteId, movie.name, movie.description,
      movie.premiere, movie.language, movie.url, movie.posterUrl,
      transformShowtimeList(movie.showtimes))

  fun transform(movieList: List<Movie>) = movieList.map { transform(it) }

  private fun transformShowtime(showtime: Showtime) = ShowtimeModel(showtime.id, showtime.date,
      showtime.start, showtime.end, cinemaDataMapper.transform(showtime.cinema))

  private fun transformShowtimeList(
      showtimeList: List<Showtime>) = showtimeList.map { transformShowtime(it) }
}