# Location Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

Adds a `LocationPermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerLocationPermission`()/`PermissionsBuilder.registerLocationPermissionIfNotRegistered()`.

The `LocationPermission` can be set to acquire location data more accurately or while the app is the background.

## Installing
This library is available on Maven Central. You can import Kaluga Location Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga.permissions:location:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../core/) for full documentation
