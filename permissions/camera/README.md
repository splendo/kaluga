# Camera Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ |  |  |

Adds a `CameraPermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerCameraPermission`()/`PermissionsBuilder.registerCameraPermissionIfNotRegistered()`.

## Installing
This library is available on Maven Central. You can import Kaluga Camera Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga.permissions:camera:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../core/) for full documentation
