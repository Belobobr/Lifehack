package md.fusionworks.lifehack.ui;

import android.os.Bundle;

public class LaunchActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getNavigator().navigateToMainActivity(this);
    finish();
  }
}