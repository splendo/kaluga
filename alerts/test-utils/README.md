# Test Utils Alerts

> **⚠️ Deprecated:** This feature has been deprecated. It is recommended to use Compose Multiplatform instead.

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  |  |  |  |

This library adds support for testing the [`alerts` module](../alerts/) to [`test-utils`](../../base/test-utils/)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Alerts as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.alerts:test:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `BaseAlertPresenter` and `BaseAlertPresenter.Builder`
