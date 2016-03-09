package md.fusionworks.lifehack.ui.movies

import android.os.Bundle
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.NavigationDrawerActivity
import md.fusionworks.lifehack.util.Constant

class MoviesActivity : NavigationDrawerActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_movie))
  }

  override fun getSelfDrawerItem(): Int {
    return Constant.DRAWER_ITEM_MOVIES
  }
}
