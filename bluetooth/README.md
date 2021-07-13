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