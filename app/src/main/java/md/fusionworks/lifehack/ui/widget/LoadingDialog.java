package md.fusionworks.lifehack.ui.widget;

import android.app.Dialog;
import android.content.Context;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.LoadingDialogCancelEvent;
import md.fusionworks.lifehack.util.rx.RxBus;

public class LoadingDialog extends Dialog {

  public LoadingDialog(Context context) {
    super(context, R.style.LoadingDialogStyle);
    setTitle(null);
    setCancelable(true);
    setOnCancelListener(dialog -> RxBus.getInstance().postIfHasObservers(new LoadingDialogCancelEvent()));
    setContentView(R.layout.layout_loading_dialog);
  }
}