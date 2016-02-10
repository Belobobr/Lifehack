package md.fusionworks.lifehack.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ungvas on 10/15/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator = new Navigator(this);
    }

    protected void addFragment(int containerViewId, Fragment fragment) {

        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }
}
