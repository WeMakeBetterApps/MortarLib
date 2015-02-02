package mortar.lib.sample.application;

import mortar.lib.application.MortarApplication;
import mortar.lib.sample.BuildConfig;

public class SampleApplication extends MortarApplication {
  @Override protected Object[] getRootScopeModules() {
    return new Object[] {new SampleModule(this)};
  }

  @Override public boolean isBuildConfigDebug() {
    return BuildConfig.DEBUG;
  }
}
