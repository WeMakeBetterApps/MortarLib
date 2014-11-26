package mortar.lib.inject;

import dagger.ObjectGraph;

public class Injector {

  private static ObjectGraph mApplicationGraph;

  public static <T> T get(Class<T> clazz) {
    return mApplicationGraph.get(clazz);
  }

  public static void inject(Object obj) {
    mApplicationGraph.inject(obj);
  }

  public static ObjectGraph getApplicationGraph() {
    return mApplicationGraph;
  }

  public static void setApplicationGraph(ObjectGraph applicationGraph) {
    mApplicationGraph = applicationGraph;
  }

}