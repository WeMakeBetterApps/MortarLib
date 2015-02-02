package mortar.lib.activity;

/**
 * <p>Implemented by objects that need to know when the {@link android.app.Activity} pauses
 * and resumes. Sign up for service via {@link ResumeAndPauseRegistrar#register(mortar.MortarScope, ResumesAndPauses)}.
 *
 * <p>Registered objects will also be subscribed to the only while the activity is running.
 */
public interface ResumesAndPauses {
  void onResume();
  void onPause();

  /**
   * @return true if is running, meaning has been resumed, and has not been paused yet; false otherwise
   */
  boolean isRunning();
}
