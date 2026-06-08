# Test Utils Location

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

This library adds support for testing the [`location` module](../location) to [`test-utils`](../../base/test-utils/)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Location as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.location:test:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `BaseLocationStateRepoBuilder`, `BaseLocationManager`, `BaseLocationManager.Builder`, and `LocationMonitor`.
