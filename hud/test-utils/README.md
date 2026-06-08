# Test Utils HUD

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  |  |  |  |

This library adds support for testing the [`hud` module](../hud) to [`test-utils`](../../base/test-utils/)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils HUD as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.hud:test:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `BaseHUD` and `BaseHUD.Builder`.
