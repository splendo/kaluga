# Storage Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  | ✅ | ✅ |  |

Adds a `StoragePermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerStoragePermission()`/`PermissionsBuilder.registerStoragePermissionIfNotRegistered()`.

## Installing
This library is available on Maven Central. You can import Kaluga Storage Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga.permissions:storage:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../core/) for full documentation
