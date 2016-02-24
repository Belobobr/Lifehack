package md.fusionworks.lifehack.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.trello.rxlifecycle.components.support.RxFragment;
import md.fusionworks.lifehack.util.rx.RxBus;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ungvas on 10/22/15.
 */
public class BaseFragment extends RxFragment {

  private BaseActivity baseActivity;
  private CompositeSubscription compositeSubscription;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    try {
      baseActivity = (BaseActivity) context;
      onFragmentAttached(context);
      compositeSubscription = new CompositeSubscription();
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
    ButterKnife.bind(this, view);
    return view;
  }

  protected void onFragmentAttached(Context context) {
  }

  protected void showNotificationToast(int type, String message) {
    baseActivity.showNotificationToast(type, message);
  }

  protected void showNotificationToast(int type, String message,
      NotificationToastActionListener notificationToastActionListener) {
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

  protected void showLoadingDialog() {
    baseActivity.showLoadingDialog();
  }

  protected void hideLoadingDialog() {
    baseActivity.hideLoadingDialog();
  }

  protected void listenForEvents() {
  }
}
