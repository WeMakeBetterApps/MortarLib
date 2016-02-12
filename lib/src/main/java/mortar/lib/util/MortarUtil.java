package mortar.lib.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import flow.Layout;
import flow.Layouts;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarScope;

public class MortarUtil {

  public static View createScreen(Context context, Blueprint screen) {
    MortarScope scope = Mortar.getScope(context);
    MortarScope newChildScope = scope.requireChild(screen);

    Context childContext = newChildScope.createContext(context);
    return Layouts.createView(childContext, screen);
  }

  public static int getLayoutId(Blueprint screen) {
    Class<? extends Blueprint> screenType = screen.getClass();
    Layout layoutAnnotation = screenType.getAnnotation(Layout.class);
    if (layoutAnnotation == null) {
      throw new IllegalArgumentException(String.format("@%s annotation not found on class %s",
          Layout.class.getSimpleName(), screenType.getName()));
    }

    return layoutAnnotation.value();
  }

  public static void destroyScreen(View view) {
    destroyScreen(view.getContext().getApplicationContext(), view);
  }

  public static void destroyScreen(Context parentContext, View view) {
    MortarScope parentScope = Mortar.getScope(parentContext);
    MortarScope oldChildScope = Mortar.getScope(view.getContext());
    parentScope.destroyChild(oldChildScope);
  }

  public static Activity getActivity(Context context) {
    Context currentContext = context;
    while (currentContext instanceof ContextWrapper) {
      currentContext = ((ContextWrapper) currentContext).getBaseContext();

      if (currentContext instanceof Activity) {
        return (Activity) currentContext;
      } else if (currentContext instanceof Application) {
        return null;
      }
    }

    return null;
  }

}
