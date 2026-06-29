# Test Utils Keyboard

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  |  |  |  |

This library adds support for testing the [`keyboard` module](../keyboard) to [`test-utils`](../../base/test-utils)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Keyboard as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.keyboard:test:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `KeyboardManager`, `KeyboardManager.Builder`, and `FocusHandler`.
