package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import javax.inject.Inject;

import butterknife.OnClick;
import mortar.ViewPresenter;
import mortar.lib.sample.R;
import mortar.lib.sample.screen.InnerScreen;
import mortar.lib.sample.screen.SampleScreen1;
import mortar.lib.util.MortarUtil;
import mortar.lib.view.MortarLinearLayout;

public class SampleView1 extends MortarLinearLayout {

  @Inject SampleScreen1.Presenter1 mPresenter;
  
  private View innerView;
  
  public SampleView1(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected ViewPresenter getPresenter() {
    return mPresenter;
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    innerView = MortarUtil.createScreen(getContext(), new InnerScreen());
    addView(innerView);
  }
  
  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    removeView(innerView);
    MortarUtil.destroyScreen(getContext(), innerView);
    innerView = null;
  }

  @OnClick(R.id.toScreen2) public void onToScreen2Click() {
    mPresenter.goToScreen2();
  }
  
}
