package mortar.lib.activity;

import mortar.MortarScope;

/**
 * Implemented by {@link android.app.Activity} instances whose pause / resume state
 * is to be shared. The activity must call {@link PauseAndResumePresenter#activityPaused()}
 * and {@link PauseAndResumePresenter#activityResumed()} at the obvious times.
 */
public interface PauseAndResumeActivity {
  boolean isRunning();

  MortarScope getMortarScope();
}
