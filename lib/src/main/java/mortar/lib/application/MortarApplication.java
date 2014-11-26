package mortar.lib.application;

import android.app.Application;

import dagger.ObjectGraph;
import mortar.Mortar;
import mortar.MortarScope;
import mortar.lib.inject.Injector;

public abstract class MortarApplication extends Application {

  private MortarScope rootScope;

  @Override public void onCreate() {
    super.onCreate();

    ObjectGraph applicationGraph = ObjectGraph.create(getRootScopeModules());
    Injector.setApplicationGraph(applicationGraph);
    rootScope = Mortar.createRootScope(isBuildConfigDebug(), applicationGraph);
  }

  protected abstract Object[] getRootScopeModules();

  public abstract boolean isBuildConfigDebug();

  public MortarScope getRootScope() {
    return rootScope;
  }

}
