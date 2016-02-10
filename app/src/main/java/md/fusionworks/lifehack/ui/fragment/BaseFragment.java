package md.fusionworks.lifehack.ui.fragment;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import md.fusionworks.lifehack.ui.activity.BaseActivity;
import md.fusionworks.lifehack.ui.listener.NotificationToastActionListener;
import md.fusionworks.lifehack.ui.navigator.Navigator;

/**
 * Created by ungvas on 10/22/15.
 */
public class BaseFragment extends RxFragment {

    private BaseActivity baseActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            baseActivity = (BaseActivity) context;
            onFragmentAttached(context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BaseActivity");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected View inflateAndBindViews(LayoutInflater inflater, @LayoutRes int layoutResId, ViewGroup container) {
        View view = inflater.inflate(layoutResId, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    protected void onFragmentAttached(Context context) {
    }

    protected void showNotificationToast(int type, String message) {
        baseActivity.showNotificationToast(type, message);
    }

    protected void showNotificationToast(int type, String message, NotificationToastActionListener notificationToastActionListener) {
        baseActivity.showNotificationToast(type, message, notificationToastActionListener);
    }

    protected Navigator getNavigator() {
        return baseActivity.getNavigator();
    }

    protected void showLoadingDialog(String message) {
        baseActivity.showLoadingDialog(message);
    }

    protected void hideLoadingDialog() {
        baseActivity.hideLoadingDialog();
    }
}
