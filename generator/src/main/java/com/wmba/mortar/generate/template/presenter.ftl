package ${presenterPackage};

import android.os.Bundle;

<#if customPresenterClass??>
import ${customPresenterClass.getName()};
</#if>
import ${viewPackage}.${viewFileName};

import javax.inject.Inject;

<#if !customPresenterClass??>
import mortar.ViewPresenter;
</#if>

<#if !isPresenterSeparateFile>
@Singleton
</#if>
public class ${presenterFileName} extends <#if customPresenterClass??>${customPresenterClass.getSimpleName()}<#else>ViewPresenter</#if><${viewFileName}> {

  @Inject public ${presenterFileName}() {
  }

  @Override protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
  }

}