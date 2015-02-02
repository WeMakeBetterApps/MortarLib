package mortar.lib.presenter;

import android.app.Activity;
import android.view.View;

import mortar.MortarScope;
import mortar.ViewPresenter;
import mortar.lib.activity.ResumeAndPauseActivity;
import mortar.lib.activity.ResumeAndPauseOwner;
import mortar.lib.activity.ResumesAndPauses;
import mortar.lib.util.MortarUtil;

public abstract class ResumeAndPausePresenter<V extends View> extends ViewPresenter<V> 
    implements ResumesAndPauses {

  private ResumeAndPauseOwner mResumeAndPauseOwner = null;
  private boolean mIsRunning = false;
  
  @Override protected void onEnterScope(MortarScope scope) {
    super.onEnterScope(scope);
    
    Activity activity = MortarUtil.getActivity(getView().getContext());
    if (activity instanceof ResumeAndPauseActivity) {
      mResumeAndPauseOwner = ((ResumeAndPauseActivity) activity).getResumeAndPausePresenter();
    } else {
      throw new RuntimeException(String.format("Cannot use %s inside an activity that doesn't " +
          "implement %s.", ResumeAndPausePresenter.class.getSimpleName(), 
          ResumeAndPauseActivity.class.getSimpleName()));
    }
    
    mResumeAndPauseOwner.register(scope, this);
  }

  @Override public void onResume() {
    mIsRunning = true;
  }

  @Override public void onPause() {
    mIsRunning = false;
  }

  @Override protected void onExitScope() {
    if (mIsRunning) {
      // Mortar keeps it's scoped in a HashMap which doesn't keep order.
      // Without this sometimes onPause would run after onExitScope, and other times before.
      onPause();
    }
    super.onExitScope();
  }

  @Override public boolean isRunning() {
    return mIsRunning;
  }
}
