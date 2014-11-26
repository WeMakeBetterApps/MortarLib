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

  private final AbstractScreenConductor<Blueprint> mScreenConductor;

  public FlowOwnerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Mortar.inject(context, this);
    mScreenConductor = createScreenConductor(context);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    //noinspection unchecked
    getPresenter().takeView(this);
  }

  public Flow getFlow() {
    return getPresenter().getFlow();
  }

  protected abstract FlowOwner getPresenter();

  @Override
  public void showScreen(Blueprint screen, Flow.Direction direction) {
    mScreenConductor.showScreen(screen, direction);
  }

  protected AbstractScreenConductor createScreenConductor(Context context) {
    return new AnimatedScreenConductor(context, this);
  }

}