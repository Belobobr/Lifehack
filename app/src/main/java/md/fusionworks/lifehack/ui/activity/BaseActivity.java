package md.fusionworks.lifehack.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.listener.NotificationToastActionListener;
import md.fusionworks.lifehack.ui.navigator.Navigator;
import md.fusionworks.lifehack.util.Constant;

/**
 * Created by ungvas on 10/15/15.
 */
public class BaseActivity extends AppCompatActivity {

    @Nullable
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private Navigator navigator;
    private MaterialDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator = new Navigator();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, null);
        fragmentTransaction.commit();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, null);
        fragmentTransaction.commit();
    }

    public void showNotificationToast(int type, String message) {
        if (coordinatorLayout != null)
            coordinatorLayout.postDelayed(() -> {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
                if (type == Constant.NOTIFICATION_TOAST_ERROR)
                    snackbar.getView().setBackgroundColor(Color.RED);
                snackbar.show();
            }, Constant.NOTIFICATION_TOAST_SHOW_DELAY);
    }

    public void showNotificationToast(int type, String message, NotificationToastActionListener notificationToastActionListener) {
        if (coordinatorLayout != null)
            coordinatorLayout.postDelayed(() -> {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
                if (type == Constant.NOTIFICATION_TOAST_ERROR)
                    snackbar.getView().setBackgroundColor(Color.RED);
                snackbar.setAction(R.string.action_retry, v -> {
                    if (notificationToastActionListener != null)
                        notificationToastActionListener.onClick();
                });
                snackbar.show();
            }, Constant.NOTIFICATION_TOAST_SHOW_DELAY);
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public void showLoadingDialog(String message) {
        if (loadingDialog == null) {
            loadingDialog = new MaterialDialog.Builder(this)
                    .content(message)
                    .theme(Theme.LIGHT)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(true)
                    .show();
        }
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null)
            if (loadingDialog.isShowing())
                loadingDialog.dismiss();
        loadingDialog = null;
    }
}
