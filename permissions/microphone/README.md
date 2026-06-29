# Microphone Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

Adds a `MicrophonePermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerMicrophonePermission`()/`PermissionsBuilder.registerMicrophonePermissionIfNotRegistered()`.

## Installing
This library is available on Maven Central. You can import Kaluga Microphone Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga.permissions:microphone:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../core/) for full documentation
