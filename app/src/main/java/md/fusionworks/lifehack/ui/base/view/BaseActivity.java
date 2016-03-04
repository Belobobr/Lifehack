package md.fusionworks.lifehack.ui.base.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.Navigator;
import md.fusionworks.lifehack.ui.widget.LoadingDialog;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.rx.RxBus;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ungvas on 10/15/15.
 */
public class BaseActivity extends RxAppCompatActivity implements LoadingDialog.OnCancelListener {

  @Nullable @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

  private RxBus rxBus;
  private Navigator navigator;
  private LoadingDialog loadingDialog;
  protected CompositeSubscription loadingDialogCancelSubscription;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    rxBus = RxBus.getInstance();
    navigator = new Navigator();
    loadingDialog = new LoadingDialog(this, this);
    loadingDialogCancelSubscription = new CompositeSubscription();
  }

  @Override protected void onResume() {
    super.onResume();
    listenForEvents();
  }

  @Override public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
  }

  protected void addFragment(int containerViewId, Fragment fragment) {
    android.support.v4.app.FragmentTransaction fragmentTransaction =
        this.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(containerViewId, fragment, null);
    fragmentTransaction.commit();
  }

  public void showNotificationToast(int type, String message) {
    if (coordinatorLayout != null) {
      coordinatorLayout.postDelayed(() -> {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        if (type == Constant.NOTIFICATION_TOAST_ERROR) {
          snackbar.getView().setBackgroundColor(Color.RED);
        }
        snackbar.show();
      }, Constant.NOTIFICATION_TOAST_SHOW_DELAY);
    }
  }

  public void showNotificationToast(int type, String message,
      NotificationToastActionListener notificationToastActionListener) {
    if (coordinatorLayout != null) {
      coordinatorLayout.postDelayed(() -> {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
        if (type == Constant.NOTIFICATION_TOAST_ERROR) {
          snackbar.getView().setBackgroundColor(Color.RED);
        }
        snackbar.setAction(R.string.retry, v -> {
          if (notificationToastActionListener != null) notificationToastActionListener.onClick();
        });
        snackbar.show();
      }, Constant.NOTIFICATION_TOAST_SHOW_DELAY);
    }
  }

  public RxBus getRxBus() {
    return rxBus;
  }

  public Navigator getNavigator() {
    return navigator;
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

  public interface NotificationToastActionListener {
    void onClick();
  }
}
