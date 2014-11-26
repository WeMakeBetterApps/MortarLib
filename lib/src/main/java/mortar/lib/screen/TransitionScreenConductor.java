package mortar.lib.screen;

import android.content.Context;
import android.support.transition.Scene;
import android.support.transition.TransitionInflater;
import android.support.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import flow.Flow;
import mortar.Blueprint;

public class TransitionScreenConductor<S extends Blueprint> extends AnimatedScreenConductor<S> {

  private TransitionManager mTransitionManager;

  /**
   * @param context
   * @param container the container used to host child views. Typically this is a {@link
   *                  android.widget.FrameLayout} under the action bar.
   * @param transitionManagerId the id for the transition manager to be used.
   */
  public TransitionScreenConductor(Context context, ViewGroup container, int transitionManagerId) {
    super(context, container);

    TransitionInflater inflater = TransitionInflater.from(context);
    mTransitionManager = inflater.inflateTransitionManager(transitionManagerId, container);
  }

  @Override
  protected void inflateAndAddScreen(Context context, Flow.Direction direction, int layoutId,
                                     View oldChild, ViewGroup container) {

    if (!useTransition(direction, layoutId, oldChild)) {
      super.inflateAndAddScreen(context, direction, layoutId, oldChild, container);
      return;
    }

    Scene scene = Scene.getSceneForLayout(container, layoutId, context);
    mTransitionManager.transitionTo(scene);
  }

  protected boolean useTransition(Flow.Direction direction, int layoutId, View oldChild) {
    return true;
  }

  protected TransitionManager getTransitionManager() {
    return mTransitionManager;
  }

}
