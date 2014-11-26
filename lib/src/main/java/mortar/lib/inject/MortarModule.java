package mortar.lib.inject;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import flow.Parcer;
import mortar.lib.activity.PauseAndResumePresenter;
import mortar.lib.util.GsonParcer;

@Module(
    library = true
)
public class MortarModule {

  @Provides @Singleton Parcer<Object> provideParcer() {
    return new GsonParcer<Object>();
  }

  @Provides @Singleton public PauseAndResumePresenter providesPauseAndResumePresenter() {
    return new PauseAndResumePresenter();
  }

}
