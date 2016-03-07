package md.fusionworks.lifehack.ui.widget

import android.app.Dialog
import android.content.Context
import md.fusionworks.lifehack.R

class LoadingDialog(context: Context, onCancelListener: LoadingDialog.OnCancelListener) : Dialog(
    context, R.style.LoadingDialogStyle) {

  init {
    setTitle(null)
    setCancelable(true)
    setOnCancelListener { dialog -> onCancelListener.onCancel() }
    setContentView(R.layout.layout_loading_dialog)
  }

  interface OnCancelListener {
    fun onCancel()
  }
}