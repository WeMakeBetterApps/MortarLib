package mortar.lib.sample.activity;

import android.os.Bundle;

import mortar.Blueprint;
import mortar.lib.activity.MortarActivity;
import mortar.lib.sample.R;
import mortar.lib.sample.screen.ActivityScreen;

public class MainActivity extends MortarActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override protected Blueprint getNewActivityScreen() {
    return new ActivityScreen();
  }

}
