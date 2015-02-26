package mortar.lib.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.ViewGroup;

import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarActivityScope;
import mortar.MortarScope;
import mortar.lib.application.MortarApplication;
import mortar.lib.view.FlowOwnerView;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.CATEGORY_LAUNCHER;

/**
 * Hooks up the {@link mortar.MortarActivityScope}. Loads the main view
 * and lets it know about up button and back button presses.
 */
public abstract class MortarActionBarActivity extends ActionBarActivity 
    implements ResumeAndPauseActivity {

  private MortarActivityScope mActivityScope;
  private final ResumeAndPauseOwner mResumeAndPauseOwner = new ResumeAndPauseOwner();
  private boolean mIsRunning = false;

  protected abstract Blueprint getNewActivityScreen();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Application application = getApplication();
    if ( !(application instanceof MortarApplication) ) {
      throw new RuntimeException("Application must be an instance of " + MortarApplication.class.getName());
    }
    //noinspection ConstantConditions
    MortarScope parentScope = ((MortarApplication) application).getRootScope();

    Blueprint activityScope = getNewActivityScreen();
    if (activityScope == null)
      throw new RuntimeException("Provided activity scope for "
          + MortarActivity.class.getSimpleName() + " cannot be null.");

    mActivityScope = Mortar.requireActivityScope(parentScope, activityScope);
    Mortar.inject(this, this);

    mActivityScope.onCreate(savedInstanceState);
    mResumeAndPauseOwner.takeView(this);
  }

  @Override protected void onResume() {
    super.onResume();
    mIsRunning = true;
    mResumeAndPauseOwner.activityResumed();
  }

  @Override protected void onPause() {
    mIsRunning = false;
    mResumeAndPauseOwner.activityPaused();
    super.onPause();
  }

  @Override protected void onDestroy() {
    mResumeAndPauseOwner.dropView(this);
    super.onDestroy();
  }

  @Override public ResumeAndPauseOwner getResumeAndPausePresenter() {
    return mResumeAndPauseOwner;
  }

  @Override public Object getSystemService(String name) {
    if (Mortar.isScopeSystemService(name)) {
      return mActivityScope;
    }
    return super.getSystemService(name);
  }

  @SuppressWarnings("NullableProblems")
  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mActivityScope.onSaveInstanceState(outState);
  }

  public MortarActivityScope getMortarScope() {
    return mActivityScope;
  }

  /**
   * Dev tools and the play store (and others?) launch with a different intent, and so
   * lead to a redundant instance of this activity being spawned. <a
   * href="http://stackoverflow.com/questions/17702202/find-out-whether-the-current-activity-will-be-task-root-eventually-after-pendin"
   * >Details</a>.
   */
  protected boolean isWrongInstance() {
    if (!isTaskRoot()) {
      Intent intent = getIntent();
      boolean isMainAction = intent.getAction() != null && intent.getAction().equals(ACTION_MAIN);
      return intent.hasCategory(CATEGORY_LAUNCHER) && isMainAction;
    }
    return false;
  }

  protected FlowOwnerView getMainView() {
    ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
    return (FlowOwnerView) root.getChildAt(0);
  }

  /** Inform the view about back events. */
  @Override public void onBackPressed() {
    // Give the view a chance to handle going back. If it declines the honor, let super do its thing.
    FlowOwnerView view = getMainView();
    if (!view.getFlow().goBack()) super.onBackPressed();
  }

  /** Inform the view about up events. */
  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      FlowOwnerView view = getMainView();
      return view.getFlow().goUp();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean isRunning() {
    return mIsRunning;
  }
}
