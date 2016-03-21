package md.fusionworks.lifehack.view.activity

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.application.AppComponent
import md.fusionworks.lifehack.application.LifehackApp
import md.fusionworks.lifehack.navigator.Navigator
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.view.widget.LoadingDialog
import org.jetbrains.anko.find
import rx.subscriptions.CompositeSubscription

/**
 * Created by ungvas on 10/15/15.
 */
open class BaseActivity : RxAppCompatActivity() {

  val coordinatorLayout by lazy { find<CoordinatorLayout>(R.id.coordinatorLayout) }

  lateinit var navigator: Navigator
  private lateinit var loadingDialog: LoadingDialog
  protected lateinit var loadingDialogCancelSubscription: CompositeSubscription

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    navigator = Navigator()
    loadingDialog = LoadingDialog(this) {
      loadingDialogCancelSubscription.clear()
      onLoadingDialogCanceled()
    }
    loadingDialogCancelSubscription = CompositeSubscription()
  }

  override fun onResume() {
    super.onResume()
    listenForEvents()
  }

  protected fun addFragment(containerViewId: Int, fragment: Fragment) {
    val fragmentTransaction = this.supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(containerViewId, fragment, null)
    fragmentTransaction.commit()
  }

  fun showNotificationToast(type: Int, message: String) {
    if (coordinatorLayout != null) {
      coordinatorLayout.postDelayed({
        val snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT)
        if (type == Constant.NOTIFICATION_TOAST_ERROR) {
          snackbar.view.setBackgroundColor(Color.RED)
        }
        snackbar.show()
      }, Constant.NOTIFICATION_TOAST_SHOW_DELAY.toLong())
    }
  }

  fun showNotificationToast(type: Int, message: String, onRetry: () -> Unit) {
    if (coordinatorLayout != null) {
      coordinatorLayout.postDelayed({
        val snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
        if (type == Constant.NOTIFICATION_TOAST_ERROR) {
          snackbar.view.setBackgroundColor(Color.RED)
        }
        snackbar.setAction(R.string.retry) { v -> onRetry() }
        snackbar.show()
      }, Constant.NOTIFICATION_TOAST_SHOW_DELAY.toLong())
    }
  }

  fun showLoadingDialog() {
    if (this.loadingDialog != null) {
      this.loadingDialog.show()
    }
  }

  val appComponent: AppComponent
    get() = LifehackApp.component

  fun hideLoadingDialog() {
    if (this.loadingDialog != null) {
      this.loadingDialog.dismiss()
    }
  }

  protected open fun listenForEvents() {
  }

  protected fun onLoadingDialogCanceled() {
  }

  protected open fun initializeDIComponent() {
  }
}
