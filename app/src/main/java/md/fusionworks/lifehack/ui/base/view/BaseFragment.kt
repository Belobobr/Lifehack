package md.fusionworks.lifehack.ui.base.view

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import md.fusionworks.lifehack.ui.Navigator
import md.fusionworks.lifehack.ui.widget.LoadingDialog
import rx.subscriptions.CompositeSubscription

/**
 * Created by ungvas on 10/22/15.
 */
open class BaseFragment : RxFragment(), LoadingDialog.OnCancelListener {

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
      loadingDialog = LoadingDialog(context, this)
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

  protected fun inflateAndBindViews(inflater: LayoutInflater?, @LayoutRes layoutResId: Int,
      container: ViewGroup?): View? {
    val view = inflater?.inflate(layoutResId, container, false)
    return view
  }

  protected fun onFragmentAttached(context: Context) {
  }

  protected fun showNotificationToast(type: Int, message: String) {
    baseActivity.showNotificationToast(type, message)
  }

  protected fun showNotificationToast(type: Int, message: String,
      notificationToastActionListener: BaseActivity.NotificationToastActionListener) {
    baseActivity.showNotificationToast(type, message, notificationToastActionListener)
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

  override fun onCancel() {
    loadingDialogCancelSubscription.clear()
    onLoadingDialogCanceled()
  }

  protected fun onLoadingDialogCanceled() {
  }
}
