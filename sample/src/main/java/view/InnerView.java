package view;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import mortar.ViewPresenter;
import mortar.lib.sample.screen.InnerScreen;
import mortar.lib.view.MortarLinearLayout;

public class InnerView extends MortarLinearLayout {

  @Inject InnerScreen.InnerPresenter mPresenter;

  public InnerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected ViewPresenter getPresenter() {
    return mPresenter;
  }
  
}
