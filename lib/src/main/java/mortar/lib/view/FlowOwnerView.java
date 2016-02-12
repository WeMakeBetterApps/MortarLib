package mortar.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import flow.Flow;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.lib.screen.AbstractScreenConductor;
import mortar.lib.screen.AnimatedScreenConductor;
import mortar.lib.screen.CanShowScreen;
import mortar.lib.screen.FlowOwner;

public abstract class FlowOwnerView extends FrameLayout implements CanShowScreen<Blueprint> {
  private AbstractScreenConductor<Blueprint> mScreenConductor;

  public FlowOwnerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnifeWrapper.get().inject(this);
    //noinspection unchecked
    mScreenConductor = createScreenConductor(getContext());
  }

  public Flow getFlow() {
    return getPresenter().getFlow();
  }

  protected abstract FlowOwner getPresenter();

  @Override public void showScreen(Blueprint screen, Flow.Direction direction) {
    mScreenConductor.showScreen(screen, direction);
  }

  protected AbstractScreenConductor createScreenConductor(Context context) {
    return new AnimatedScreenConductor(context, this);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    //noinspection unchecked
    getPresenter().takeView(this);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    //noinspection unchecked
    getPresenter().dropView(this);
  }
}