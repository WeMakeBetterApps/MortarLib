package mortar.lib.activity;

import java.util.LinkedHashSet;
import java.util.Set;

import mortar.MortarScope;
import mortar.Presenter;
import mortar.Scoped;

/**
 * Presenter to be registered by the {@link ResumeAndPauseActivity}.
 */
public class ResumeAndPauseOwner extends Presenter<ResumeAndPauseActivity>
    implements ResumeAndPauseRegistrar {

  private final Set<Registration> registrations = new LinkedHashSet<Registration>();

  public ResumeAndPauseOwner() {
  }

  @Override protected MortarScope extractScope(ResumeAndPauseActivity view) {
    return view.getMortarScope();
  }

  @Override public void onExitScope() {
    registrations.clear();
  }

  @Override public void register(MortarScope scope, ResumesAndPauses listener) {
    Registration registration = new Registration(listener);
    scope.register(registration);

    boolean added = registrations.add(registration);
    if (added && isRunning()) {
      listener.onResume();
    }
  }

  @Override public boolean isRunning() {
    return getView() != null && getView().isRunning();
  }

  public void activityPaused() {
    for (Registration registration : registrations) {
      registration.registrant.onPause();
    }
  }

  public void activityResumed() {
    for (Registration registration : registrations) {
      registration.registrant.onResume();
    }
  }

  private class Registration implements Scoped {
    final ResumesAndPauses registrant;

    private Registration(ResumesAndPauses registrant) {
      this.registrant = registrant;
    }

    @Override public void onEnterScope(MortarScope scope) {
    }

    @Override public void onExitScope() {
      if (registrant.isRunning())
        registrant.onPause();
      registrations.remove(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Registration that = (Registration) o;

      return registrant.equals(that.registrant);
    }

    @Override
    public int hashCode() {
      return registrant.hashCode();
    }
  }
}