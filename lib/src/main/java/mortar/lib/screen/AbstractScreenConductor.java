package mortar.lib.screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import flow.Flow;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarScope;
import mortar.lib.util.MortarUtil;


/**
 * A conductor that can swap subviews within a container view.
 * <p/>
 *
 * @param <S> the type of the screens that serve as a {@link Blueprint} for subview. Must
 * be annotated with {@link flow.Layout}, suitable for use with {@link flow.Layouts#createView}.
 */
public abstract class AbstractScreenConductor<S extends Blueprint> implements CanShowScreen<S> {

  private final Context mContext;
  private final ViewGroup mContainer;

  /**
   * @param container the container used to host child views. Typically this is a {@link
   * android.widget.FrameLayout} under the action bar.
   */
  public AbstractScreenConductor(Context context, ViewGroup container) {
    this.mContext = context;
    this.mContainer = container;
  }

  public void showScreen(S screen, Flow.Direction direction) {
    MortarScope parentScope = Mortar.getScope(mContext);
    MortarScope newChildScope = parentScope.requireChild(screen);

    View oldChild = getChildView();

    if (oldChild != null) {
      MortarScope oldChildScope = Mortar.getScope(oldChild.getContext());
      if (oldChildScope.getName().equals(screen.getMortarScopeName())) {
        // If it's already showing, short circuit.
        return;
      }

      parentScope.destroyChild(oldChildScope);
    }

    // Create the new child.
    Context childContext = newChildScope.createContext(mContext);
    View newView = inflateScreen(childContext, screen, mContainer);
    animateAndAddScreen(childContext, direction, oldChild, newView, mContainer);
  }

  protected View inflateScreen(Context context, S screen, ViewGroup container) {
    int layoutId = MortarUtil.getLayoutId(screen);
    return LayoutInflater.from(context).inflate(layoutId, container, false);
  }

  protected abstract void animateAndAddScreen(Context context, Flow.Direction direction,
                                              View oldView, View newView, ViewGroup container);

  private View getChildView() {
    return mContainer.getChildAt(0);
  }
}
