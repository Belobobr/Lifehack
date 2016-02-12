package md.fusionworks.lifehack.ui.fragment;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.trello.rxlifecycle.components.support.RxFragment;
import md.fusionworks.lifehack.ui.activity.BaseActivity;
import md.fusionworks.lifehack.ui.listener.NotificationToastActionListener;
import md.fusionworks.lifehack.ui.navigator.Navigator;
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
}
