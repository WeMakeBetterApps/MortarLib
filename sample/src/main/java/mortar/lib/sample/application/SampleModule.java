package mortar.lib.sample.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import flow.Parcer;
import mortar.lib.sample.activity.MainActivity;
import mortar.lib.util.GsonParcer;

@Module(
    injects = {
        MainActivity.class
    },
    library = true
)
public class SampleModule {
  
  private SampleApplication mApplication;
  
  public SampleModule(SampleApplication application) {
    mApplication = application;
  }
  
  @Provides @Singleton Gson provideGson() {
    return new GsonBuilder()
        .create();
  }

  @Provides @Singleton Parcer<Object> provideParcer(Gson gson) {
    return new GsonParcer<Object>(gson);
  }
}
