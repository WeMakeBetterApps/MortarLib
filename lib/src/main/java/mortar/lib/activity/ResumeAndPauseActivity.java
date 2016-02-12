package mortar.lib.activity;

import mortar.MortarScope;

/**
 * Implemented by {@link android.app.Activity} instances whose pause / resume state
 * is to be shared. The activity must call {@link ResumeAndPauseOwner#activityPaused()}
 * and {@link ResumeAndPauseOwner#activityResumed()} at the obvious times.
 */
public interface ResumeAndPauseActivity {
  boolean isRunning();
  MortarScope getMortarScope();
  ResumeAndPauseOwner getResumeAndPausePresenter();
}
