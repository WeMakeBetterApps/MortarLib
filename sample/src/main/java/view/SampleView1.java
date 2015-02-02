package view;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import butterknife.OnClick;
import mortar.ViewPresenter;
import mortar.lib.sample.R;
import mortar.lib.sample.screen.SampleScreen1;
import mortar.lib.view.MortarLinearLayout;

public class SampleView1 extends MortarLinearLayout {

  @Inject SampleScreen1.Presenter1 mPresenter;
  
  public SampleView1(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected ViewPresenter getPresenter() {
    return mPresenter;
  }

  @OnClick(R.id.toScreen2) public void onToScreen2Click() {
    mPresenter.goToScreen2();
  }
  
}
