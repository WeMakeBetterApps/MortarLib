package mortar.lib.sample.screen;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.Parcer;
import mortar.Blueprint;
import mortar.lib.sample.activity.MainActivity;
import mortar.lib.sample.application.SampleModule;
import mortar.lib.screen.FlowOwner;
import view.ActivityView;

public class ActivityScreen implements Blueprint {

  public ActivityScreen() {
  }

  @Override public String getMortarScopeName() {
    return ActivityScreen.class.getName();
  }

  @Override public Object getDaggerModule() {
    return new Module();
  }

  @dagger.Module(
      addsTo = SampleModule.class,
      injects = {
          MainActivity.class,
          ActivityView.class,
      },
      library = true
  )
  public class Module {
    @Provides @Singleton Flow provideFlow(Presenter presenter) {
      return presenter.getFlow();
    }
  }

  @Singleton public static class Presenter extends FlowOwner<Blueprint, ActivityView> {
    @Inject Presenter(Parcer<Object> flowParcer) {
      super(flowParcer);
    }

    @Override protected Blueprint getFirstScreen() {
      return new SampleScreen1();
    }
  }

}

