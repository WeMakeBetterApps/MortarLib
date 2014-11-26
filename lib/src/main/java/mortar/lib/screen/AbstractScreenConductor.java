package mortar.lib.screen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarScope;


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
    MortarScope myScope = Mortar.getScope(mContext);
    MortarScope newChildScope = myScope.requireChild(screen);

    View oldChild = getChildView();
    View newChild;

    if (oldChild != null) {
      MortarScope oldChildScope = Mortar.getScope(oldChild.getContext());
      if (oldChildScope.getName().equals(screen.getMortarScopeName())) {
        // If it's already showing, short circuit.
        return;
      }

      myScope.destroyChild(oldChildScope);
    }

    int layoutId;
    {
      Class<?> screenType = screen.getClass();
      Layout screenLayoutAnnotation = screenType.getAnnotation(Layout.class);
      if (screenLayoutAnnotation == null) {
        throw new IllegalArgumentException(
            String.format("@%s annotation not found on class %s", Layout.class.getSimpleName(),
                screenType.getName()));
      }
      layoutId = screenLayoutAnnotation.value();
    }

    // Create the new child.
    Context childContext = newChildScope.createContext(mContext);
    inflateAndAddScreen(childContext, direction, layoutId, oldChild, mContainer);
  }

  protected abstract void inflateAndAddScreen(Context context, Flow.Direction direction, int layoutId,
                                              View oldChild, ViewGroup container);

  private View getChildView() {
    return mContainer.getChildAt(0);
  }
}
