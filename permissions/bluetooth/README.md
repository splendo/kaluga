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

Note that for using bluetooth on Android 12 and above, it is recommended to declare `android:usesPermissionFlags="neverForLocation"` in your manifest. If not, you need to set the `useForLocation` parameter. 
