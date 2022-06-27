# Test Utils Keyboard

This library adds support for testing the [`keyboard` module](../keyboard) to [`test-utils`](../test-utils-base)

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
    implementation("com.splendo.kaluga:test-utils-keyboard:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `KeyboardManager`, `KeyboardManager.Builder`, and `FocusHandler`.
