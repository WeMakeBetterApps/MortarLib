MortarLib
=========

Supports building Android apps using Square's Flow and Mortar.

APIs aren't finalized yet. Flow and Mortar are still in rapid development, and as such still is this. As I use Flow and Mortar, if I find the need to write a component that is a potential common use case it will be added to this library. Pull requests are welcome.

<hr />

Currently supports Flow 0.7, and Mortar 0.16

Gradle Usage:
```groovy
compile 'com.wemakebetterapps:mortar-lib:1.2.0'
compile 'com.squareup.flow:flow:0.7'
compile 'com.squareup.mortar:mortar:0.16'
```

### Screen Generator Sample

Creating a new screen using Flow & Mortar is an effort unto itself, so included is a Screen Generator. It will generate your Screen, Presenter, Module, view, and xml file with all the necessary boiler plate. It's recommended that you set this up as a separate java project.

```groovy
compile 'com.wemakebetterapps:mortar-gen:1.1.0'
```

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
            .defaultBaseModule("com.example.app.screen", "MainScreen.Module")
            .presenterPackage("com.example.app.presenter");

    new ScreenGenerator(prefs).start();
  }

}
```
