package md.fusionworks.lifehack.ui.base.view

import android.content.Context
import com.trello.rxlifecycle.components.support.RxFragment
import md.fusionworks.lifehack.ui.Navigator
import md.fusionworks.lifehack.ui.widget.LoadingDialog
import rx.subscriptions.CompositeSubscription

/**
 * Created by ungvas on 10/22/15.
 */
open class BaseFragment : RxFragment() {

  private lateinit var baseActivity: BaseActivity
  protected lateinit var compositeSubscription: CompositeSubscription
  private lateinit var loadingDialog: LoadingDialog
  protected lateinit var loadingDialogCancelSubscription: CompositeSubscription

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    try {
      baseActivity = context as BaseActivity
      onFragmentAttached(context)
      compositeSubscription = CompositeSubscription()
      loadingDialog = LoadingDialog(context) {
        loadingDialogCancelSubscription.clear()
        onLoadingDialogCanceled()
      }
      loadingDialogCancelSubscription = CompositeSubscription()
    } catch (e: ClassCastException) {
      throw ClassCastException(context!!.toString() + " must implement BaseActivity")
    }

  }

  override fun onDestroy() {
    super.onDestroy()
    compositeSubscription.unsubscribe()
  }

  override fun onResume() {
    super.onResume()
    listenForEvents()
  }

  protected fun onFragmentAttached(context: Context) {
  }

  protected fun showNotificationToast(type: Int, message: String) {
    baseActivity.showNotificationToast(type, message)
  }

  protected fun showNotificationToast(type: Int, message: String, onRetry: () -> Unit) {
    baseActivity.showNotificationToast(type, message, onRetry)
  }

  protected val navigator: Navigator
    get() = baseActivity.navigator

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
