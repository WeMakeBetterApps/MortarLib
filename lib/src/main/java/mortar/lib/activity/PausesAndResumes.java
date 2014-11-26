package mortar.lib.activity;

/**
 * <p>Implemented by objects that need to know when the {@link android.app.Activity} pauses
 * and resumes. Sign up for service via {@link PauseAndResumeRegistrar#register(mortar.MortarScope, PausesAndResumes)}.
 *
 * <p>Registered objects will also be subscribed to the only while the activity is running.
 */
public interface PausesAndResumes {
  void onResume();

  void onPause();
}
