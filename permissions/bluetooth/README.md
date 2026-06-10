# Bluetooth Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

Adds a `BluetoothPermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerBluetoothPermission()`/`PermissionsBuilder.registerBluetoothPermissionIfNotRegistered()`.

## Installing
This library is available on Maven Central. You can import Kaluga Permissions Bluetooth as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga.permissions:bluetooth:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../core/) for full documentation

## Android manifest

The Android permissions are scoped by `BluetoothPermission.Type`, and each module declares only the permissions it needs, so an app inherits only what it uses:

- `bluetooth-client` declares `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, and `ACCESS_FINE_LOCATION` (`maxSdkVersion="30"`, since location is mandatory for scanning below API 31).
- `bluetooth-server` declares `BLUETOOTH_ADVERTISE`.
- `bluetooth-core` declares the legacy `BLUETOOTH`/`BLUETOOTH_ADMIN` (`maxSdkVersion="30"`) shared by both.

Location is intentionally left to the app on API 31+, as it depends on your use case:

- If you scan for location (`BluetoothPermission.Type.Client(useForLocation = true)`), declare `ACCESS_FINE_LOCATION` (without a `maxSdkVersion`) in your manifest.
- Otherwise, declare `BLUETOOTH_SCAN` with `android:usesPermissionFlags="neverForLocation"` in your manifest to decouple scanning from the location permission, and keep `useForLocation = false` (the default).
