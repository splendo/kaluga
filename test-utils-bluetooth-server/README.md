# Test Utils Bluetooth Server

This library adds support for testing the [`bluetooth-server` module](../bluetooth-server) to [`test-utils`](../test-utils-base).

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Bluetooth Server as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils-bluetooth-server:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for the Bluetooth Server: `MockBluetoothServer` and `MockBluetoothServerBuilder` for the [`BluetoothServer`](../bluetooth-server), `MockConnectedDevice` for connected centrals, and the local attribute wrappers `MockLocalServiceWrapper`, `MockLocalCharacteristicWrapper` and `MockLocalDescriptorWrapper` (built through `MockLocalServiceWrapperBuilder`).
