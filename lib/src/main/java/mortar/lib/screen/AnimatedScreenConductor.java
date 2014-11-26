package mortar.lib.screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import flow.Flow;
import mortar.Blueprint;

public class AnimatedScreenConductor<S extends Blueprint> extends AbstractScreenConductor<S> {

  /**
   * @param context
   * @param container the container used to host child views. Typically this is a {@link
   *                  android.widget.FrameLayout} under the action bar.
   */
  public AnimatedScreenConductor(Context context, ViewGroup container) {
    super(context, container);
  }

  @Override
  protected void inflateAndAddScreen(Context context, Flow.Direction direction, int layoutId, View oldChild, ViewGroup container) {
    View newChild = LayoutInflater.from(context).inflate(layoutId, container, false);

    setAnimation(context, direction, oldChild, newChild);

    // Out with the old, in with the new.
    if (oldChild != null) container.removeView(oldChild);
    container.addView(newChild);
  }

  private void setAnimation(Context context, Flow.Direction direction, View oldChild, View newChild) {
    if (oldChild == null) return;

    Animation out = getOutAnimation(context, direction, oldChild, newChild);
    Animation in = getInAnimation(context, direction, oldChild, newChild);

    oldChild.setAnimation(out);
    newChild.setAnimation(in);
  }

  protected Animation getInAnimation(Context context, Flow.Direction direction, View oldChild,
                                      View newChild) {
    return AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
  }

  protected Animation getOutAnimation(Context context, Flow.Direction direction, View oldChild,
                                      View newChild) {
    return AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
  }

}
