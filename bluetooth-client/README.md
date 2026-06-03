# Bluetooth Client

This library provides support for connecting to Bluetooth devices as a Client: scanning for devices, connecting to them and reading, writing and subscribing to their attributes.

## Installing
This library is available on Maven Central. You can import Kaluga Bluetooth Client as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:bluetooth-client:$kalugaVersion")
}
```

## Usage
Create a `BluetoothClient` through the `BluetoothClientBuilder` (or via `BluetoothBuilder.createClient()` from the [`bluetooth`](../bluetooth) module). This gives you access to a `Flow` of Bluetooth devices. To scan for devices simply call

```kotlin
bluetooth.startScanning(setOf(someUUID))
launch {
    bluetooth.devices().collect {} // Gets all devices scanned by the last filter
}
///
bluetooth.stopScanning()
```

Bluetooth Scanning is managed by a state machine that will keep running as long as it is observed. 
It will automatically handle Permissions and Enabling/Disabling bluetooth on the device, although this behaviour can be disabled via the builder.
Devices are returned as a flow of `Device` objects, which manages the connection state of each device.

By default the `BluetoothClient` removes all scanned devices when starting a new scan. Use `CleanMode` to change this behaviour. Using `allDevices` or `scannedDevices` scans for a specific can be accessed.

Devices can be grabbed via a getter method. This returns a flow on which the device can be connected/disconnected from.

```kotlin
launch {
    bluetooth.devices()[someUUID].advertisement().collect { advertisementData ->
        // handle Advertisement Data
    }   
}
// Connect to a device
bluetooth.devices()[someUUID].connect()
// Disconnects a device
bluetooth.devices()[someUUID].disconnect()
```

### Accessing Attributes
From a device it is possible to grab any Gatt Attribute using its UUID. Use `services` to get a List of `RemoteService` objects containing the currently discovered services. After this use getters to access services, characteristics and so on.

```kotlin
// In a suspend method
val descriptor = bluetooth.devices()[someUUID].services()[serviceUUID].characteristics()[characteristicUUID].descriptors()[descriptorUUID].first()
```

Accessors may fail if the attribute with the given UUID is not available. To only get services after discovery has completed, use `discoveredServices`. Alternatively, use `getOrNull` to only get an attribute if it is available to the device:

```kotlin

// In a suspend method
val characteristic = bluetooth.devices()[someUUID].discoveredServices()[serviceUUID].characteristics().getOrNull(characteristicUUID).first()
```

### Read, Write, and Notify
From a `RemoteCharacteristic` or `RemoteDescriptor` data can be read or written to:

```kotlin
// Read
when (val response = characteristic.read()) {
    is GattResponse.ReadSuccess -> println("Did Read ${response.value.toHexString()}")
    is GattResponse.WriteError -> println("Failed with ${response.statusCode}")
}

// Write
descriptor.write(byteArrayOf())
```

Or for a `RemoteCharacteristic` subscribe to a notification:

```kotlin
val subscription = characteristic.subscribe {
    println("Notified of ${it.toHexString()}")
}
subscription.unsubscribe() // Must be called when done

// Alternative approach:
characteristic.value().collect {
    println("Notified of ${it.toHexString()}")
}
```

All these methods have alternative implementations that automatically (de)serialize a ByteArray into a kotlin class. See the `BluetoothFormat` documentation in [`bluetooth-base`](../bluetooth-base).

#### Binding
Using the `bind` methods, an object can be make to transform based on bluetooth communication:

```kotlin
val value = "".bind(device) {
    service(serviceUUID) {
        characteristic(serviceUUID) {
            observe {
                mutate {
                    onNotification { data ->
                        data.toHexString()
                    }
                }
            }
            
            // Observe flows or channels to trigger read/writes
            flowOf(1, 2 ,3).collectTo {
                triggerRead {
                    onRead { response, trigger ->
                        println("$trigger Did read $response.toHexString()")
                    }
                }
            }
        }
    }
}
```

### Android
When using automatic permissions by default only the relevant Bluetooth permissions are asked for, and not the location permission (unless the Android version is lower than 12 where it is always required). Make sure you include `android:usesPermissionFlags="neverForLocation"`, unless you do use Bluetooth to determine location, in which case you can use the `useLocation` flag in `BaseScanner.Settings`.

### Notes
There is a major difference when it comes to the reporting of scanned devices between Android and iOS. Android report multiple scans of the same device, whereas iOS filters them out.

To align the behaviour across platforms the [CBCentralManagerScanOptionAllowDuplicatesKey](https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerscanoptionallowduplicateskey) option is enabled on iOS. It can be set to another value using `ScanSettings` as shown above.

## Testing
Use the [`test-utils-bluetooth-client` module](../test-utils-bluetooth-client) to get mockable Bluetooth classes.
