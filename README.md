MortarLib
=========

Supports building Android apps using Square's Flow and Mortar.

Currently supports Flow 0.7, and Mortar 0.16

Gradle Usage:
```groovy
compile 'com.wemakebetterapps:mortar-lib:1.0.0'
compile 'com.squareup.flow:flow:0.7'
compile 'com.squareup.mortar:mortar:0.16'
```

### Screen Generator Sample

Creating a new screen using Flow & Mortar is an effort unto itself, so included is a Screen Generator. It's recommended that you set this up as a separate java project.

```java
public class ExampleScreenGenerator {

  public static void main(String[] args) {
    ScreenGenerator.ScreenGeneratorPrefs prefs =
        new ScreenGenerator.ScreenGeneratorPrefs(
            new File("app/src/main/java"),
            new File("app/src/main/res/layout"),
            "com.example.app",
            "com.example.app.screen",
            "view"
        ).customPresenter("com.example.app.presenter", "BasePresenter")
            .defaultBaseModule("com.example.app.screen", "ActivityScreen.Module")
            .presenterPackage("com.example.app.presenter");

    new ScreenGenerator(prefs).start();
  }

}
```
