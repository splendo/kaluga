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

Simulate connection to the device after given delay (`connectionDelay`) and check connected state afterwords:

```kotlin
device.connect()
device.state.firstInstance<ConnectableDeviceState.Connected>()
```

Disconnect from the device:

```kotlin
device.disconnect()
device.state.firstInstance<ConnectableDeviceState.Disconnected>()
```
