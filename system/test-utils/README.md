# Test Utils System

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ |  |  |

This library adds support for testing the [`system` module](../system) to [`test-utils`](../../base/test-utils/)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Resources as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.system:test:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `NetworkManager`, and `BaseNetworkStateRepoBuilder`
