package md.fusionworks.lifehack.ui.base.view

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import md.fusionworks.lifehack.LifehackApp
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.Navigator
import md.fusionworks.lifehack.ui.widget.LoadingDialog
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.rx.RxBusDagger
import org.jetbrains.anko.find
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by ungvas on 10/15/15.
 */
open class BaseActivity : RxAppCompatActivity() {

  val coordinatorLayout by lazy { find<CoordinatorLayout>(R.id.coordinatorLayout) }

  lateinit var navigator: Navigator
  private lateinit var loadingDialog: LoadingDialog
  protected lateinit var loadingDialogCancelSubscription: CompositeSubscription

  @Inject lateinit var rxBus: RxBusDagger

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LifehackApp.component.inject(this)
    navigator = Navigator()
    loadingDialog = LoadingDialog(this) {
      loadingDialogCancelSubscription.clear()
      onLoadingDialogCanceled()
    }
    loadingDialogCancelSubscription = CompositeSubscription()

    rxBus.hasObservers()
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

  fun hideLoadingDialog() {
    if (this.loadingDialog != null) {
      this.loadingDialog.dismiss()
    }
  }

  protected open fun listenForEvents() {
  }

  protected fun onLoadingDialogCanceled() {
  }
}
