package ${screenPackage};

<#if !isPresenterSeparateFile>
import android.os.Bundle;
</#if>

import ${androidAppPackage}.R;
<#if defaultBaseModuleClass??>
import ${defaultBaseModuleClass.getName()};
</#if>
<#if isPresenterSeparateFile>
import ${presenterPackage}.${presenterFileName};
<#else>
<#if customPresenterClass??>
import ${customPresenterClass.getName()};
</#if>
</#if>
import ${viewPackage}.${viewFileName};

<#if !isPresenterSeparateFile>
import javax.inject.Inject;
</#if>
import javax.inject.Singleton;

<#if isPresenterSeparateFile>
import dagger.Provides;
</#if>
import flow.Layout;
import mortar.Blueprint;
<#if !isPresenterSeparateFile && !customPresenterClass??>
import mortar.ViewPresenter;
</#if>

@Layout(R.layout.${layoutXMLFileName})
public class ${screenFileName} implements Blueprint {

  @Override public String getMortarScopeName() {
    return getClass().getName();
  }

  @Override public Object getDaggerModule() {
    <#if isPresenterSeparateFile>
    return new Module(new ${presenterFileName}());
    <#else>
    return new Module();
    </#if>
  }

  @dagger.Module(
      addsTo = <#if defaultBaseModuleClass??>${defaultBaseModuleClass.getSimpleName()}.class</#if>,
      injects = {
          <#if isPresenterSeparateFile>
          ${presenterFileName}.class,
          <#else>
          Presenter.class,
          </#if>
          ${viewFileName}.class
      }
  )
  static class Module {
    <#if isPresenterSeparateFile>

    private final ${presenterFileName} mPresenter;

    public Module(${presenterFileName} presenter) {
      this.mPresenter = presenter;
    }

    @Provides @Singleton public ${presenterFileName} providesPresenter() {
      return mPresenter;
    }

    </#if>
  }

  <#if !isPresenterSeparateFile>
  @Singleton
  public static class ${presenterFileName} extends <#if customPresenterClass??>${customPresenterClass.getSimpleName()}<#else>ViewPresenter</#if><${viewFileName}> {

    @Inject public ${presenterFileName}() {
    }

    @Override protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
    }

  }
  </#if>

}