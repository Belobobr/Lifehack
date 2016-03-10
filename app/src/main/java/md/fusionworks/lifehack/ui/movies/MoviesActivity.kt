package md.fusionworks.lifehack.ui.movies

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_movie.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.NavigationDrawerActivity
import md.fusionworks.lifehack.util.Constant

class MoviesActivity : NavigationDrawerActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie)

    movieList.layoutManager = LinearLayoutManager(this)
    movieList.adapter = MoviesAdapter(movieList)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_movie))
  }

  override fun getSelfDrawerItem(): Int {
    return Constant.DRAWER_ITEM_MOVIES
  }
}
