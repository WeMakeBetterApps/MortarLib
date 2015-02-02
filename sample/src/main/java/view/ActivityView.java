package view;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import flow.Flow;
import mortar.Blueprint;
import mortar.lib.sample.screen.ActivityScreen;
import mortar.lib.screen.AbstractScreenConductor;
import mortar.lib.screen.AnimatedScreenConductor;
import mortar.lib.screen.CanShowScreen;
import mortar.lib.screen.FlowOwner;
import mortar.lib.view.FlowOwnerView;

public class ActivityView extends FlowOwnerView implements CanShowScreen<Blueprint> {

  @Inject ActivityScreen.Presenter mPresenter;

  public ActivityView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected FlowOwner getPresenter() {
    return mPresenter;
  }

  @Override public void showScreen(Blueprint screen, Flow.Direction direction) {
    super.showScreen(screen, direction);
  }

  @Override protected AbstractScreenConductor createScreenConductor(Context context) {
    return new AnimatedScreenConductor(context, ActivityView.this);
  }

}