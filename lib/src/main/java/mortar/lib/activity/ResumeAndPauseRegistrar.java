package mortar.lib.activity;

import mortar.MortarScope;

/**
 * Provides means to listen for {@link android.app.Activity#onPause()} and {@link
 * android.app.Activity#onResume()}.
 */
public interface ResumeAndPauseRegistrar {
  /**
   * <p>Registers a {@link ResumesAndPauses} client for the duration of the given {@link
   * MortarScope}. This method is debounced, redundant calls are safe.
   *
   * <p>Calls {@link ResumesAndPauses#onResume()} immediately if the host {@link
   * android.app.Activity} is currently running.
   */
  void register(MortarScope scope, ResumesAndPauses listener);

  /** Returns {@code true} if called between resume and pause. {@code false} otherwise. */
  boolean isRunning();
}