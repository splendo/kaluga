# Bluetooth Annotations

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

The `@Bluetooth` annotations used to describe a Bluetooth device declaratively. A device is annotated with these and
fed to the [`ksp`](../ksp/) processor - wired in by the [code generation plugin](../plugin/) - to generate typed
clients and servers. The annotations are source-retention only: they carry no runtime behaviour and disappear after
compilation.

The annotations fall into four groups:

- **Structure** — `@Bluetooth` (the device), `@BluetoothService(uuid)`, `@BluetoothCharacteristic(uuid)`, `@BluetoothDescriptor(uuid)`
- **Advertising** — `@AdvertisingName(name)` (the local name a generated server advertises) and `@Advertising` (include a service's UUID in the advertisement)
- **Access** — `@Readable`, `@Writable`, `@WritableWithoutResponse`, `@WritableSigned`, `@Notifiable`, `@Indicatable`, `@Encrypted`
- **Naming** — `@BluetoothClientName(name)`, `@BluetoothServerName(name)` (override the generated type names)

See [`Bluetooth.kt`](src/commonMain/kotlin/Bluetooth.kt) for the documented annotations, and the
[code generation plugin README](../plugin/) for how to define a device and generate code from it.

## Installing

The [`com.splendo.kaluga.bluetooth.plugin`](../plugin/) adds this dependency for you. To depend on it directly:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bluetooth:annotations:$kalugaVersion")
}
```
