package md.fusionworks.lifehack.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.util.Constant;

public class LifeHacksActivity extends NavigationDrawerActivity {

  @Bind(R.id.lifeHacksWebView) WebView lifeHacksWebView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lifehacks);
    lifeHacksWebView.setWebChromeClient(new ChromeClient());
    lifeHacksWebView.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        showLoadingDialog();
        lifeHacksWebView.loadUrl(url);
        return false;
      }

      @Override public void onReceivedError(WebView view, WebResourceRequest request,
          WebResourceError error) {
        showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
            getString(R.string.field_something_gone_wrong));
      }
    });
    showLoadingDialog();
    lifeHacksWebView.getSettings().setJavaScriptEnabled(true);
    lifeHacksWebView.setOnKeyListener((v, keyCode, event) -> {
      if (event.getAction() == KeyEvent.ACTION_DOWN) {
        WebView webView = (WebView) v;

        switch (keyCode) {
          case KeyEvent.KEYCODE_BACK:
            if (webView.isFocusable() && webView.canGoBack()) {
              showLoadingDialog();
              webView.goBack();
              return true;
            }
            break;
        }
      }
      return false;
    });
    lifeHacksWebView.loadUrl("http://lifehack.md");
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle("Лайфхаки");
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_LIFE_HACKS;
  }

  private class ChromeClient extends WebChromeClient {
    @Override public void onProgressChanged(WebView view, int newProgress) {
      if (newProgress >= 85) {
        hideLoadingDialog();
      }
      super.onProgressChanged(view, newProgress);
    }
  }
}
