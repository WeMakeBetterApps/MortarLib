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
import mortar.lib.view.MortarFrameLayout;

public class ${viewFileName} extends MortarFrameLayout {

  <#if isPresenterSeparateFile>
  @Inject ${presenterFileName} mPresenter;
  <#else>
  @Inject ${screenFileName}.${presenterFileName} mPresenter;
  </#if>

  public ${viewFileName}(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected ViewPresenter getPresenter() {
    return mPresenter;
  }

}