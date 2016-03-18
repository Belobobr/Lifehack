package md.fusionworks.lifehack.movies

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_movie.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.activity.NavigationDrawerActivity
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.rx.RxBus

class MoviesActivity : NavigationDrawerActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie)

    movieList.layoutManager = LinearLayoutManager(this)
    movieList.adapter = MoviesAdapter(movieList)
    initializeCinemaSpinner()
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
}
