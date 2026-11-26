# Bluetooth Client

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

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
    implementation("com.splendo.kaluga.bluetooth:client:$kalugaVersion")
}
```

## Usage
Create a `BluetoothClient` through the `BluetoothClientBuilder`. This gives you access to a `Flow` of Bluetooth devices. To scan for devices simply call

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
    is GattResponse.ReadError -> println("Failed with ${response.statusCode}")
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

Each of these methods also has an overload that takes a kotlinx.serialization `DeserializationStrategy` or `SerializationStrategy` and converts the bytes to or from a `@Serializable` class with [`BytesFormat`](../../bytes/serialization/), for example `characteristic.read(HeartRate.serializer())`.

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

### Disconnecting
Whether the disconnect was requested or the link dropped, everything pending on the connection and everything discovered on it settles the same way; nothing throws and nothing stays pending.

- Pending actions answer with a typed failure. `read()` and `write()` return `GattResponse.DeviceUnavailable`, `subscribe {}` returns `FailedToSubscribe(GattResponse.DeviceUnavailable)`, `enableNotification()`, `disableNotification()` and `Subscription.unsubscribe()` return `GattResponse.DeviceUnavailable`, and `requestMtu` returns `GattResponse.MTUDeviceUnavailable`. This covers the action in flight, the actions queued behind it and actions issued while the device is not in a discovered state. A requested disconnect answers the queued actions as it starts; the action in flight still gets the device's answer if it arrives before the link closes.
- A failure the platform reports as the link drops is wrapped. Where Android fails the action in flight with an error status and already reports the device as disconnected at that moment, the action answers `DeviceUnavailable` (`MTUDeviceUnavailable` for `requestMtu`) with that error as its `cause`. The `cause` is `null` when the disconnect itself answered the action. Both are data classes, so one with a `cause` is not equal to one without: check for a lost link with `is GattResponse.DeviceUnavailable`.
- Flows end. `value()` (and the notification flows of generated clients) complete normally, `isNotifying` becomes `false` and subscriptions are dropped.
- Objects stay stale. A `RemoteService`, `RemoteCharacteristic` or `RemoteDescriptor` of the ended connection never becomes usable again; later calls on it answer with the same typed failures at once, without reaching the device. After reconnecting, `discoveredServices()` emits new objects.
- `invalidated` tells you. Each of these objects has an `invalidated: Deferred<Unit>` completed when its connection ends (a `reset()` of the connection manager ends it too). Await it, or bound a collection with it:

```kotlin
launch {
    characteristic.invalidated.await()
    // rediscover
}
```

A notification request that is replaced by the opposite request before it reached the device (a disable followed by an enable while a previous enable is still in flight) answers `GattResponse.Superseded`.

### Android
When using automatic permissions by default only the relevant Bluetooth permissions are asked for, and not the location permission (unless the Android version is lower than 12 where it is always required). Make sure you include `android:usesPermissionFlags="neverForLocation"`, unless you do use Bluetooth to determine location, in which case you can use the `useLocation` flag in `BaseScanner.Settings`.

### JavaScript and WebAssembly (Web Bluetooth)
On the JS family (`js` and `wasmJs`) the client is backed by the [Web Bluetooth API](https://developer.mozilla.org/en-US/docs/Web/API/Web_Bluetooth_API). This API is **Chromium-only**, requires a **secure context** (HTTPS or `localhost`), and is **central/client only** — there is no peripheral/GATT-server role (the `server` module is not available for web). RSSI, MTU negotiation and pairing have no Web Bluetooth equivalent: reading RSSI and pairing are no-ops and an MTU request resolves to `GattResponse.MTUNotPermitted`.

#### Scanning is an "Add Device" overlay
Web Bluetooth has no free-running scan, and its `navigator.bluetooth.requestDevice` device picker must be opened from a user gesture. The `DefaultScanner` therefore does not call `requestDevice` directly: while a scan is active it renders an **"Add Device" overlay** in the DOM. Each press of its button opens the system picker (from within the click handler, satisfying the gesture requirement) and adds the chosen device to the scan results, so a single scan can collect any number of devices. The overlay carries its own close (`✕`) button and is removed automatically when scanning stops.

#### Optional services
Web Bluetooth only grants access to GATT services that are declared **up front** — a service that is not advertised, or only available after connecting, is unreachable unless it is listed as an optional service. Supply these through the scanner's builder; they are applied to every picker invocation:

```kotlin
val builder = BluetoothClientBuilder(
    scannerBuilder = DefaultScanner.Builder(
        optionalServices = listOf(heartRateServiceUUID, batteryServiceUUID),
    ),
)
```

The per-scan `Filter` passed to `startScanning` is separate: it only narrows which devices the picker *shows* (by advertised services), whereas `optionalServices` is the advertisement-independent access allowlist.

#### Permissions
There is no upfront Bluetooth permission prompt on the web — access is granted per device when the user picks one through the overlay. The Bluetooth permission therefore reports granted whenever Web Bluetooth is available (the browser's experimental `navigator.permissions.query({ name: 'bluetooth' })` is not relied upon, as it is unsupported in most browsers).

#### Styling the overlay
The overlay is configured with `WebDevicePickerSettings` (passed to `DefaultScanner.Builder`):

```kotlin
DefaultScanner.Builder(
    optionalServices = listOf(heartRateServiceUUID),
    pickerSettings = WebDevicePickerSettings(
        title = "Bluetooth Devices",
        addButtonLabel = "Add Device",
        emptyLabel = "No devices added yet",
        cssClassPrefix = "kaluga-bluetooth", // default
        containerId = null, // mount in document.body when null
    ),
)
```

No inline styles are applied — every element gets a class derived from `cssClassPrefix`, so the host page styles it entirely through CSS:

| Class (default prefix) | Element |
| --- | --- |
| `kaluga-bluetooth-overlay` | the overlay container |
| `kaluga-bluetooth-title` | the heading |
| `kaluga-bluetooth-close` | the `✕` dismiss button |
| `kaluga-bluetooth-list` | the added-device list (`<ul>`) |
| `kaluga-bluetooth-list-item` | a device entry |
| `kaluga-bluetooth-list-empty` | the placeholder shown before any device is added |
| `kaluga-bluetooth-button` | the "Add Device" button |

> **The overlay has no visual styling by default.** Kaluga applies `position: fixed` and `z-index: 1000` as inline styles so the overlay always renders above canvas-based renderers (such as **Compose for Web**) without any host-page CSS. All visual properties — colour, padding, border-radius, typography — are left entirely to the host page via the CSS classes below. To override `z-index`, use `!important` in your CSS rule.

A complete example that floats the overlay in the top-right corner:

```css
.kaluga-bluetooth-overlay {
    position: fixed;
    top: 16px;
    right: 16px;
    z-index: 1000;
    padding: 16px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.25);
}
.kaluga-bluetooth-close { position: absolute; top: 8px; right: 8px; border: none; background: transparent; cursor: pointer; }
.kaluga-bluetooth-button { width: 100%; padding: 8px 12px; border: none; border-radius: 6px; background: #1a73e8; color: #fff; cursor: pointer; }
```

### Notes
There is a major difference when it comes to the reporting of scanned devices between Android and iOS. Android report multiple scans of the same device, whereas iOS filters them out.

To align the behaviour across platforms the [CBCentralManagerScanOptionAllowDuplicatesKey](https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerscanoptionallowduplicateskey) option is enabled on iOS. It can be set to another value using `ScanSettings` as shown above.

## Platform differences
Android and CoreBluetooth answer some situations differently, and Kaluga passes their answers on as they come, apart from wrapping a failure reported as the link drops (see [Disconnecting](#disconnecting)):

| Situation | Android | CoreBluetooth |
| --- | --- | --- |
| The peripheral answers a read with Insufficient Authentication | The stack tries to pair; when the peripheral refuses, the read fails with `ApplicationError(0x89)` (Android's `GATT_AUTH_FAIL`) | CoreBluetooth tries to pair; when the peripheral refuses, the read fails with `InsufficientEncryption` |
| The same answer to a write | The stack tries to pair and never answers the write; it drops the link about three seconds later and the write answers `DeviceUnavailable` | The write fails with `InsufficientEncryption` and the link stays |
| Reading a value longer than 512 bytes | The read succeeds with the first 512 bytes | The read stops after the response that passes 512 bytes and succeeds with all it read, so the value can run past 512 bytes (516 at an ATT MTU of 517, 528 at 23) |
| Writing more than 512 bytes | Nothing is sent; the write answers `InvalidAttributeValueLength` | Sent as a long write; the answer is the peripheral's |
| A notification longer than 512 bytes, possible at an MTU above 515 | Dropped by the stack; nothing reaches the app | Delivered whole |
| Write without response | `WriteSuccess.Acknowledged` once the stack has taken the write | `WriteSuccess.Ready`: the peripheral was ready to take it, nothing confirms that it arrived |
| A read the peripheral never answers | After 30 seconds the stack fails the read with `GATT_ERROR` (0x85) as it drops the link, so the read answers `DeviceUnavailable(cause = ApplicationError(0x85))` | After 30 seconds CoreBluetooth closes the link without failing the read, so the disconnect answers it with `DeviceUnavailable` without a cause |
| A request failed as the link drops | The stack fails the request with an error status some milliseconds before it reports the disconnect; as the platform already reports the device as disconnected, the request answers `DeviceUnavailable` with the mapped error as its `cause`. The same status sent by a peripheral on a link that stays answers as the plain error, such as `ApplicationError(0x85)` | The request answers the error code CoreBluetooth reports, read as an ATT error |

An Android client stays at the default ATT MTU of 23 until the app calls `requestMtu`, so a value that does not fit one packet takes a long read or write. CoreBluetooth negotiates the MTU by itself when connecting and has no API to request one: `requestMtu` sends nothing and answers `MTUSuccess` with the longest write without response CoreBluetooth allows plus the 3 byte ATT header. That follows the ATT MTU, 23 at the default, but CoreBluetooth caps such a write at 512 bytes, so at the largest ATT MTU of 517 it reports 515.

Where a value is cut, dropped or routed without the app being told, Kaluga logs it at debug level on the connection's state logger, set through `ConnectionSettings`:

- a read that ends at or past 512 bytes;
- a write longer than 512 bytes that Android does not send;
- notifications enabled on a characteristic that shares its UUID with another notifying characteristic: notifications are routed by UUID, so those of both reach the one enabled last;
- a status code that is no known error, answered as `UnknownError` or as the `cause` of a `DeviceUnavailable`;
- on Android, a Service Changed indication, after which the services discovered before stay in use.

The message is only built when the logger takes debug messages.

## Running the client over another transport
The client does not depend on the platform Bluetooth stack: `DefaultBluetoothClient`, the device state machines and the generated service implementations only talk to a `Scanner`, a `DeviceConnectionManager` and the attribute wrappers. Another transport (an emulator reached over a socket, an in-process fake, a bridge to a different Bluetooth library) plugs in at that seam, in common code:

- `PortableRemoteServiceWrapper`, `PortableRemoteCharacteristicWrapper`, `PortableRemoteDescriptorWrapper` and `PortableDeviceWrapper` are the attribute and device wrappers to extend. The platform `RemoteServiceWrapper` (etc.) interfaces add members only Kaluga's own connection managers use; these classes implement them, so a subclass written in common code implements just the common members (`uuid`, `type`, `characteristics`, `properties`, ...).
- `BaseDeviceConnectionManager` is the connection manager to extend. Implement `connect`, `intentionalDisconnect`, `discoverServices`, `didStartPerformingAction` and the pairing/RSSI requests against the transport, and report results through the protected `handle…` methods: `handleConnect`/`handleDisconnect`, `createService` + `handleDiscoverCompleted` after discovery, `handleCharacteristicReadOrNotified`/`handleCharacteristicWritten`/`handleDescriptorRead`/`handleDescriptorWritten`, `handleNotificationStateChanged` for a notification action and `handleCharacteristicNotified` for a pushed value, `handleNewMtu` and `handleNewRssi`. `handleDisconnect` is the one path for a lost or requested disconnect: it answers the pending action, invalidates the services created on the connection and fires the `Disconnected` event, so a transport reports nothing else. A completion reported for an action that was already answered (a callback arriving after the disconnect) is ignored.
- A `Scanner` (or `BaseScanner`) emits `Scanner.Event.PermissionChanged`, `BluetoothEnabled`/`BluetoothDisabled` and `DeviceDiscovered` events. Each `DeviceDiscovered` carries a device creator; build it with `ConnectableDeviceImpl` and `DeviceInfoImpl` around your `PortableDeviceWrapper` and a `DeviceConnectionManager.Builder` creating your manager.
- `DefaultBluetoothClient(coroutineContext) { context -> ScanningStateImplRepo(createScanner, contextForIdentifier, context) }` wires that scanner into a regular `BluetoothClient`, so everything built on the client, including generated clients, runs unchanged over the new transport. A `BaseBluetoothClientBuilder` returning it slots into code that is handed a builder.

`PortableWrapperTransportTest` in this module's common tests shows the whole loop over an in-process transport.

## Testing
Use the [`test-utils/client` module](../test-utils/client/) to get mockable Bluetooth classes.
