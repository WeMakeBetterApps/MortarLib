package mortar.lib.sample.screen;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.MortarScope;
import mortar.lib.presenter.ResumeAndPausePresenter;
import mortar.lib.sample.R;
import view.SampleView1;

@Layout(R.layout.screen_sample1)
public class SampleScreen1 implements Blueprint {
  @Override public String getMortarScopeName() {
    return SampleScreen1.class.getName();
  }

  @Override public Object getDaggerModule() {
    return new Module();
  }
  
  @dagger.Module(
      addsTo = ActivityScreen.Module.class,
      injects = {
          Presenter1.class,
          SampleView1.class
      },
      library = true
  )
  class Module {
    
  }
  
  @Singleton public static class Presenter1 extends ResumeAndPausePresenter<SampleView1> {

    private static final String TAG = Presenter1.class.getSimpleName();
    
    @Inject Flow mFlow;
    
    @Inject Presenter1() {}

    @Override protected void onEnterScope(MortarScope scope) {
      super.onEnterScope(scope);
      Log.d(TAG, String.format("onEnterScope, getView()==null: %b", getView() == null));
    }

    @Override protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
      Log.d(TAG, String.format("onLoad, getView()==null: %b", getView() == null));
    }

    @Override public void onResume() {
      super.onResume();
      Log.d(TAG, String.format("onResume, getView()==null: %b", getView() == null));
    }

    @Override public void onPause() {
      super.onPause();
      Log.d(TAG, String.format("onPause, getView()==null: %b", getView() == null));
    }

    @Override protected void onSave(Bundle outState) {
      super.onSave(outState);
      Log.d(TAG, String.format("onSave, getView()==null: %b", getView() == null));
    }

    @Override protected void onExitScope() {
      super.onExitScope();
      Log.d(TAG, String.format("onExitScope, getView()==null: %b", getView() == null));
    }

    public void goToScreen2() {
      mFlow.goTo(new SampleScreen2());
    }
  }
  
}
