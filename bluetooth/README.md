# Bluetooth

This library provides support for out-of-the-box access to Bluetooth.

## Installing
This library is available on Maven Central. You can import Kaluga Bluetooth as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:bluetooth:$kalugaVersion")
}
```

## Usage
### Client
Create a `Bluetooth` object through the `BluetoothBuilder`. This gives you access to a `Flow` of Bluetooth devices. To scan for devices simply call

```kotlin
bluetooth.startScanning(setOf(someUUID))
launch {
    bluetooth.devices().collect {}
}
///
bluetooth.stopScanning()
```

Bluetooth Scanning is managed by a state machine that will keep running as long as it is observed. It will automatically handle Permissions and Enabling/Disabling bluetooth on the device, although this behaviour can be disabled via the `BluetoothBuilder`
Devices are returned as a flow of `Device` objects, which manages the connection state of each device.

Devices can be grabbed via a getter method. This returns a flow on which the device can be connected/disconnected from. Services, Characteristics and Descriptors can be accessed via easy accessor methods.

```kotlin
launch {
    bluetooth.devices()[someUUID].advertisement().collect { advertisementData ->
        // handle Advertisement Data
    }   
}
// Connect to a device
bluetooth.devices()[someUUID].connect()

launch {
    bluetooth.devices()[someUUID].services()[serviceUUID].characteristics()[characteristicUUID].descriptors()[descriptorUUID].value().collect {
        // Observe value changes
    }
}

// Write data
bluetooth.devices()[someUUID].services()[serviceUUID].characteristics()[characteristicUUID].first().writeValue(newValue)

bluetooth.devices()[someUUID].disconnect()
```

### Android
When using automatic permissions by default only the relevant Bluetooth permissions are asked for, and not the location permission (unless the Android version is lower than 12 where it is always required). Make sure you include `android:usesPermissionFlags="neverForLocation"`, unless you do use Bluetooth to determine location, in which case you can use the `useLocation` flag in `BaseScanner.Settings`.


## Setup
In order to setup a bluetooth repo you need to create a `Bluetooth` object using a `BluetoothBuilder`.

```kotlin
class CommonViewModel(bltBuilder: BluetoothBuilder) : BaseLifecycleViewModel() {
    private val repo: Bluetooth = bltBuilder.create(
        scannerSettingsBuilder = { permissions ->
            BaseScanner.Settings(
                permissions = permissions,
                autoRequestPermission = false
            )
         },
        connectionSettings = ConnectionSettings(
            ConnectionSettings.ReconnectionSettings.Never
        )
    )
}
```

The `BluetoothBuilder` is created on the platform

### Android
```kotlin
// Somewhere in Android code
val scanSettings = ScanSettings.Builder()
    .setScanMode(..)
    .setNumOfMatches(..)
    .build()

val scannerBuilder = DefaultScanner.Builder(scanSettings = scanSettings,..)

val bluetoothBuilder = BluetoothBuilder(
    scannerBuilder = scannerBuilder
)
CommonViewModel(bluetoothBuilder)
```

### iOS
```kotlin
// Somewhere in iOS code
val scanSettings = ScanSettings(allowDuplicateKeys, solicitedServiceUUIDsKey)

val scannerBuilder = DefaultScanner.Builder(scanSettings)

val bluetoothBuilder = BluetoothBuilder(scannerBuilder = scannerBuilder)
CommonViewModel(bluetoothBuilder)
```

### Notes
There is a major difference when it comes to the reporting of scanned devices between Android and iOS. Android report multiple scans of the same device, whereas iOS filters them out.

To align the behaviour across platforms the [CBCentralManagerScanOptionAllowDuplicatesKey](https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerscanoptionallowduplicateskey) option is enabled on iOS. It can be set to another value using `ScanSettings` as shown above.

## Testing
Use the [`test-utils-bluetooth` module](../test-utils-bluetooth) to get mockable Bluetooth classes.
