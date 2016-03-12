package md.fusionworks.lifehack.ui.base.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.trello.rxlifecycle.components.support.RxFragment;
import md.fusionworks.lifehack.ui.Navigator;
import md.fusionworks.lifehack.ui.widget.LoadingDialog;
import md.fusionworks.lifehack.util.rx.RxBus;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ungvas on 10/22/15.
 */
public class BaseFragment extends RxFragment implements LoadingDialog.OnCancelListener {

  private BaseActivity baseActivity;
  private CompositeSubscription compositeSubscription;
  private LoadingDialog loadingDialog;
  protected CompositeSubscription loadingDialogCancelSubscription;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    try {
      baseActivity = (BaseActivity) context;
      onFragmentAttached(context);
      compositeSubscription = new CompositeSubscription();
      loadingDialog = new LoadingDialog(context, this);
      loadingDialogCancelSubscription = new CompositeSubscription();
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement BaseActivity");
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
    compositeSubscription.unsubscribe();
  }

  @Override public void onResume() {
    super.onResume();
    listenForEvents();
  }

  protected View inflateAndBindViews(LayoutInflater inflater, @LayoutRes int layoutResId,
      ViewGroup container) {
    View view = inflater.inflate(layoutResId, container, false);
    return view;
  }

  protected void onFragmentAttached(Context context) {
  }

  protected void showNotificationToast(int type, String message) {
    baseActivity.showNotificationToast(type, message);
  }

  protected void showNotificationToast(int type, String message,
      BaseActivity.NotificationToastActionListener notificationToastActionListener) {
    baseActivity.showNotificationToast(type, message, notificationToastActionListener);
  }

  protected RxBus getRxBus() {
    return baseActivity.getRxBus();
  }

  protected Navigator getNavigator() {
    return baseActivity.getNavigator();
  }

  protected CompositeSubscription getCompositeSubscription() {
    return compositeSubscription;
  }

  public void showLoadingDialog() {
    if (this.loadingDialog != null) {
      this.loadingDialog.show();
    }
  }

  public void hideLoadingDialog() {
    if (this.loadingDialog != null) {
      this.loadingDialog.dismiss();
    }
  }

  protected void listenForEvents() {
  }

  @Override public void onCancel() {
    loadingDialogCancelSubscription.clear();
    onLoadingDialogCanceled();
  }

  protected void onLoadingDialogCanceled() {
  }
}
