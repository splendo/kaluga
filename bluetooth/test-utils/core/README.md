# Test Utils Bluetooth Base

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

This library adds support for testing the [`core` module](../../core/) to [`test-utils`](../../../base/test-utils/).

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Bluetooth Base as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bluetooth:test-core:$kalugaVersion")
}
```

## Mocks
This library contains `MockBluetoothMonitor` for mocking the Bluetooth service [`BluetoothMonitor`](../../core/), and a `randomUUID` helper for generating random [`UUID`](../../core/) values in tests.
