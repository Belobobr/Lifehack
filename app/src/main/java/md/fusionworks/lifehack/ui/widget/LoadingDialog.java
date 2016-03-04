package md.fusionworks.lifehack.ui.widget;

import android.app.Dialog;
import android.content.Context;
import md.fusionworks.lifehack.R;

public class LoadingDialog extends Dialog {

  public LoadingDialog(Context context, OnCancelListener onCancelListener) {
    super(context, R.style.LoadingDialogStyle);
    setTitle(null);
    setCancelable(true);
    setOnCancelListener(dialog -> onCancelListener.onCancel());
    setContentView(R.layout.layout_loading_dialog);
  }

  public interface OnCancelListener {
    void onCancel();
  }
}