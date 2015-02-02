package view;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import butterknife.OnClick;
import mortar.ViewPresenter;
import mortar.lib.sample.R;
import mortar.lib.sample.screen.SampleScreen2;
import mortar.lib.view.MortarLinearLayout;

public class SampleView2 extends MortarLinearLayout {

  @Inject SampleScreen2.Presenter2 mPresenter;

  public SampleView2(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected ViewPresenter getPresenter() {
    return mPresenter;
  }
  
  @OnClick(R.id.toScreen1) public void onToScreen1Click() {
    mPresenter.goToScreen1();
  }
  
}
