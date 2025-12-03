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
Accessing the Bluetooth library can be done through the `BluetoothBuilder`:

- Use `BluetoothBuilder.createClient()` to create a `Bluetooth` object that acts as a Client
- Use `BluetoothBuilder.createServer()` to create a `BluetoothServer` object that acts as a Server

## Client
Create a `Bluetooth` object through the `BluetoothBuilder`. This gives you access to a `Flow` of Bluetooth devices. To scan for devices simply call

```kotlin
bluetooth.startScanning(setOf(someUUID))
launch {
    bluetooth.devices().collect {} // Gets all devices scanned by the last filter
}
///
bluetooth.stopScanning()
```

Bluetooth Scanning is managed by a state machine that will keep running as long as it is observed. 
It will automatically handle Permissions and Enabling/Disabling bluetooth on the device, although this behaviour can be disabled via the `BluetoothBuilder`
Devices are returned as a flow of `Device` objects, which manages the connection state of each device.

By default the `Bluetooth` removes all scanned devices when starting a new scan. Use `CleanMode` to change this behaviour. Using `allDevices` or `scannedDevices` scans for a specific can be accessed.

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

All these methods have alternative implementations that automatically (de)serialize a ByteArray into a kotlin class. See [Serialization](#Serialization)

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

## Server
Create a `BluetoothServer` through the `BluetoothBuilder`. In it, specify the advertising and the attributes to support:

```kotlin

val server = builder.createServer(context) {
    advertise {
        localName = "Kaluga Server"
        serviceUUIDs(kalugaUUID)
    }
    service(kalugaUUID) {
        characteristic(characteristicUUID) {
            readable {
                GattResponse.ReadSuccess(byteArrayOf())
            }
            writable { _, data, _ ->
                println("Did write ${data.toHexString()}")
                GattResponse.WriteSuccess
            }
            notifiable {
                onSubscribe {
                    coroutineScope.launch {
                        notify(byteArrayOf())
                    }
                }
            }
            
            // Alternative notification
            flowOf(byteArrayOf()).collectTo(coroutineScope) {
                triggerNotification()
            }
            
            // Ignored on iOS
            descriptor(descriptorUUID) {
                
            }
        }
    }
}

val notifiableCharacteristic = server.services[kalugaUUID].characteristics[characteristicUUID] as LocalCharacteristic.Notifiable
notifiableCharacteristic.notifyAll(byteArrayOf())

server.close() // server must be closed when done
```

## Serialization
The `BluetoothFormat` can be used to easily (De)Serialize Objects into ByteArrays usable by most Bluetooth protocols. By default this will serialize as follows:

Booleans will be added to the next available byte. Up to 8 booleans in a row will fit in a single byte, their bit position determined by their order. 
Numbers will be serialized as their byte length implies (e.g. an Int is 4 bytes, a Short 2)
Strings and Collections will be prefixed with a single byte containing their length. If longer encoding will fail.
Polymorphic and Enum classes will serialize their serialName as a string with no length prefix.
When an element is nullable, a nullable flag will be added to the flags header. The position in the flags header is determined by order of the element within the object.

Multiple flags are available to change this behaviour:

- Use `@Prefix` to always add bytes to the start of the structure.
- Use `@Postfix` to always add bytes to the end of the structure.
- Use `@Checksum` to add checksum bytes (of width `Checksum.width`) between the body and the `Postfix`. Checksum will be calculated over body only.
When decoding, if `validateChecksum` is `true` the checksum will be automatically compared to the checksum in the data and throw an exception if they don't match.
- Use `@FlagIndex` to change the position of the header flag(s) to be used for storing headers.
If applied to a Boolean, the boolean will be stored as a flag instead of within the body itself.
- Use `@FlagWidth` to change the width of the flags to be used by this element. If the desired width is bigger than this width, flag will be ignored.
- Use `@ByteOrder` to change the byte order in which this element is encoded. Nested structures must have the same byte order, though primary types can change.
- Use `@LengthPrefix` to change the length prefix used in Strings or Collections.
- Use `@Encoded` to change the encoding used in Strings.
- Use `@NullTerminated` to change the end marking of a Collection of String to be determined by a null byte instead of a length prefix
- Use `@Unsigned` to encode numbers as unsigned.
- Use `@Scalar` to encode numbers as a scalar value. Length can be modified using `@Size`, defaults to ``Length.`16_BIT` `` for Floats and ``Length.`32_BIT` `` for Doubles
- Use `@MedFloat` to encode numbers as a MedFloat value. Can have its sizing determined by `@Size`. When ``Length.`16_BIT` `` encodes as `com.splendo.kaluga.base.utils.MedFloat16`, when ``Length.`32_BIT` `` encodes as `com.splendo.kaluga.base.utils.MedFloat32`.
Any other `@Size` is not allowed.
- Use `@Size` to change the length of the bytes used to encode a numeric value.
When multiple are added, the smallest `Length` that fits the entire number will be used and flags will be added to the header to indicate which size was picked.
For Float/Double values, this can only be ``Length.`32_BIT` `` or ``Length.`64_BIT` ``, for `@MedFloat` it is restricted to ``Length.`16_BIT` `` and ``Length.`32_BIT` ``.
- Use `@Unsized` to mark a String or Collection as Unsized, meaning all remaining bytes (with the exception of any `@Checksum` or `@Postfix`) belong to this object.
Attempting to encode data after will lead to an exception.
- Use `@NullIfEmpty` to mark a Collection as nullable if it is empty. When null its size will not be encoded.
- Use `@SerializedByteValue` to change the byte identifier of an Enum or Polymorphic class. This replaces serializing its serial name as an unsized string.

Equivalent flags are available to encode items in a List (e.g. `ItemSize`) or key/values in a Map (e.g. `KeyEncoded`, `ValueNullTerminated`)

As an Example, this is what the Bluetooth Heart Rate Characteristic looks like:

```kotlin
@Serializable
@JvmInline
value class RRInterval private constructor(
    @Size(Length.`16_BIT`)
    @Scalar(binaryExponent = 10)
    val seconds: Double,
) {
    constructor(duration: Duration) : this(duration.toDouble(DurationUnit.SECONDS))

    val duration: Duration get() = seconds.seconds
}

@Serializable
enum class SensorLocation {
    @SerializedByteValue(0x00)
    OTHER,
    @SerializedByteValue(0x01)
    CHEST,
    @SerializedByteValue(0x02)
    WRIST,
    @SerializedByteValue(0x03)
    FINGER,
    @SerializedByteValue(0x04)
    HAND,
    @SerializedByteValue(0x05)
    EAR_LOBE,
    @SerializedByteValue(0x06)
    FOOT,
}

@Serializable
data class HeartRate(
    @Size(Length.`8_BIT`)
    @Size(Length.`16_BIT`)
    @Unsigned
    val heartRate: Int,
    @FlagIndex(1)
    val contactSupported: Boolean,
    @FlagIndex(2)
    val contactDetected: Boolean = !contactSupported,
    @Unsigned
    @Size(Length.`16_BIT`)
    val energyExpended: Int? = null,
    @NullIfEmpty
    @Unsized
    val rrIntervals: List<RRInterval> = emptyList(),
)
```

## Testing
Use the [`test-utils-bluetooth` module](../test-utils-bluetooth) to get mockable Bluetooth classes.
