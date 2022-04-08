## Permissions

This library provide support for out-of-the-box access to Bluetooth.


### Usage
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
You may notice that when you ask for kaluga's `Permission.Bluetooth` the android request alert will prompt `Location` permission. This behaviour is encountered because Android system requires Location to access the hardware identifiers of nearby external devices via Bluetooth.

In order to setup a bluetooth repo you need to do the following:
```kotlin
// Somewhere in Android code
val permissions = Permissions(
    PermissionsBuilder().apply {
        registerBluetoothPermission()
        registerLocationPermission()
    }
)

val scanSettings = ScanSettings.Builder()
    .setScanMode(..)
    .setNumOfMatches(..)
    .build()

val scannerBuilder = Scanner.Builder(scanSettings = scanSettings,..)

val bluetoothBuilder = BluetoothBuilder(permissions = permissions)
CommonViewModel(bluetoothBuilder)
...

// Somewhere in iOS code
val permissions = Permissions(
    PermissionsBuilder().apply {
        registerBluetoothPermission()
        registerLocationPermission()
    }
)

val scanSettings = ScanSettings(allowDuplicateKeys, solicitedServiceUUIDsKey)

val scannerBuilder = Scanner.Builder(scanSettings)

val bluetoothBuilder = BluetoothBuilder(permissions = permissions, scannerBuilder = scannerBuilder)
CommonViewModel(bluetoothBuilder)
...

// Common code
class CommonViewModel(bltBuilder: BluetoothBuilder) : BaseViewModel() {
    private val repo: Bluetooth = bltBuilder.create(
        connectionSettings = ConnectionSettings(
            ConnectionSettings.ReconnectionSettings.Never
        ),
        autoRequestPermission = false
    )
} 
```

### Notes
There is a major difference when it comes to default reported device emissions between Android and iOS. Android will report multiple emissions of the same device, as iOS will filter them out.

To align the behaviour across platform the [CBCentralManagerScanOptionAllowDuplicatesKey](https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerscanoptionallowduplicateskey) option is enabled on iOS.
