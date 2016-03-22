package md.fusionworks.lifehack.movies

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_movie.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.api.cinema.model.CinemaModel
import md.fusionworks.lifehack.helper.LocaleHelper
import md.fusionworks.lifehack.rx.ObservableTransformation
import md.fusionworks.lifehack.rx.ObserverAdapter
import md.fusionworks.lifehack.rx.RxBus
import md.fusionworks.lifehack.sales.MoviesRepository
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.view.activity.NavigationDrawerActivity
import javax.inject.Inject

class MoviesActivity : NavigationDrawerActivity() {

  @Inject lateinit var moviesRepository: MoviesRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
    setContentView(R.layout.activity_movie)

    movieList.layoutManager = LinearLayoutManager(this)
    movieList.adapter = MoviesAdapter(movieList)
    initializeCinemaSpinner()
    getAllCinemas()
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_movie))
  }

  override fun getSelfDrawerItem() = Constant.DRAWER_ITEM_MOVIES

  private fun initializeCinemaSpinner() {
    val adapter = ArrayAdapter.createFromResource(this,
        R.array.cinema_list, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    cinemaSpinner.adapter = adapter;
  }

  override fun listenForEvents() {
    super.listenForEvents()
    RxBus.event(MovieClickEvent::class.java)
        .compose(bindToLifecycle<MovieClickEvent>())
        .subscribe { navigator.navigateToMovieDetailActivity(this) }
  }

  private fun getAllCinemas() {
    showLoadingDialog()
    loadingDialogCancelSubscription.add(
        moviesRepository.getAllCinemas(LocaleHelper.getLanguage(this))
            .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
            .compose(bindToLifecycle())
            .subscribe {
              object : ObserverAdapter<List<CinemaModel>>() {
                override fun onNext(t: List<CinemaModel>) {

                }

                override fun onError(e: Throwable) {

                }
              }
            })
  }
}
