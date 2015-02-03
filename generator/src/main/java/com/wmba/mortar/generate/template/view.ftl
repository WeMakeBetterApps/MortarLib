package ${viewPackage};

import android.content.Context;
import android.util.AttributeSet;

<#if isPresenterSeparateFile>
import ${presenterPackage}.${presenterFileName};
<#else>
import ${screenPackage}.${screenFileName};
</#if>

import javax.inject.Inject;

import mortar.ViewPresenter;
import mortar.lib.view.MortarLinearLayout;

public class ${viewFileName} extends MortarLinearLayout {

  <#if isPresenterSeparateFile>
  @Inject ${presenterFileName} presenter;
  <#else>
  @Inject ${screenFileName}.${presenterFileName} presenter;
  </#if>

  public ${viewFileName}(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected ViewPresenter getPresenter() {
    return presenter;
  }

}