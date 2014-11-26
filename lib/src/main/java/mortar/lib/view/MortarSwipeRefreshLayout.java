package mortar.lib.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import mortar.Mortar;
import mortar.ViewPresenter;

public abstract class MortarSwipeRefreshLayout extends SwipeRefreshLayout {

  public MortarSwipeRefreshLayout(Context context) {
    super(context);
    Mortar.inject(context, this);
  }

  public MortarSwipeRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    Mortar.inject(context, this);
  }

  protected abstract ViewPresenter getPresenter();

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnifeWrapper.get().inject(this);
    //noinspection unchecked
    getPresenter().takeView(this);
  }

  @Override public void onAttachedToWindow() {
    super.onAttachedToWindow();
    //noinspection unchecked
    getPresenter().takeView(this);
  }

  @Override public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    //noinspection unchecked
    getPresenter().dropView(this);
  }

}
