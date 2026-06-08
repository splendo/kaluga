# Notifications Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

Adds a `NotificationsPermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerNotificationsPermission`()/`PermissionsBuilder.registerNotificationsPermissionIfNotRegistered()`.

On Android this permission will be granted automatically for devices running API 32 or lower.

## Installing
This library is available on Maven Central. You can import Kaluga Notifications Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga.permissions:notifications:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../core/) for full documentation
