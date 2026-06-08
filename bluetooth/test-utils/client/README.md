# Test Utils Bluetooth

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

This library adds support for testing the [`client` module](../../client/) to [`test-utils`](../../../base/test-utils/)

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
    implementation("com.splendo.kaluga.bluetooth:test-client:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `Scanner`, `DeviceConnectionManager`, `AdvertisementData`, `Characteristic`, `Descriptor`, `BluetoothMonitor`, and `MockDevice`.
In addition use `createDeviceWrapper`, `createServiceWrapper` to generate mocked Device and Service wrappers.

### Using MockDevice

Create mock using `buildMockDevice`:

```kotlin
val device = buildMockDevice(coroutineContext) {
    identifier = identifierFromString("1234")!!
    manufacturerId = 0xf00d
    services {
        add(uuidFrom("2345"))
    }
    connectionDelay = 100.milliseconds
}
```

Simulate connection to the device after given delay (`connectionDelay`) and check connected state afterwards:

```kotlin
device.connect()
device.state.firstInstance<ConnectableDeviceState.Connected>()
```

Disconnect from the device:

```kotlin
device.disconnect()
device.state.firstInstance<ConnectableDeviceState.Disconnected>()
```
