package md.fusionworks.lifehack.widget

import android.app.Dialog
import android.content.Context
import md.fusionworks.lifehack.R

class LoadingDialog(context: Context, onCancel: () -> Unit) : Dialog(
    context, R.style.LoadingDialogStyle) {

  init {
    setTitle(null)
    setCancelable(true)
    setOnCancelListener { dialog -> onCancel() }
    setContentView(R.layout.layout_loading_dialog)
  }
}