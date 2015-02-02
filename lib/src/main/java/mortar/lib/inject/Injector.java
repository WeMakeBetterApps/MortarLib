package mortar.lib.inject;

import android.content.Context;

import dagger.ObjectGraph;
import mortar.Mortar;
import mortar.MortarScope;

public class Injector {

  private static ObjectGraph mApplicationGraph;

  public static <T> T get(Class<T> clazz) {
    return mApplicationGraph.get(clazz);
  }

  public static void inject(Object obj) {
    mApplicationGraph.inject(obj);
  }

  public static <T> T get(Context context, Class<T> clazz) {
    MortarScope scope = Mortar.getScope(context);
    ObjectGraph objectGraph = scope.getObjectGraph();
    return objectGraph.get(clazz);
  }

  public static ObjectGraph getApplicationGraph() {
    return mApplicationGraph;
  }

  public static void setApplicationGraph(ObjectGraph applicationGraph) {
    mApplicationGraph = applicationGraph;
  }

}