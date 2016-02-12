package mortar.lib.view;

import android.view.View;

import butterknife.ButterKnife;

public class ButterKnifeWrapper {
  private boolean mIsButterknifeAvailable = false;

  private static ButterKnifeWrapper instance;

  static ButterKnifeWrapper get() {
    if (instance == null)
      instance = new ButterKnifeWrapper();
    return instance;
  }

  private ButterKnifeWrapper() {
    try {
      Class.forName("butterknife.ButterKnife");
      mIsButterknifeAvailable = true;
    } catch (ClassNotFoundException ignored) {
    }
  }

  void inject(View view) {
    if (!mIsButterknifeAvailable)
      return;

    ButterKnife.bind(view);
  }
}
