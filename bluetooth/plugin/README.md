# Bluetooth code generation plugin

`com.splendo.kaluga.bluetooth.plugin` is a Gradle plugin that generates typed Bluetooth **clients** and **servers**
from a declarative, annotated definition of a device. You describe the GATT layout once — services, characteristics,
descriptors and how each value is accessed — and the plugin generates strongly-typed APIs (and their implementations)
on top of [`bluetooth-client`](../client/) and [`bluetooth-server`](../server/), so you never hand-write attribute
(de)serialization or characteristic plumbing.

It is built from three pieces:

| Module | Usage | Artifact |
|---|---|---|
| [`annotations`](../annotations/) | The `@Bluetooth` annotations used to describe a device | `com.splendo.kaluga.bluetooth:annotations` |
| [`ksp`](../ksp/) | The KSP processor that generates the code | `com.splendo.kaluga.bluetooth:ksp` |
| [`plugin`](plugin/) | The Gradle plugin wiring KSP, the processor and the runtime dependencies together | `com.splendo.kaluga.bluetooth.plugin` |

## Applying the plugin

The plugin applies the Kotlin Multiplatform and KSP plugins itself, and adds the `annotations`, `core` and (depending
on configuration) `client` / `server` runtime dependencies to `commonMain`. Apply it and configure your targets as
usual:

```kotlin
plugins {
    id("com.splendo.kaluga.bluetooth.plugin")
}

kotlin {
    androidTarget()
    iosArm64()
    iosSimulatorArm64()
    // macosArm64(), etc.
}
```

## Defining a device

A device is a class or interface annotated with `@Bluetooth`. It exposes its services as properties; a service exposes
its characteristics; a characteristic exposes its values, each marked with how it can be accessed. Descriptors are
nested in the same way.

```kotlin
import com.splendo.kaluga.bluetooth.annotations.*

@Bluetooth
@AdvertisingName("Kaluga Demo")
interface DemoDevice {
    @Advertising
    val sensor: SensorService
}

@BluetoothService("d000")
interface SensorService {
    val reading: ReadingCharacteristic
    val config: ConfigCharacteristic
}

@BluetoothCharacteristic("d001")
interface ReadingCharacteristic {
    @Readable
    val value: Int

    @Notifiable
    val live: Short
}

@BluetoothCharacteristic("d002")
interface ConfigCharacteristic {
    @Writable
    val threshold: Int

    @Indicatable
    val status: Short

    @BluetoothDescriptor("d003")
    val info: InfoDescriptor
}

interface InfoDescriptor {
    @Readable
    val name: String
}
```

See the [annotations](../annotations/src/commonMain/kotlin/Bluetooth.kt) for the full set:

- **Structure:** `@Bluetooth`, `@BluetoothService(uuid)`, `@BluetoothCharacteristic(uuid)`, `@BluetoothDescriptor(uuid)`
- **Advertising:** `@AdvertisingName(name)` (the server's local name), `@Advertising` (include a service's UUID in the advertisement)
- **Access:** `@Readable`, `@Writable`, `@WritableWithoutResponse`, `@WritableSigned`, `@Notifiable`, `@Indicatable`, `@Encrypted`
- **Naming:** `@BluetoothClientName(name)`, `@BluetoothServerName(name)` (override the generated type names)

## Configuring generation

Generation is configured through the `bluetooth { }` extension:

```kotlin
import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.BLUETOOTH, ImplementFor.SIMULATOR))
}
```

| Option | Default | Description |
|---|---|---|
| `target` | `[CLIENT]` | Roles to generate: `CLIENT` (central), `SERVER` (peripheral), or both. |
| `implementFor` | `[BLUETOOTH]` | Implementations to generate per role: `BLUETOOTH` (real platform stack) and/or `SIMULATOR` (in-process loopback). |
| `apiOnly()` | — | Generate only the API interfaces (no implementation); the module then depends only on `bluetooth-core`. |
| `useExternalApi()` | — | Generate implementations only, importing the API from another module that used `apiOnly()`. |
| `generatedPackage` | package of the definitions | Package the generated code is placed in. |
| `apiPackage` | `generatedPackage` | Package the generated API interfaces live in (set on an implementation module that `useExternalApi()`). |
| `annotationSource(path)` | — | Add a directory of annotated definitions to generate from; lets multiple modules share one set of definitions. |

## What gets generated

For a `@Bluetooth DemoDevice` the plugin generates, according to `target` / `implementFor`:

- A `DemoDeviceClient` / `DemoDeviceServer` API (interfaces mirroring the device's services, characteristics and descriptors).
- A `BluetoothDemoDeviceClient` / `BluetoothDemoDeviceServer` backed by the platform Bluetooth stack (`ImplementFor.BLUETOOTH`).
- A `SimulatedDemoDeviceClient` / `SimulatedDemoDeviceServer` that talk to each other in-process (`ImplementFor.SIMULATOR`).
- Factory functions to obtain them, e.g.:

```kotlin
// client over a real connection (bluetooth-client)
val client = DemoDeviceClient.bluetooth(bluetoothClient, deviceIdentifier)
val result = client.sensor.reading.readValue()        // @Readable  -> suspending read, typed result
client.sensor.reading.live.collect { live -> /* … */ } // @Notifiable -> Flow of value changes
client.sensor.config.writeThreshold(42)               // @Writable  -> suspending write

// server over the real stack (bluetooth-server)
val server = DemoDeviceServer.bluetooth(serverBuilder, delegate)

// in-process simulation, no radio involved
val simulatedServer = DemoDeviceServer.simulated(delegate)
val simulatedClient = DemoDeviceClient.simulated(identifier, simulatedServer)
```

The generated client and server APIs are implementation-agnostic: the same `DemoDeviceClient` code works against the
real `Bluetooth*` implementation or the `Simulated*` one, which makes the simulator useful for previews and tests.

## Sharing definitions across modules

To split the API and implementation across modules — for example a shared API module consumed by both a client app and
a server app — generate the API once and import it elsewhere:

- API module: `bluetooth { apiOnly() }` (depends only on `bluetooth-core`).
- Implementation module: `bluetooth { useExternalApi(); apiPackage = "<api package>" }`, depending on the API module,
  and `annotationSource("<path to the shared definitions>")` so it generates against the same device.

## Validation

[`example/`](example/) is a standalone composite build of validation fixtures — one module per plugin capability
(`full`, `client`, `server`, `simulator`, `external-api-*`) — that exercises the generator and confirms the generated
code compiles on every supported target. It is built in CI; see [`.github/workflows`](../../.github/workflows). A
runnable demo of the generated code lives in the main example app under
[`example/feature-bluetooth-generation`](../../example/feature-bluetooth-generation/).
