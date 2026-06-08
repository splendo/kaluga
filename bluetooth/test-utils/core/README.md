# Test Utils Bluetooth Base

This library adds support for testing the [`bluetooth-base` module](../bluetooth-base) to [`test-utils`](../test-utils-base).

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
    implementation("com.splendo.kaluga.bluetooth.test:core:$kalugaVersion")
}
```

## Mocks
This library contains `MockBluetoothMonitor` for mocking the Bluetooth service [`BluetoothMonitor`](../bluetooth-base), and a `randomUUID` helper for generating random [`UUID`](../bluetooth-base) values in tests.
