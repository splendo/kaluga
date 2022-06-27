# Test Utils Bluetooth

This library adds support for testing the [`bluetooth` module](../bluetooth) to [`test-utils`](../test-utils-base)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Bluetooth as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils-bluetooth:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `Scanner`, `DeviceConnectionManager`, `AdvertisementData`, `Characteristic`, `Descriptor`, and `BluetoothMonitor`.
In addition use `createDeviceWrapper` and `createServiceWrapper` to generate mocked Device and Service wrappers.
