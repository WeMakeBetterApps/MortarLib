package mortar.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import mortar.Mortar;
import mortar.ViewPresenter;

public abstract class MortarView extends View {

  public MortarView(Context context) {
    super(context);
    Mortar.inject(context, this);
  }

  public MortarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Mortar.inject(context, this);
  }

  public MortarView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    Mortar.inject(context, this);
  }

  protected abstract ViewPresenter getPresenter();

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnifeWrapper.get().inject(this);
    //noinspection unchecked
    getPresenter().takeView(this);
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
