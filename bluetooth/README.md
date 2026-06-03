# Bluetooth

This library provides out-of-the-box access to Bluetooth. It is an aggregate of the individual Bluetooth modules, bundling them behind a single `BluetoothBuilder`:

- [`bluetooth-base`](../bluetooth-base) — shared GATT attributes, `GattResponse` and the `BluetoothFormat` (de)serialization framework.
- [`bluetooth-client`](../bluetooth-client) — scanning for and connecting to devices as a Client.
- [`bluetooth-server`](../bluetooth-server) — advertising and exposing attributes as a Server.
- [`bluetooth-permissions`](../bluetooth-permissions) — the `BluetoothPermission`.

Depend on this module to get everything at once, or depend on the individual modules to only include what you need.

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
The `BluetoothBuilder` creates both Client and Server objects:

- Use `BluetoothBuilder.createClient()` to create a `BluetoothClient`. See [`bluetooth-client`](../bluetooth-client) for usage.
- Use `BluetoothBuilder.createServer()` to create a `BluetoothServer`. See [`bluetooth-server`](../bluetooth-server) for usage.

For (de)serializing data exchanged over Bluetooth, see the `BluetoothFormat` documentation in [`bluetooth-base`](../bluetooth-base).

## Testing
Use the [`test-utils-bluetooth-client` module](../test-utils-bluetooth-client) to get mockable Bluetooth classes.
