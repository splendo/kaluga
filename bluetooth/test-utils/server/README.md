# Test Utils Bluetooth Server

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  | ✅ |  |  |

This library adds support for testing the [`server` module](../../server/) to [`test-utils`](../../../base/test-utils/).

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
    implementation("com.splendo.kaluga.bluetooth:test-server:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for the Bluetooth Server: `MockBluetoothServer` and `MockBluetoothServerBuilder` for the [`BluetoothServer`](../../server/), `MockConnectedDevice` for connected centrals, and the local attribute wrappers `MockLocalServiceWrapper`, `MockLocalCharacteristicWrapper` and `MockLocalDescriptorWrapper` (built through `MockLocalServiceWrapperBuilder`).
