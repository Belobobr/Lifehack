package md.fusionworks.lifehack.movies

import android.os.Bundle
import android.support.v7.widget.Toolbar
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.activity.BaseActivity
import org.jetbrains.anko.find

class MovieDetailActivity : BaseActivity() {

  private val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_detail)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Batman v Superman"
  }
}
