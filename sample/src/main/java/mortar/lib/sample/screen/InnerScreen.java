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
import view.InnerView;
import view.SampleView1;

@Layout(R.layout.screen_inner)
public class InnerScreen implements Blueprint {
  @Override public String getMortarScopeName() {
    return InnerScreen.class.getName();
  }

  @Override public Object getDaggerModule() {
    return new Module();
  }
  
  @dagger.Module(
      addsTo = SampleScreen1.Module.class,
      injects = {
          InnerPresenter.class,
          InnerView.class
      },
      library = true
  )
  class Module {
    
  }
  
  @Singleton public static class InnerPresenter extends ResumeAndPausePresenter<SampleView1> {

    private static final String TAG = InnerPresenter.class.getSimpleName();
    
    @Inject Flow mFlow;
    
    @Inject InnerPresenter() {}

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

  }
  
}
