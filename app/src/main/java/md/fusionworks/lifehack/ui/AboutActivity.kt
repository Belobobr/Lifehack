package md.fusionworks.lifehack.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.util.Constant

class AboutActivity : NavigationDrawerActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_about)

    emailField.setOnClickListener {
      navigator.navigateToMail(this, getString(R.string.contact_email))
    }
    urlField.setOnClickListener { navigator.navigateToUrl(this, getString(R.string.contact_url)) }
    developerLogoField.setOnClickListener {
      navigator.navigateToUrl(this, getString(R.string.contact_developer_url))
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_about))
  }

  override fun getSelfDrawerItem() = Constant.DRAWER_ITEM_ABOUT
}