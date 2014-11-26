package mortar.lib.util;

import android.content.Context;
import android.view.View;

import flow.Layouts;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarScope;

public class MortarUtil {

  public static View createScreen(Context context, Blueprint screen) {
    MortarScope myScope = Mortar.getScope(context);
    MortarScope newChildScope = myScope.requireChild(screen);
    Context childContext = newChildScope.createContext(context);
    return Layouts.createView(childContext, screen);
  }

  public static void destroyScreen(View view) {
    destroyScreen(view.getContext().getApplicationContext(), view);
  }

  public static void destroyScreen(Context parentContext, View view) {
    MortarScope parentScope = Mortar.getScope(parentContext);
    MortarScope oldChildScope = Mortar.getScope(view.getContext());
    parentScope.destroyChild(oldChildScope);
  }

}
