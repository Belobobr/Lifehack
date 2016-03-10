package md.fusionworks.lifehack.ui

import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_lifehacks.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.util.Constant

/**
 * Created by ungvas on 3/7/16.
 */
class LifehacksActivity : NavigationDrawerActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_lifehacks)
    lifeHacksWebView.setWebChromeClient(ChromeClient())
    lifeHacksWebView.setWebViewClient(WebViewClient())
    lifeHacksWebView.settings.javaScriptEnabled = true;
    lifeHacksWebView.setOnKeyListener { view, i, keyEvent ->
      if (keyEvent.action == KeyEvent.ACTION_DOWN) {
        val webView = view as WebView
        when (i) {
          KeyEvent.KEYCODE_BACK -> {
            if (webView.isFocusable && webView.canGoBack()) {
              NavigationDrawerActivity().showLoadingDialog()
              webView.goBack()
              true
            }
            false
          }
        }
      }
      false
    }
    showLoadingDialog()
    lifeHacksWebView.loadUrl("http://lifehack.md")
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_life_hacks))
  }

  override fun getSelfDrawerItem() = Constant.DRAWER_ITEM_LIFE_HACKS

  inner private class ChromeClient : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
      if (newProgress >= 85) hideLoadingDialog()
      super.onProgressChanged(view, newProgress)
    }
  }

  inner private class WebViewClient : android.webkit.WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
      showLoadingDialog()
      view?.loadUrl(url)
      return false
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?,
        error: WebResourceError?) {
      NavigationDrawerActivity().showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
          LifehacksActivity().lifeHacksWebView.context.getString(
              R.string.error_something_gone_wrong))
    }
  }
}