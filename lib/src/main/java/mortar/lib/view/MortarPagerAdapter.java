package mortar.lib.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import mortar.Blueprint;
import mortar.lib.util.MortarUtil;

public abstract class MortarPagerAdapter extends PagerAdapter {

  public abstract Blueprint getScreen(int position);

  public abstract Context getContext();

  @Override public boolean isViewFromObject(View view, Object o) {
    return view == o;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    Blueprint blueprint = getScreen(position);
    View child = MortarUtil.createScreen(getContext(), blueprint);
    container.addView(child);
    return child;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    View child = (View) object;
    MortarUtil.destroyScreen(getContext(), child);
    container.removeView(child);
  }

}
