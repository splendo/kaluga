# Test Utils Date Time Picker

> **⚠️ Deprecated:** This feature has been deprecated. It is recommended to use Compose Multiplatform instead.

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  |  |  |  |

This library adds support for testing the [`date-time-picker` module](../date-time-picker/) to [`test-utils`](../../base/test-utils/)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Date Time Picker as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.date-time-picker:test:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `BaseDateTimePickerPresenter` and `BaseDateTimePickerPresenter.Builder`.
